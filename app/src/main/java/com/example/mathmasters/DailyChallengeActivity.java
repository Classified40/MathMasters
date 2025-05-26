package com.example.mathmasters;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.Toast;
import android.content.Intent;

public class DailyChallengeActivity extends AppCompatActivity {
    TextView questionText, feedbackText, timerText;
    Button answerBtn1, answerBtn2, answerBtn3, answerBtn4, nextBtn;
    int currentQuestion = 0;
    int score = 0;
    int tries = 0;
    List<Question> questions;
    CountDownTimer countDownTimer;
    long timeLeft = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge);

        questionText = findViewById(R.id.questionText);
        feedbackText = findViewById(R.id.feedbackText);
        answerBtn1 = findViewById(R.id.answerBtn1);
        answerBtn2 = findViewById(R.id.answerBtn2);
        answerBtn3 = findViewById(R.id.answerBtn3);
        answerBtn4 = findViewById(R.id.answerBtn4);
        nextBtn = findViewById(R.id.nextBtn);
        timerText = findViewById(R.id.timerText);

        questions = getDailyQuestions();
        showQuestion();

        View.OnClickListener answerClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer((Button) v);
            }
        };
        answerBtn1.setOnClickListener(answerClick);
        answerBtn2.setOnClickListener(answerClick);
        answerBtn3.setOnClickListener(answerClick);
        answerBtn4.setOnClickListener(answerClick);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestion++;
                if (currentQuestion < questions.size()) {
                    showQuestion();
                } else {
                    updateStreakIfNeeded();
                    finish();
                }
            }
        });
    }

    List<Question> getDailyQuestions() {
        // Use a fixed pool of questions, shuffle with seed based on date
        ArrayList<Question> pool = new ArrayList<>();
        pool.add(new Question("5 + 3 = ?", new String[]{"8", "7", "6", "9"}, 0));
        pool.add(new Question("12 - 4 = ?", new String[]{"8", "7", "6", "9"}, 0));
        pool.add(new Question("6 x 2 = ?", new String[]{"12", "10", "14", "8"}, 0));
        pool.add(new Question("15 / 3 = ?", new String[]{"5", "4", "6", "3"}, 0));
        pool.add(new Question("9 + 7 = ?", new String[]{"16", "15", "17", "14"}, 0));
        pool.add(new Question("20 - 9 = ?", new String[]{"11", "10", "12", "9"}, 0));
        pool.add(new Question("3 x 4 = ?", new String[]{"12", "9", "7", "8"}, 0));
        pool.add(new Question("18 / 2 = ?", new String[]{"9", "8", "10", "7"}, 0));
        pool.add(new Question("7 + 6 = ?", new String[]{"13", "12", "14", "15"}, 0));
        pool.add(new Question("14 - 5 = ?", new String[]{"9", "8", "10", "7"}, 0));
        // Shuffle with seed based on date
        String today = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Calendar.getInstance().getTime());
        long seed = Long.parseLong(today);
        Collections.shuffle(pool, new Random(seed));
        return pool.subList(0, 3); // 3 daily questions
    }

    void showQuestion() {
        tries = 0;
        feedbackText.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        Question q = questions.get(currentQuestion);
        questionText.setText(q.text);
        answerBtn1.setText(q.answers[0]);
        answerBtn2.setText(q.answers[1]);
        answerBtn3.setText(q.answers[2]);
        answerBtn4.setText(q.answers[3]);
        answerBtn1.setEnabled(true);
        answerBtn2.setEnabled(true);
        answerBtn3.setEnabled(true);
        answerBtn4.setEnabled(true);
        startTimer();
    }

    void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        timeLeft = 20000;
        timerText.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                timerText.setText("Time left: " + (timeLeft / 1000) + "s");
            }
            public void onFinish() {
                timerText.setText("Time's up!");
                answerBtn1.setEnabled(false);
                answerBtn2.setEnabled(false);
                answerBtn3.setEnabled(false);
                answerBtn4.setEnabled(false);
                feedbackText.setText("Time's up! 0 points");
                feedbackText.setTextColor(0xFFB00020);
                feedbackText.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    void checkAnswer(Button btn) {
        if (countDownTimer != null) countDownTimer.cancel();
        Question q = questions.get(currentQuestion);
        int answerIndex = -1;
        if (btn == answerBtn1) answerIndex = 0;
        if (btn == answerBtn2) answerIndex = 1;
        if (btn == answerBtn3) answerIndex = 2;
        if (btn == answerBtn4) answerIndex = 3;
        if (answerIndex == q.correct) {
            int points = 0;
            if (tries == 0) points = 400;
            else if (tries == 1) points = 200;
            else if (tries == 2) points = 100;
            else if (tries == 3) points = 50;
            score += points;
            feedbackText.setText("Correct! +" + points + " points");
            feedbackText.setTextColor(0xFF388E3C);
            feedbackText.setVisibility(View.VISIBLE);
            answerBtn1.setEnabled(false);
            answerBtn2.setEnabled(false);
            answerBtn3.setEnabled(false);
            answerBtn4.setEnabled(false);
            nextBtn.setVisibility(View.VISIBLE);
        } else {
            tries++;
            if (tries >= 4) {
                feedbackText.setText("No more tries! 0 points");
                feedbackText.setTextColor(0xFFB00020);
                feedbackText.setVisibility(View.VISIBLE);
                answerBtn1.setEnabled(false);
                answerBtn2.setEnabled(false);
                answerBtn3.setEnabled(false);
                answerBtn4.setEnabled(false);
                nextBtn.setVisibility(View.VISIBLE);
            } else {
                feedbackText.setText("Wrong! Try again.");
                feedbackText.setTextColor(0xFFB00020);
                feedbackText.setVisibility(View.VISIBLE);
                btn.setEnabled(false);
            }
        }
    }

    private void updateStreakIfNeeded() {
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String lastDateKey = "last_date";
        String streakKey = "streak";
        String today = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Calendar.getInstance().getTime());
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
        }
        // Always go back to HomeActivity, and pass updateStreak flag
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("updateStreak", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private boolean isYesterday(String lastDate, String today) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());
            java.util.Calendar calLast = java.util.Calendar.getInstance();
            java.util.Calendar calToday = java.util.Calendar.getInstance();
            calLast.setTime(sdf.parse(lastDate));
            calToday.setTime(sdf.parse(today));
            calLast.add(java.util.Calendar.DAY_OF_YEAR, 1);
            return calLast.get(java.util.Calendar.YEAR) == calToday.get(java.util.Calendar.YEAR) &&
                    calLast.get(java.util.Calendar.DAY_OF_YEAR) == calToday.get(java.util.Calendar.DAY_OF_YEAR);
        } catch (Exception e) {
            return false;
        }
    }

    // Simple question class (reuse from PracticeActivity)
    static class Question {
        String text;
        String[] answers;
        int correct;
        Question(String text, String[] answers, int correct) {
            this.text = text;
            this.answers = answers;
            this.correct = correct;
        }
    }
} 