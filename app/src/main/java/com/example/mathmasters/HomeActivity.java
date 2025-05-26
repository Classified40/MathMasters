package com.example.mathmasters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.AlertDialog;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    TextView welcomeText, streakText;
    Button startBtn, signOutBtn, dailyChallengeBtn, mathFactBtn;
    SharedPreferences prefs;
    String prefsName = "prefs";
    String streakKey = "streak";
    String lastDateKey = "last_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.tv_welcome);
        streakText = findViewById(R.id.tv_streak);
        startBtn = findViewById(R.id.btn_start_practice);
        signOutBtn = findViewById(R.id.btn_sign_out);
        dailyChallengeBtn = findViewById(R.id.btn_daily_challenge);
        mathFactBtn = findViewById(R.id.btn_math_fact);

        welcomeText.setText("Welcome, Math Master!");

        // get streak
        prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        int streak = prefs.getInt(streakKey, 0);
        streakText.setText("Streak: " + streak + " days");

        // Check if we need to update streak from daily challenge
        if (getIntent().getBooleanExtra("updateStreak", false)) {
            streak = prefs.getInt(streakKey, 0);
            streakText.setText("Streak: " + streak + " days");
            Toast.makeText(this, "Streak increased!", Toast.LENGTH_SHORT).show();
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStreak();
                Intent i = new Intent(HomeActivity.this, LevelActivity.class);
                startActivity(i);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        dailyChallengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, DailyChallengeActivity.class);
                startActivity(i);
            }
        });

        mathFactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchMathFact();
            }
        });

        setDailyReminderAlarm();
        setStreakResetAlarm();
    }

    // update streak if practiced today
    private void updateStreak() {
        String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String lastDate = prefs.getString(lastDateKey, "");
        int streak = prefs.getInt(streakKey, 0);
        if (!today.equals(lastDate)) {
            // If last practice was yesterday, increase streak
            if (isYesterday(lastDate, today)) {
                streak = streak + 1;
            } else {
                streak = 1;
            }
            prefs.edit().putInt(streakKey, streak).putString(lastDateKey, today).apply();
            streakText.setText("Streak: " + streak + " days");
        }
    }

    // check if lastDate is yesterday
    private boolean isYesterday(String lastDate, String today) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            Calendar calLast = Calendar.getInstance();
            Calendar calToday = Calendar.getInstance();
            calLast.setTime(sdf.parse(lastDate));
            calToday.setTime(sdf.parse(today));
            calLast.add(Calendar.DAY_OF_YEAR, 1);
            return calLast.get(Calendar.YEAR) == calToday.get(Calendar.YEAR) &&
                    calLast.get(Calendar.DAY_OF_YEAR) == calToday.get(Calendar.DAY_OF_YEAR);
        } catch (Exception e) {
            return false;
        }
    }

    private void setDailyReminderAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setAction("REMINDER");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long triggerTime = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > triggerTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            triggerTime = calendar.getTimeInMillis();
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void setStreakResetAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setAction("RESET");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long triggerTime = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > triggerTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            triggerTime = calendar.getTimeInMillis();
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void fetchMathFact() {
        new Thread(() -> {
            try {
                URL url = new URL("http://numbersapi.com/random/math?json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                String json = response.toString();
                String fact = parseFactFromJson(json);
                new Handler(Looper.getMainLooper()).post(() -> showMathFactDialog(fact));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> showMathFactDialog("Could not fetch fact. Try again."));
            }
        }).start();
    }

    private String parseFactFromJson(String json) {
        // Very simple parse, not using a library
        int idx = json.indexOf("\"text\":");
        if (idx == -1) return "No fact found.";
        int start = json.indexOf('"', idx + 7) + 1;
        int end = json.indexOf('"', start);
        if (start == 0 || end == -1) return "No fact found.";
        return json.substring(start, end);
    }

    private void showMathFactDialog(String fact) {
        new AlertDialog.Builder(this)
                .setTitle("Math Fact")
                .setMessage(fact)
                .setPositiveButton("OK", null)
                .show();
    }
} 