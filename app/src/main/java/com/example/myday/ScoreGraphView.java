package com.example.myday;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreGraphView extends View {

    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final ArrayList<DayEntry> entries;

    public ScoreGraphView(Context context, ArrayList<DayEntry> entries) {
        super(context);

        this.entries = new ArrayList<>(entries);
        Collections.reverse(this.entries);

        linePaint.setColor(0xFF4F8F8B);
        linePaint.setStrokeWidth(6);

        pointPaint.setColor(0xFF3F7572);

        gridPaint.setColor(0xFFD8D2C8);
        gridPaint.setStrokeWidth(2);

        axisPaint.setColor(0xFF7A7A7A);
        axisPaint.setStrokeWidth(3);

        textPaint.setColor(0xFF7A7A7A);
        textPaint.setTextSize(26);
        textPaint.setTextAlign(Paint.Align.RIGHT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = 80;
        float top = 70;
        float right = getWidth() - 30;
        float bottom = getHeight() - 50;

        drawGridAndLabels(canvas, left, top, right, bottom);

        if (entries.size() < 2) {
            return;
        }

        drawGraph(canvas, left, top, right, bottom);
    }

    private void drawGridAndLabels(Canvas canvas, float left, float top, float right, float bottom) {
        for (int score = 1; score <= 10; score++) {
            float y = getYForScore(score, top, bottom);

            canvas.drawLine(left, y, right, y, gridPaint);

            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float textY = y - (metrics.ascent + metrics.descent) / 2;

            canvas.drawText(String.valueOf(score), left - 18, textY, textPaint);
        }

        canvas.drawLine(left, top, left, bottom, axisPaint);
        canvas.drawLine(left, bottom, right, bottom, axisPaint);
    }

    private void drawGraph(Canvas canvas, float left, float top, float right, float bottom) {
        float graphWidth = right - left;

        for (int i = 0; i < entries.size() - 1; i++) {
            float x1 = left + graphWidth * i / (entries.size() - 1);
            float y1 = getYForScore(entries.get(i).getScore(), top, bottom);

            float x2 = left + graphWidth * (i + 1) / (entries.size() - 1);
            float y2 = getYForScore(entries.get(i + 1).getScore(), top, bottom);

            canvas.drawLine(x1, y1, x2, y2, linePaint);
            canvas.drawCircle(x1, y1, 10, pointPaint);
            canvas.drawCircle(x2, y2, 10, pointPaint);
        }
    }

    private float getYForScore(int score, float top, float bottom) {
        float graphHeight = bottom - top;
        return bottom - graphHeight * (score - 1) / 9;
    }
}