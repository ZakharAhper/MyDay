package com.example.myday;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private ArrayList<DayEntry> entries;

    public EntryAdapter(ArrayList<DayEntry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entry, parent, false);

        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        DayEntry entry = entries.get(position);
        int score = entry.getScore();

        holder.textScore.setText("Оценка: " + score);
        holder.textMood.setText("Настроение: " + entry.getMood());
        holder.textComment.setText("Комментарий: " + entry.getComment());
        holder.textDate.setText("Дата: " + entry.getDate());

        int scoreColor = getScoreColor(score);

        holder.textScore.setTextColor(scoreColor);
        holder.colorStrip.setBackgroundColor(scoreColor);

        holder.itemView.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) v.getContext();

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.screenContainer, EditEntryFragment.newInstance(entry))
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    private int getScoreColor(int score) {
        if (score <= 3) {
            return Color.parseColor("#D32F2F");
        }

        if (score <= 7) {
            return Color.parseColor("#F9A825");
        }

        return Color.parseColor("#388E3C");
    }

    static class EntryViewHolder extends RecyclerView.ViewHolder {

        TextView textScore;
        TextView textMood;
        TextView textComment;
        TextView textDate;
        View colorStrip;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);

            textScore = itemView.findViewById(R.id.textScore);
            textMood = itemView.findViewById(R.id.textMood);
            textComment = itemView.findViewById(R.id.textComment);
            textDate = itemView.findViewById(R.id.textDate);
            colorStrip = itemView.findViewById(R.id.colorStrip);
        }
    }
}