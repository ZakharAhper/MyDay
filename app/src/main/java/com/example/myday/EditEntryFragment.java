package com.example.myday;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditEntryFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_SCORE = "score";
    private static final String ARG_MOOD = "mood";
    private static final String ARG_COMMENT = "comment";

    private TextView textEditScore;
    private EditText editEditComment;

    private int entryId;
    private int selectedScore;
    private String selectedMood;

    private DatabaseHelper db;

    public static EditEntryFragment newInstance(DayEntry entry) {
        EditEntryFragment fragment = new EditEntryFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ID, entry.getId());
        args.putInt(ARG_SCORE, entry.getScore());
        args.putString(ARG_MOOD, entry.getMood());
        args.putString(ARG_COMMENT, entry.getComment());

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_edit_entry, container, false);

        textEditScore = view.findViewById(R.id.textEditScore);
        editEditComment = view.findViewById(R.id.editEditComment);
        SeekBar seekEditScore = view.findViewById(R.id.seekEditScore);
        Button btnUpdateEntry = view.findViewById(R.id.btnUpdateEntry);

        db = new DatabaseHelper(getContext());

        loadArguments();
        setupScore(seekEditScore);
        setupMoodButtons(view);

        btnUpdateEntry.setOnClickListener(v -> updateEntry());

        return view;
    }

    private void loadArguments() {
        Bundle args = getArguments();

        if (args == null) {
            return;
        }

        entryId = args.getInt(ARG_ID);
        selectedScore = args.getInt(ARG_SCORE);
        selectedMood = args.getString(ARG_MOOD, "🙂");

        textEditScore.setText("Оценка: " + selectedScore);
        editEditComment.setText(args.getString(ARG_COMMENT, ""));
        updateScoreColor();
    }

    private void setupScore(SeekBar seekEditScore) {
        seekEditScore.setProgress(selectedScore - 1);

        seekEditScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedScore = progress + 1;
                textEditScore.setText("Оценка: " + selectedScore);
                updateScoreColor();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupMoodButtons(View view) {
        view.findViewById(R.id.btnEditHappy).setOnClickListener(v -> selectedMood = "😄");
        view.findViewById(R.id.btnEditSmile).setOnClickListener(v -> selectedMood = "🙂");
        view.findViewById(R.id.btnEditNeutral).setOnClickListener(v -> selectedMood = "😐");
        view.findViewById(R.id.btnEditSad).setOnClickListener(v -> selectedMood = "😔");
        view.findViewById(R.id.btnEditAngry).setOnClickListener(v -> selectedMood = "😡");
    }

    private void updateScoreColor() {
        if (selectedScore <= 3) {
            textEditScore.setTextColor(Color.parseColor("#D32F2F"));
        } else if (selectedScore <= 7) {
            textEditScore.setTextColor(Color.parseColor("#F9A825"));
        } else {
            textEditScore.setTextColor(Color.parseColor("#388E3C"));
        }
    }

    private void updateEntry() {
        String comment = editEditComment.getText().toString();

        boolean success = db.updateEntry(entryId, selectedScore, selectedMood, comment);

        if (success) {
            Toast.makeText(getContext(), "Запись обновлена", Toast.LENGTH_SHORT).show();

            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        } else {
            Toast.makeText(getContext(), "Ошибка обновления", Toast.LENGTH_SHORT).show();
        }
    }
}