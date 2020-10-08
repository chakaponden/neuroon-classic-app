package io.intercom.android.sdk.utilities;

import android.content.Context;
import io.intercom.android.sdk.R;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public String getFormattedTime(long time, Context context) {
        if (time <= 0) {
            return context.getResources().getString(R.string.intercomsdk_time_never);
        }
        Date now = new Date();
        Date date = new Date(1000 * time);
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - date.getTime());
        long diffInHours = TimeUnit.MILLISECONDS.toHours(now.getTime() - date.getTime());
        long diffInDays = TimeUnit.MILLISECONDS.toDays(now.getTime() - date.getTime());
        long diffInWeeks = diffInDays / 7;
        long diffInYears = diffInWeeks / 52;
        if (diffInYears > 0) {
            return context.getResources().getString(R.string.intercomsdk_time_year_ago, new Object[]{Long.valueOf(diffInYears)});
        } else if (diffInWeeks > 0) {
            return context.getResources().getString(R.string.intercomsdk_time_week_ago, new Object[]{Long.valueOf(diffInWeeks)});
        } else if (diffInDays > 0) {
            return context.getResources().getString(R.string.intercomsdk_time_day_ago, new Object[]{Long.valueOf(diffInDays)});
        } else if (diffInHours > 0) {
            return context.getResources().getString(R.string.intercomsdk_time_hour_ago, new Object[]{Long.valueOf(diffInHours)});
        } else if (diffInMin < 1) {
            return context.getResources().getString(R.string.intercomsdk_time_just_now);
        } else {
            return context.getResources().getString(R.string.intercomsdk_time_minute_ago, new Object[]{Long.valueOf(diffInMin)});
        }
    }
}
