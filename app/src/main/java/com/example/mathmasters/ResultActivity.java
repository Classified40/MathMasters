package com.example.mathmasters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvScore = findViewById(R.id.tv_score);
        TextView tvPercent = findViewById(R.id.tv_percent);
        TextView tvStatus = findViewById(R.id.tv_status);
        Button btnBack = findViewById(R.id.btn_back);

        int score = getIntent().getIntExtra("score", 0);
        int maxScore = getIntent().getIntExtra("maxScore", 1);
        int percent = getIntent().getIntExtra("percent", 0);
        int questionsCount = getIntent().getIntExtra("questionsCount", 0);
        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int levelNumber = getIntent().getIntExtra("levelNumber", 1);
        int subLevelNumber = getIntent().getIntExtra("subLevelNumber", 1);

        tvScore.setText("Score: " + score + " / " + maxScore);
        tvPercent.setText("Percent: " + percent + "%");

        SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (percent >= 70) {
            tvStatus.setText("Passed! Next sub-level unlocked.");
            editor.putInt("level" + levelNumber + "_sub" + subLevelNumber + "_done", questionsCount);
            editor.putBoolean("level" + levelNumber + "_sub" + (subLevelNumber + 1) + "_unlocked", true);
            editor.putInt("level" + levelNumber + "_sub" + subLevelNumber + "_total", questionsCount);
            editor.putBoolean("level" + levelNumber + "_sub" + subLevelNumber + "_passed", true);
        } else {
            tvStatus.setText("Not passed. Try again to unlock next sub-level.");
            editor.putInt("level" + levelNumber + "_sub" + subLevelNumber + "_done", correctAnswers);
            editor.putBoolean("level" + levelNumber + "_sub" + subLevelNumber + "_passed", false);
        }
        editor.apply();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, SubLevelActivity.class);
                i.putExtra("levelNumber", levelNumber);
                startActivity(i);
                finish();
            }
        });
    }
} 