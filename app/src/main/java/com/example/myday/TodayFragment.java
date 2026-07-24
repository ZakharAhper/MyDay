package com.example.myday;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodayFragment extends Fragment {

    private TextView textSelectedScore;
    private EditText editComment;
    private Button btnSave;

    private Button btnHappy;
    private Button btnSmile;
    private Button btnNeutral;
    private Button btnSad;
    private Button btnAngry;

    private int selectedScore = 5;
    private String selectedMood = "🙂";

    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        textSelectedScore = view.findViewById(R.id.textSelectedScore);
        editComment = view.findViewById(R.id.editComment);
        btnSave = view.findViewById(R.id.btnSave);

        btnHappy = view.findViewById(R.id.btnHappy);
        btnSmile = view.findViewById(R.id.btnSmile);
        btnNeutral = view.findViewById(R.id.btnNeutral);
        btnSad = view.findViewById(R.id.btnSad);
        btnAngry = view.findViewById(R.id.btnAngry);

        SeekBar seekScore = view.findViewById(R.id.seekScore);

        db = new DatabaseHelper(getContext());

        setupMoodButtons();
        setupScoreSeekBar(seekScore);
        updateScoreColor();
        selectMood("🙂", btnSmile);

        btnSave.setOnClickListener(v -> saveData());

        return view;
    }

    private void setupScoreSeekBar(SeekBar seekScore) {
        seekScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedScore = progress + 1;
                textSelectedScore.setText("Оценка: " + selectedScore);
                updateScoreColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setupMoodButtons() {
        btnHappy.setOnClickListener(v -> selectMood("😄", btnHappy));
        btnSmile.setOnClickListener(v -> selectMood("🙂", btnSmile));
        btnNeutral.setOnClickListener(v -> selectMood("😐", btnNeutral));
        btnSad.setOnClickListener(v -> selectMood("😔", btnSad));
        btnAngry.setOnClickListener(v -> selectMood("😡", btnAngry));
    }

    private void selectMood(String mood, Button selectedButton) {
        selectedMood = mood;

        resetMoodButtons();

        selectedButton.setAlpha(0.65f);
    }

    private void resetMoodButtons() {
        btnHappy.setAlpha(1f);
        btnSmile.setAlpha(1f);
        btnNeutral.setAlpha(1f);
        btnSad.setAlpha(1f);
        btnAngry.setAlpha(1f);
    }

    private void updateScoreColor() {

        int color;

        if (selectedScore <= 3) {
            color = Color.parseColor("#D96C6C");
        } else if (selectedScore <= 7) {
            color = Color.parseColor("#E6B85C");
        } else {
            color = Color.parseColor("#6BAF7A");
        }

        String fullText = "Оценка: " + selectedScore;

        SpannableString spannable = new SpannableString(fullText);

        int start = fullText.indexOf(String.valueOf(selectedScore));

        spannable.setSpan(
                new ForegroundColorSpan(color),
                start,
                fullText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textSelectedScore.setText(spannable);
    }

    private void saveData() {
        String comment = editComment.getText().toString();

        String dayOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        if (db.hasEntryForDate(dayOnly)) {
            Toast.makeText(getContext(), "За сегодня запись уже есть", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date());

        boolean success = db.addEntry(selectedScore, selectedMood, comment, date);

        if (success) {
            Toast.makeText(getContext(), "Сохранено", Toast.LENGTH_SHORT).show();

            selectedScore = 5;
            selectedMood = "🙂";
            textSelectedScore.setText("Оценка: 5");
            editComment.setText("");
            updateScoreColor();
            selectMood("🙂", btnSmile);
        } else {
            Toast.makeText(getContext(), "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }
}