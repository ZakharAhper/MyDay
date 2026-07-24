package com.example.myday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class StatsFragment extends Fragment {

    private TextView textCount;
    private TextView textAverage;
    private TextView textMin;
    private TextView textMax;

    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        textCount = view.findViewById(R.id.textCount);
        textAverage = view.findViewById(R.id.textAverage);
        textMin = view.findViewById(R.id.textMin);
        textMax = view.findViewById(R.id.textMax);

        db = new DatabaseHelper(getContext());

        loadStats();
        addGraph(view);

        return view;
    }

    private void loadStats() {
        int count = db.getEntriesCount();
        double average = db.getAverageScore();
        int min = db.getMinScore();
        int max = db.getMaxScore();

        textCount.setText("Количество записей: " + count);
        textAverage.setText("Средняя оценка: " + String.format("%.1f", average));
        textMin.setText("Минимальная оценка: " + min);
        textMax.setText("Максимальная оценка: " + max);
    }

    private void addGraph(View view) {
        LinearLayout statsContainer = view.findViewById(R.id.statsContainer);
        ArrayList<DayEntry> entries = db.getAllEntries();

        ScoreGraphView graphView = new ScoreGraphView(getContext(), entries);
        graphView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        ));

        statsContainer.addView(graphView);
    }
}