package com.example.mathmasters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<LevelItem> list;
    LevelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_levels);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = findViewById(R.id.rv_levels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        // add levels
        list.add(new LevelItem(1, "Addition", R.drawable.ic_launcher_foreground, true, 0, 4, "Practice single digit addition"));
        list.add(new LevelItem(2, "Subtraction", R.drawable.ic_launcher_foreground, false, 0, 2, "Practice single digit subtraction"));
        list.add(new LevelItem(3, "Multiplication", R.drawable.ic_launcher_foreground, false, 0, 2, "Practice multiplication"));
        list.add(new LevelItem(4, "Division", R.drawable.ic_launcher_foreground, false, 0, 2, "Practice division"));
        list.add(new LevelItem(5, "Add & Subtract Negatives", R.drawable.ic_launcher_foreground, false, 0, 2, "Practice with negative numbers"));
        list.add(new LevelItem(6, "Multiply & Divide Negatives", R.drawable.ic_launcher_foreground, false, 0, 2, "Multiply and divide negative numbers"));
        list.add(new LevelItem(7, "Simple Equations", R.drawable.ic_launcher_foreground, false, 0, 2, "Solve simple equations"));
        // Read progress for each level
        android.content.SharedPreferences prefs = getSharedPreferences("progress", MODE_PRIVATE);
        for (int i = 0; i < list.size(); i++) {
            LevelItem level = list.get(i);
            int done = 0;
            for (int sub = 1; sub <= level.subLevelsTotal; sub++) {
                String passedKey = "level" + level.number + "_sub" + sub + "_passed";
                boolean passed = prefs.getBoolean(passedKey, false);
                if (passed) done++;
            }
            level.subLevelsDone = done;
            if (level.number == 1) {
                level.unlocked = true;
            } else {
                LevelItem prev = list.get(i - 1);
                level.unlocked = (prev.subLevelsDone == prev.subLevelsTotal);
            }
        }
        adapter = new LevelAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}

class LevelItem {
    int number;
    String name;
    int iconResId;
    boolean unlocked;
    int subLevelsDone;
    int subLevelsTotal;
    String description;
    LevelItem(int number, String name, int iconResId, boolean unlocked, int subLevelsDone, int subLevelsTotal, String description) {
        this.number = number;
        this.name = name;
        this.iconResId = iconResId;
        this.unlocked = unlocked;
        this.subLevelsDone = subLevelsDone;
        this.subLevelsTotal = subLevelsTotal;
        this.description = description;
    }
}

class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.MyViewHolder> {
    ArrayList<LevelItem> list;
    LevelActivity activity;
    LevelAdapter(ArrayList<LevelItem> list, LevelActivity activity) {
        this.list = list;
        this.activity = activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LevelItem item = list.get(position);
        holder.title.setText("Level " + item.number + ": " + item.name);
        holder.desc.setText(item.description);
        holder.icon.setImageResource(item.iconResId);
        int percent = 0;
        if (item.subLevelsTotal != 0) {
            percent = (int) ((item.subLevelsDone * 100.0f) / item.subLevelsTotal);
        }
        holder.progressBar.setProgress(percent);
        holder.progressPercent.setText(percent + "%");
        if (item.unlocked) {
            holder.lock.setVisibility(View.GONE);
            holder.btn.setEnabled(true);
        } else {
            holder.lock.setVisibility(View.VISIBLE);
            holder.btn.setEnabled(false);
        }
        // הצגת וי אם כל תתי־השלבים הושלמו
        if (item.subLevelsDone == item.subLevelsTotal && item.subLevelsTotal > 0) {
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.GONE);
        }
        holder.btn.setOnClickListener(v -> {
            if (item.unlocked) {
                Intent i = new Intent(activity, SubLevelActivity.class);
                i.putExtra("levelNumber", item.number);
                activity.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon, lock, check;
        TextView title, desc, progressPercent;
        ProgressBar progressBar;
        Button btn;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_level_icon);
            lock = itemView.findViewById(R.id.iv_lock);
            check = itemView.findViewById(R.id.iv_level_check);
            title = itemView.findViewById(R.id.tv_level_title);
            desc = itemView.findViewById(R.id.tv_level_desc);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressPercent = itemView.findViewById(R.id.tv_progress_percent);
            btn = itemView.findViewById(R.id.btn_to_sublevels);
        }
    }
} 