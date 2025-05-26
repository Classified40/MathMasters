package com.example.mathmasters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SubLevelActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<SubLevelItem> list;
    SubLevelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sublevels);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        int levelNumber = getIntent().getIntExtra("levelNumber", 1);
        recyclerView = findViewById(R.id.rv_sublevels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        if (levelNumber == 1) {
            list.add(new SubLevelItem(1, "Single digit addition", true, 0, 10, "Practice adding single digit numbers", "Add the numbers together. Example: 3 + 5 = 8"));
            list.add(new SubLevelItem(2, "Double digit addition", false, 0, 10, "Practice adding double digit numbers", "Line up the numbers by place value and add. Example: 12 + 15 = 27"));
            list.add(new SubLevelItem(3, "Addition with carry", false, 0, 10, "Practice addition with carry", "Add the digits, carry over if needed. Example: 27 + 18 = 45"));
            list.add(new SubLevelItem(4, "Word problems", false, 0, 10, "Addition word problems", "Read the problem, find the numbers to add, and solve. Example: If you have 4 apples and get 3 more, how many? 4 + 3 = 7"));
        } else if (levelNumber == 2) {
            list.add(new SubLevelItem(1, "Single digit subtraction", true, 0, 10, "Practice subtracting single digit numbers", "Subtract the numbers. Example: 7 - 2 = 5"));
            list.add(new SubLevelItem(2, "Double digit subtraction", false, 0, 10, "Practice subtracting double digit numbers", "Line up the numbers and subtract. Example: 23 - 11 = 12"));
        } else if (levelNumber == 3) {
            list.add(new SubLevelItem(1, "Single digit multiplication", true, 0, 10, "Practice multiplying single digit numbers", "Multiply the numbers. Example: 3 x 4 = 12"));
            list.add(new SubLevelItem(2, "Multiplication by 10", false, 0, 10, "Practice multiplying by 10", "Multiply the number by 10. Example: 7 x 10 = 70"));
        } else if (levelNumber == 4) {
            list.add(new SubLevelItem(1, "Simple division", true, 0, 10, "Practice simple division", "Divide the numbers. Example: 8 / 2 = 4"));
            list.add(new SubLevelItem(2, "Division with remainder", false, 0, 10, "Practice division with remainder", "Divide and find the remainder. Example: 10 / 3 = 3 remainder 1"));
        } else if (levelNumber == 5) {
            list.add(new SubLevelItem(1, "Add & Subtract Negatives 1", true, 0, 10, "Practice with negative numbers", "Add or subtract negative numbers. Example: -3 + 5 = 2"));
            list.add(new SubLevelItem(2, "Add & Subtract Negatives 2", false, 0, 10, "Practice with negative numbers", "Add or subtract negative numbers. Example: -7 + (-2) = -9"));
        } else if (levelNumber == 6) {
            list.add(new SubLevelItem(1, "Multiply & Divide Negatives 1", true, 0, 10, "Multiply and divide negative numbers", "Multiply or divide negative numbers. Example: -3 x 4 = -12"));
            list.add(new SubLevelItem(2, "Multiply & Divide Negatives 2", false, 0, 10, "Multiply and divide negative numbers", "Multiply or divide negative numbers. Example: -12 / 3 = -4"));
        } else if (levelNumber == 7) {
            list.add(new SubLevelItem(1, "Simple equations 1", true, 0, 10, "Solve simple equations", "Solve for x. Example: x + 3 = 7, x = 4"));
            list.add(new SubLevelItem(2, "Simple equations 2", false, 0, 10, "Solve simple equations", "Solve for x. Example: x - 5 = 2, x = 7"));
        }
        // Read progress for each sub-level
        android.content.SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);
        for (SubLevelItem item : list) {
            String key = "level" + levelNumber + "_sub" + item.number + "_done";
            item.questionsDone = prefs.getInt(key, 0);
            String totalKey = "level" + levelNumber + "_sub" + item.number + "_total";
            item.questionsTotal = prefs.getInt(totalKey, item.questionsTotal);
            if (item.number == 1) {
                item.unlocked = true;
            } else {
                String unlockKey = "level" + levelNumber + "_sub" + item.number + "_unlocked";
                item.unlocked = prefs.getBoolean(unlockKey, false);
            }
        }
        adapter = new SubLevelAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(this, LevelActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LevelActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}

class SubLevelItem {
    int number;
    String name;
    boolean unlocked;
    int questionsDone;
    int questionsTotal;
    String description;
    String howToSolve;
    SubLevelItem(int number, String name, boolean unlocked, int questionsDone, int questionsTotal, String description, String howToSolve) {
        this.number = number;
        this.name = name;
        this.unlocked = unlocked;
        this.questionsDone = questionsDone;
        this.questionsTotal = questionsTotal;
        this.description = description;
        this.howToSolve = howToSolve;
    }
}

class SubLevelAdapter extends RecyclerView.Adapter<SubLevelAdapter.MyViewHolder> {
    ArrayList<SubLevelItem> list;
    SubLevelActivity activity;
    SubLevelAdapter(ArrayList<SubLevelItem> list, SubLevelActivity activity) {
        this.list = list;
        this.activity = activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sublevel, parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubLevelItem item = list.get(position);
        holder.title.setText("Sub-level " + item.number + ": " + item.name);
        holder.desc.setText(item.description);
        if (item.unlocked) {
            holder.lock.setVisibility(View.GONE);
            holder.btn.setEnabled(true);
        } else {
            holder.lock.setVisibility(View.VISIBLE);
            holder.btn.setEnabled(false);
        }
        if (item.questionsDone == item.questionsTotal && item.questionsTotal > 0) {
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.GONE);
        }
        holder.btn.setOnClickListener(v -> {
            if (item.unlocked) {
                Intent i = new Intent(activity, PracticeActivity.class);
                i.putExtra("subLevelNumber", item.number);
                i.putExtra("levelNumber", activity.getIntent().getIntExtra("levelNumber", 1));
                activity.startActivity(i);
            }
        });
        holder.info.setOnClickListener(v -> {
            String msg = item.description + "\n\nHow to solve: " + item.howToSolve;
            new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(item.name)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        ImageView lock, info, check;
        Button btn;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_sublevel_title);
            desc = itemView.findViewById(R.id.tv_sublevel_desc);
            lock = itemView.findViewById(R.id.iv_sublevel_lock);
            btn = itemView.findViewById(R.id.btn_to_practice);
            info = itemView.findViewById(R.id.iv_info);
            check = itemView.findViewById(R.id.iv_sublevel_check);
        }
    }
} 