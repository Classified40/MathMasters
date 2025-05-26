package com.example.mathmasters;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "streak_reminder_channel";
    private static final int NOTIF_ID = 1001;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String action = intent.getAction();
        String lastDate = prefs.getString("last_date", "");
        String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if ("REMINDER".equals(action)) {
            if (!today.equals(lastDate)) {
                createNotificationChannel(context);
                Intent openIntent = new Intent(context, HomeActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Don't lose your streak!")
                        .setContentText("Practice at least one exercise today to keep your streak.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIF_ID, builder.build());
            }
        } else if ("RESET".equals(action)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                Calendar calToday = Calendar.getInstance();
                Calendar calLast = Calendar.getInstance();
                calToday.setTime(sdf.parse(today));
                calLast.setTime(sdf.parse(lastDate));
                calLast.add(Calendar.DAY_OF_YEAR, 1);
                String yesterday = sdf.format(calLast.getTime());
                if (!lastDate.equals(today) && !lastDate.equals(yesterday)) {
                    // streak is already broken, do nothing
                    return;
                }
                if (!lastDate.equals(today)) {
                    // User did not practice yesterday, reset streak
                    prefs.edit().putInt("streak", 0).apply();
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Streak Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminds user to practice daily to keep their streak");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
} 