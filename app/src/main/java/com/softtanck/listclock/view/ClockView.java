package com.softtanck.listclock.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * @author : Tanck
 * @Description : TODO 适配中的倒计时,性能优化,复用机制 参考了DigitalClock
 * @date 8/12/2015
 */
public class ClockView extends TextView {

    private boolean mTickerStopped;
    private Handler mHandler;
    private Runnable mTicker;
    private long endTime;

    private ClockListener mClockListener;


    public void setClockListener(ClockListener clockListener) {
        this.mClockListener = clockListener;
    }

    /**
     * Clock end time from now on.
     *
     * @param endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onAttachedToWindow() {

        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        getVisibility();
        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                long currentTime = System.currentTimeMillis();
                if (currentTime / 1000 == endTime / 1000 - 5 * 60 && null != mClockListener) {
                    mClockListener.remainFiveMinutes();
                }
                long distanceTime = endTime - currentTime;
                distanceTime /= 1000;
                if (distanceTime == 0) {
                    setText("00:00:00");
                    onDetachedFromWindow();
                    if (null != mClockListener)
                        mClockListener.timeEnd();
                } else if (distanceTime < 0) {
                    setText("00:00:00");
                } else {
                    setText(dealTime(distanceTime));
                }
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);// 够不够一秒,保证一秒更新一次
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();

    }

    /**
     * deal time string
     *
     * @param time
     * @return
     */
    public static String dealTime(long time) {
        StringBuffer returnString = new StringBuffer();
        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
        String dayStr = String.valueOf(day);
        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));
        returnString.append(hoursStr).append(":").append(minutesStr)
                .append(":").append(secondStr);
        return returnString.toString();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }


    /**
     * 回收后启动
     */
    public void changeTicker() {
        mTickerStopped = !mTickerStopped;
        if (!mTickerStopped) {
            mHandler.post(mTicker);
        }else{
            mHandler.removeCallbacks(mTicker);
        }
    }

    /**
     * format time
     *
     * @param timeStr
     * @return
     */
    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }


    public interface ClockListener {
        void timeEnd();

        void remainFiveMinutes();
    }
}
