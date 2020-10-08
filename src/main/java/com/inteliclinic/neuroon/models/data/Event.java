package com.inteliclinic.neuroon.models.data;

import com.google.gson.annotations.SerializedName;
import com.inteliclinic.neuroon.utils.UnitConverter;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.util.Date;

public class Event extends BaseModel {
    private static final long MIN20_MILLIS = 1200000;
    private static final long MIN30_MILLIS = 1800000;
    private static final long MIN60_MILLIS = 3600000;
    private static final long MIN90_MILLIS = 5400000;
    @SerializedName("completed")
    boolean mCompleted;
    @SerializedName("endDate")
    Date mEndDate;
    @SerializedName("id")
    long mId;
    @SerializedName("startDate")
    Date mStartDate;
    @SerializedName("type")
    EventType mType;

    public Event() {
    }

    private Event(Date startDate, Date endDate, EventType type) {
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mType = type;
        this.mCompleted = false;
    }

    public static Event sleepEvent(Date alarmTime) {
        return new Event(new Date(), alarmTime, EventType.ETSleep);
    }

    public static Event seekLightEvent(long startMilliseconds, long endMilliseconds) {
        return new Event(new Date(startMilliseconds), new Date(endMilliseconds), EventType.ETSeekLight);
    }

    public static Event avoidLightEvent(long startMilliseconds, long endMilliseconds) {
        return new Event(new Date(startMilliseconds), new Date(endMilliseconds), EventType.ETAvoidLight);
    }

    public static Event feelingSleepyEvent(long startMilliseconds, long endMilliseconds) {
        return new Event(new Date(startMilliseconds), new Date(endMilliseconds), EventType.ETSleepy);
    }

    public static Event lightBoostTherapy() {
        return new Event(new Date(), new Date(new Date().getTime() + MIN20_MILLIS), EventType.ETBLT);
    }

    public static Event powerNapTherapy(int durationMilliseconds) {
        return new Event(new Date(), new Date(new Date().getTime() + ((long) durationMilliseconds)), EventType.ETNapPower);
    }

    public static Event bodyNapTherapy(int durationMilliseconds) {
        return new Event(new Date(), new Date(new Date().getTime() + ((long) durationMilliseconds)), EventType.ETNapBody);
    }

    public static Event bodyNapTherapy(Date startDate, Date endDate) {
        return new Event(startDate, endDate, EventType.ETNapBody);
    }

    public static Event remNapTherapy(int durationMilliseconds) {
        return new Event(new Date(), new Date(new Date().getTime() + ((long) durationMilliseconds)), EventType.ETNapRem);
    }

    public static Event remNapTherapy(Date startDate, Date endDate) {
        return new Event(startDate, endDate, EventType.ETNapRem);
    }

    public static Event ultimateNapTherapy(int durationMilliseconds) {
        return new Event(new Date(), new Date(new Date().getTime() + ((long) durationMilliseconds)), EventType.ETNapUltimate);
    }

    public static Event ultimateNapTherapy(Date startDate, Date endDate) {
        return new Event(startDate, endDate, EventType.ETNapUltimate);
    }

    public static Event pairMaskEvent(Date pairDate) {
        return new Event(pairDate, pairDate, EventType.ETPaired);
    }

    public EventType getType() {
        return this.mType;
    }

    public void setType(Sleep sleep) {
        this.mType = EventType.convertFromSleep(sleep);
    }

    public Date getStartDate() {
        return this.mStartDate;
    }

    public void setStartDate(Date startDate) {
        this.mStartDate = startDate;
    }

    public Date getEndDate() {
        return this.mEndDate;
    }

    public void setEndDate(Date endDate) {
        this.mEndDate = endDate;
    }

    public boolean isCompleted() {
        return this.mCompleted;
    }

    public void setCompleted(boolean completed) {
        this.mCompleted = completed;
    }

    public int getDuration() {
        long duration = getEndDate().getTime() - getStartDate().getTime();
        EventType type = getType();
        if (type != null) {
            switch (type) {
                case ETBLT:
                    if (duration > MIN20_MILLIS) {
                        duration = MIN20_MILLIS;
                        break;
                    }
                    break;
                case ETNapPower:
                    if (duration > MIN20_MILLIS) {
                        duration = MIN20_MILLIS;
                        break;
                    }
                    break;
                case ETNapBody:
                    if (duration > MIN30_MILLIS) {
                        duration = MIN30_MILLIS;
                        break;
                    }
                    break;
                case ETNapRem:
                    if (duration > MIN60_MILLIS) {
                        duration = MIN60_MILLIS;
                        break;
                    }
                    break;
                case ETNapUltimate:
                    if (duration > MIN90_MILLIS) {
                        duration = MIN90_MILLIS;
                        break;
                    }
                    break;
            }
        }
        return (int) UnitConverter.millisToSec(duration);
    }

    public long getId() {
        return this.mId;
    }

    public enum EventType {
        ETNapPower,
        ETNapBody,
        ETNapRem,
        ETNapUltimate,
        ETSleep,
        ETBLT,
        ETAvoidLight,
        ETSeekLight,
        ETSleepy,
        ETPaired,
        ETJetLagTreatment,
        ETJetLagFlight,
        ETJetLagFlightDelayed,
        ETJetLagConfigurated,
        ETJetLagCancelled,
        ETSeekSleep,
        ETAvoidSleep;

        public static EventType convertFromSleep(Sleep sleep) {
            if (sleep == null) {
                return ETSleep;
            }
            switch (sleep.getSleepType()) {
                case 1:
                case 2:
                    return ETSleep;
                case 3:
                    return ETBLT;
                case 4:
                    int sleepDuration = sleep.getSleepDuration();
                    if (sleepDuration <= 1200) {
                        return ETNapPower;
                    }
                    if (sleepDuration <= 1800) {
                        return ETNapBody;
                    }
                    if (sleepDuration <= 3600) {
                        return ETNapRem;
                    }
                    return ETNapUltimate;
                default:
                    return ETSleep;
            }
        }
    }
}
