package com.example.storytv;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.KeyEvent;
import java.util.Locale;

final class ReadingTimer {
    private int minutes = 10;
    private long startedAt;
    private long pausedAt;
    private boolean paused;
    private int score;

    boolean setupKey(int key) {
        if (key == KeyEvent.KEYCODE_DPAD_UP) { minutes = Math.min(60, minutes + 1); return true; }
        if (key == KeyEvent.KEYCODE_DPAD_DOWN) { minutes = Math.max(1, minutes - 1); return true; }
        return false;
    }
    void start() { startedAt = SystemClock.elapsedRealtime(); paused = false; }
    void pause() { if (!paused && startedAt != 0) { pausedAt = SystemClock.elapsedRealtime(); paused = true; } }
    void togglePause() { if (paused) { startedAt += SystemClock.elapsedRealtime() - pausedAt; paused = false; } else pause(); }
    boolean isPaused() { return paused; }
    long remaining() { return Math.max(0, minutes * 60000L - ((paused ? pausedAt : SystemClock.elapsedRealtime()) - startedAt)); }
    void finish() { score = (int) (100L * remaining() / (minutes * 60000L)); }
    void drawSetup(Canvas c, Paint p) {
        p.setTextAlign(Paint.Align.CENTER); p.setTypeface(android.graphics.Typeface.create("sans-serif-medium", 0));
        p.setTextSize(23); p.setColor(0xff315f67);
        c.drawText("Time: " + minutes + " min   |   UP / DOWN to change", 640, 620, p);
        c.drawText("Press OK to start timer", 640, 656, p);
    }
    void drawTimer(Canvas c, Paint p) {
        long seconds = (remaining() + 999) / 1000;
        p.setTypeface(android.graphics.Typeface.create("sans-serif-medium", 0));
        p.setTextAlign(Paint.Align.CENTER); p.setTextSize(24); p.setColor(0xff20464c);
        c.drawText(String.format(Locale.US, "%s  %02d:%02d  ·  DOWN %s", paused ? "Paused" : "Time left", seconds / 60, seconds % 60, paused ? "resume" : "pause"), 640, 55, p);
    }
    void drawScore(Canvas c, Paint p) {
        p.setTextAlign(Paint.Align.CENTER); p.setTypeface(android.graphics.Typeface.create("sans-serif-medium", 0));
        p.setColor(0xff315f67); p.setTextSize(23);
        c.drawText("Time score: " + score + "% of time remaining", 640, 590, p);
    }
}
