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

public class PracticeActivity extends AppCompatActivity {
    TextView questionText, feedbackText, timerText;
    Button answerBtn1, answerBtn2, answerBtn3, answerBtn4, nextBtn;
    int currentQuestion = 0;
    int score = 0;
    int tries = 0;
    List<Question> questions;
    CountDownTimer countDownTimer;
    long timeLeft = 20000; // 20 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        questionText = findViewById(R.id.questionText);
        feedbackText = findViewById(R.id.feedbackText);
        answerBtn1 = findViewById(R.id.answerBtn1);
        answerBtn2 = findViewById(R.id.answerBtn2);
        answerBtn3 = findViewById(R.id.answerBtn3);
        answerBtn4 = findViewById(R.id.answerBtn4);
        nextBtn = findViewById(R.id.nextBtn);
        timerText = findViewById(R.id.timerText);

        int levelNumber = getIntent().getIntExtra("levelNumber", 1);
        int subLevelNumber = getIntent().getIntExtra("subLevelNumber", 1);
        questions = new ArrayList<>();

        if (levelNumber == 1) { // Addition
            if (subLevelNumber == 1) { // Single digit addition
                questions.add(new Question("2 + 3 = ?", new String[]{"5", "4", "6", "7"}, 0));
                questions.add(new Question("1 + 6 = ?", new String[]{"7", "8", "6", "5"}, 0));
                questions.add(new Question("4 + 2 = ?", new String[]{"6", "7", "5", "8"}, 0));
                questions.add(new Question("7 + 2 = ?", new String[]{"9", "8", "10", "7"}, 0));
                questions.add(new Question("1 + 8 = ?", new String[]{"9", "8", "7", "10"}, 0));
            } else if (subLevelNumber == 2) { // Double digit addition
                questions.add(new Question("12 + 15 = ?", new String[]{"27", "25", "26", "28"}, 0));
                questions.add(new Question("23 + 14 = ?", new String[]{"37", "36", "38", "35"}, 0));
                questions.add(new Question("34 + 22 = ?", new String[]{"56", "54", "57", "55"}, 0));
                questions.add(new Question("45 + 13 = ?", new String[]{"58", "57", "59", "56"}, 0));
                questions.add(new Question("27 + 19 = ?", new String[]{"46", "45", "47", "48"}, 0));
            } else if (subLevelNumber == 3) { // Addition with carry
                questions.add(new Question("19 + 7 = ?", new String[]{"26", "25", "27", "24"}, 0));
                questions.add(new Question("28 + 9 = ?", new String[]{"37", "36", "38", "39"}, 0));
                questions.add(new Question("36 + 8 = ?", new String[]{"44", "43", "45", "46"}, 0));
                questions.add(new Question("47 + 6 = ?", new String[]{"53", "52", "54", "51"}, 0));
                questions.add(new Question("58 + 7 = ?", new String[]{"65", "64", "66", "63"}, 0));
            } else if (subLevelNumber == 4) { // Word problems
                questions.add(new Question("Tom has 3 apples. He buys 2 more. How many apples does he have now?", new String[]{"5", "4", "6", "3"}, 0));
                questions.add(new Question("Sara found 4 coins and then found 3 more. How many coins does she have?", new String[]{"7", "6", "8", "5"}, 0));
                questions.add(new Question("Ben read 5 pages on Monday and 6 on Tuesday. How many pages in total?", new String[]{"11", "10", "12", "9"}, 0));
                questions.add(new Question("Anna has 2 candies and gets 5 more. How many now?", new String[]{"7", "6", "8", "5"}, 0));
                questions.add(new Question("Mike picked 8 flowers and his friend gave him 4 more. How many flowers?", new String[]{"12", "10", "14", "11"}, 0));
            }
        } else if (levelNumber == 2) { // Subtraction
            if (subLevelNumber == 1) { // Single digit subtraction
                questions.add(new Question("7 - 2 = ?", new String[]{"5", "4", "6", "3"}, 0));
                questions.add(new Question("9 - 4 = ?", new String[]{"5", "6", "4", "3"}, 0));
                questions.add(new Question("5 - 2 = ?", new String[]{"3", "2", "4", "1"}, 0));
            } else if (subLevelNumber == 2) { // Double digit subtraction
                questions.add(new Question("23 - 11 = ?", new String[]{"12", "13", "14", "11"}, 0));
                questions.add(new Question("35 - 18 = ?", new String[]{"17", "16", "18", "19"}, 0));
                questions.add(new Question("56 - 19 = ?", new String[]{"37", "36", "38", "39"}, 0));
                questions.add(new Question("67 - 28 = ?", new String[]{"39", "38", "40", "41"}, 0));
            }
        } else if (levelNumber == 3) { // Multiplication
            if (subLevelNumber == 1) { // Single digit multiplication
                questions.add(new Question("3 x 4 = ?", new String[]{"12", "9", "7", "11"}, 0));
                questions.add(new Question("2 x 6 = ?", new String[]{"12", "8", "10", "14"}, 0));
                questions.add(new Question("5 x 3 = ?", new String[]{"15", "12", "18", "10"}, 0));
                questions.add(new Question("7 x 2 = ?", new String[]{"14", "12", "16", "10"}, 0));
                questions.add(new Question("4 x 5 = ?", new String[]{"20", "15", "25", "10"}, 0));
            } else if (subLevelNumber == 2) { // Multiplication by 10
                questions.add(new Question("7 x 10 = ?", new String[]{"70", "17", "77", "60"}, 0));
                questions.add(new Question("3 x 10 = ?", new String[]{"30", "13", "33", "20"}, 0));
                questions.add(new Question("8 x 10 = ?", new String[]{"80", "18", "88", "70"}, 0));
                questions.add(new Question("6 x 10 = ?", new String[]{"60", "16", "66", "50"}, 0));
            }
        } else if (levelNumber == 4) { // Division
            if (subLevelNumber == 1) { // Simple division
                questions.add(new Question("8 / 2 = ?", new String[]{"4", "2", "6", "8"}, 0));
                questions.add(new Question("12 / 3 = ?", new String[]{"4", "3", "6", "5"}, 0));
                questions.add(new Question("16 / 4 = ?", new String[]{"4", "2", "6", "8"}, 0));
                questions.add(new Question("18 / 6 = ?", new String[]{"3", "2", "6", "4"}, 0));
            } else if (subLevelNumber == 2) { // Division with remainder
                questions.add(new Question("10 / 3 = ?", new String[]{"3", "4", "2", "1"}, 0));
                questions.add(new Question("17 / 5 = ?", new String[]{"3", "2", "4", "5"}, 0));
                questions.add(new Question("13 / 4 = ?", new String[]{"3", "2", "4", "5"}, 0));
                questions.add(new Question("15 / 4 = ?", new String[]{"3", "2", "4", "5"}, 0));
            }
        } else if (levelNumber == 5) { // Add & Subtract Negatives
            if (subLevelNumber == 1) {
                questions.add(new Question("-3 + 5 = ?", new String[]{"2", "-2", "8", "-8"}, 0));
                questions.add(new Question("6 - (-3) = ?", new String[]{"9", "3", "-9", "-3"}, 0));
                questions.add(new Question("-2 + 4 = ?", new String[]{"2", "-2", "6", "-6"}, 0));
            } else if (subLevelNumber == 2) {
                questions.add(new Question("-7 + (-2) = ?", new String[]{"-9", "9", "-5", "5"}, 0));
                questions.add(new Question("-4 - 3 = ?", new String[]{"-7", "7", "-1", "1"}, 0));
                questions.add(new Question("-6 + (-3) = ?", new String[]{"-9", "9", "-3", "3"}, 0));
                questions.add(new Question("-8 - 2 = ?", new String[]{"-10", "10", "-6", "6"}, 0));
                questions.add(new Question("-5 + 1 = ?", new String[]{"-4", "4", "-6", "6"}, 0));
            }
        } else if (levelNumber == 6) { // Multiply & Divide Negatives
            if (subLevelNumber == 1) {
                questions.add(new Question("-3 x 4 = ?", new String[]{"-12", "12", "-7", "7"}, 0));
                questions.add(new Question("5 x -2 = ?", new String[]{"-10", "10", "-7", "7"}, 0));
                questions.add(new Question("6 x -1 = ?", new String[]{"-6", "6", "-1", "1"}, 0));
            } else if (subLevelNumber == 2) {
                questions.add(new Question("-12 / 3 = ?", new String[]{"-4", "4", "-3", "3"}, 0));
                questions.add(new Question("16 / -4 = ?", new String[]{"-4", "4", "-12", "12"}, 0));
                questions.add(new Question("20 / -5 = ?", new String[]{"-4", "4", "-5", "5"}, 0));
                questions.add(new Question("-15 / 3 = ?", new String[]{"-5", "5", "-3", "3"}, 0));
            }
        } else if (levelNumber == 7) { // Simple Equations
            if (subLevelNumber == 1) {
                questions.add(new Question("x + 3 = 7, x = ?", new String[]{"4", "3", "5", "7"}, 0));
                questions.add(new Question("2x = 8, x = ?", new String[]{"4", "2", "8", "6"}, 0));
                questions.add(new Question("x - 2 = 5, x = ?", new String[]{"7", "5", "3", "2"}, 0));
                questions.add(new Question("3x = 9, x = ?", new String[]{"3", "6", "9", "2"}, 0));
            } else if (subLevelNumber == 2) {
                questions.add(new Question("x - 5 = 2, x = ?", new String[]{"7", "5", "3", "2"}, 0));
                questions.add(new Question("x / 2 = 6, x = ?", new String[]{"12", "6", "8", "10"}, 0));
                questions.add(new Question("2x = 10, x = ?", new String[]{"5", "10", "2", "8"}, 0));
                questions.add(new Question("x / 3 = 5, x = ?", new String[]{"15", "5", "3", "8"}, 0));
            }
        }
        Collections.shuffle(questions);

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
                    int levelNumber = getIntent().getIntExtra("levelNumber", 1);
                    int subLevelNumber = getIntent().getIntExtra("subLevelNumber", 1);
                    int maxScore = questions.size() * 400;
                    int percent = (int) ((score * 100.0f) / maxScore);
                    int correctAnswers = score / 400;
                    android.content.Intent intent = new android.content.Intent(PracticeActivity.this, ResultActivity.class);
                    intent.putExtra("score", score);
                    intent.putExtra("maxScore", maxScore);
                    intent.putExtra("percent", percent);
                    intent.putExtra("questionsCount", questions.size());
                    intent.putExtra("correctAnswers", correctAnswers);
                    intent.putExtra("levelNumber", levelNumber);
                    intent.putExtra("subLevelNumber", subLevelNumber);
                    startActivity(intent);
                    finish();
                }
            }
        });
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

    // simple question class
    class Question {
        String text;
        String[] answers;
        int correct;
        Question(String t, String[] a, int c) {
            text = t;
            // ערבוב תשובות ועדכון אינדקס נכונה
            java.util.List<String> ansList = new java.util.ArrayList<>();
            Collections.addAll(ansList, a);
            String correctAnswer = a[c];
            Collections.shuffle(ansList);
            answers = ansList.toArray(new String[0]);
            for (int i = 0; i < answers.length; i++) {
                if (answers[i].equals(correctAnswer)) {
                    correct = i;
                    break;
                }
            }
        }
    }
} 