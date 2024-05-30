package com.dating.flirtify.Services;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public class CountdownTimerHelper {
    private long endTime;
    private Handler handler;
    private Runnable countdownRunnable;
    private CountdownListener countdownListener;

    public interface CountdownListener {
        void onCountdownTick(String formattedTime);
        void onCountdownFinish();
    }

    public CountdownTimerHelper(long durationMillis, CountdownListener listener) {
        this.endTime = System.currentTimeMillis() + durationMillis;
        this.countdownListener = listener;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void start() {
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                long millisUntilFinished = endTime - System.currentTimeMillis();
                if (millisUntilFinished > 0) {
                    long seconds = millisUntilFinished / 1000;
                    String formattedTime = String.format("%02d:%02d", seconds / 60, seconds % 60);
                    if (countdownListener != null) {
                        countdownListener.onCountdownTick(formattedTime);
                    }
                    handler.postDelayed(this, 1000);
                } else {
                    if (countdownListener != null) {
                        countdownListener.onCountdownFinish();
                    }
                }
            }
        };
        handler.post(countdownRunnable);
    }

    public void cancel() {
        handler.removeCallbacks(countdownRunnable);
    }

    public boolean isCountdownFinished() {
        return System.currentTimeMillis() >= endTime;
    }
}
