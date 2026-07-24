package com.example.myday;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private GridLayout weekDaysGrid;
    private GridLayout calendarGrid;
    private TextView textMonthTitle;
    private TextView textSelectedDate;
    private RecyclerView calendarRecyclerView;

    private DatabaseHelper db;
    private Calendar currentMonth;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        weekDaysGrid = view.findViewById(R.id.weekDaysGrid);
        calendarGrid = view.findViewById(R.id.calendarGrid);
        textMonthTitle = view.findViewById(R.id.textMonthTitle);
        textSelectedDate = view.findViewById(R.id.textSelectedDate);
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);

        Button btnPreviousMonth = view.findViewById(R.id.btnPreviousMonth);
        Button btnNextMonth = view.findViewById(R.id.btnNextMonth);

        db = new DatabaseHelper(getContext());
        currentMonth = Calendar.getInstance();

        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnPreviousMonth.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, -1);
            drawMonth();
        });

        btnNextMonth.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, 1);
            drawMonth();
        });

        drawWeekDays();
        drawMonth();

        return view;
    }

    private void drawWeekDays() {
        String[] days = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};

        weekDaysGrid.removeAllViews();

        for (String day : days) {
            TextView textView = new TextView(getContext());

            textView.setText(day);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(14);
            textView.setTextColor(Color.GRAY);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 60;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

            textView.setLayoutParams(params);
            weekDaysGrid.addView(textView);
        }
    }

    private void drawMonth() {
        int year = currentMonth.get(Calendar.YEAR);
        int month = currentMonth.get(Calendar.MONTH);

        String monthName = currentMonth.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                new Locale("ru")
        );

        textMonthTitle.setText(monthName + " " + year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int emptyCellsBeforeFirstDay = firstDayOfWeek - Calendar.MONDAY;

        if (emptyCellsBeforeFirstDay < 0) {
            emptyCellsBeforeFirstDay = 6;
        }

        calendarGrid.removeAllViews();

        for (int i = 0; i < emptyCellsBeforeFirstDay; i++) {
            calendarGrid.addView(createEmptyDayView());
        }

        for (int day = 1; day <= daysInMonth; day++) {
            TextView dayView = createDayView(day, year, month);
            calendarGrid.addView(dayView);
        }
    }

    private TextView createDayView(int day, int year, int month) {
        TextView textView = new TextView(getContext());

        int realMonth = month + 1;
        String formattedDate = String.format("%04d-%02d-%02d", year, realMonth, day);
        int score = db.getScoreByDate(formattedDate);

        textView.setText(String.valueOf(day));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(getColorByScore(score));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 90;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 4);

        textView.setLayoutParams(params);
        textView.setOnClickListener(v -> selectDate(year, month, day));

        return textView;
    }

    private int getColorByScore(int score) {
        if (score >= 1 && score <= 3) {
            return Color.parseColor("#FFCDD2");
        }

        if (score >= 4 && score <= 7) {
            return Color.parseColor("#FFF59D");
        }

        if (score >= 8 && score <= 10) {
            return Color.parseColor("#C8E6C9");
        }

        return Color.WHITE;
    }

    private void selectDate(int year, int month, int day) {
        int realMonth = month + 1;

        String formattedDate = String.format("%04d-%02d-%02d", year, realMonth, day);
        textSelectedDate.setText("Выбрана дата: " + formattedDate);

        calendarRecyclerView.setAdapter(
                new EntryAdapter(db.getEntriesByDate(formattedDate))
        );
    }

    private TextView createEmptyDayView() {
        TextView textView = new TextView(getContext());

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 90;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 4);

        textView.setLayoutParams(params);

        return textView;
    }
}