package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.models.network.Flight;
import java.util.ArrayList;
import java.util.List;

public class WorldMapRoadView extends ImageView {
    private static final float[] AA = {0.8487f, 0.8475118f, 0.844796f, 0.840213f, 0.83359313f, 0.8257851f, 0.814752f, 0.8000695f, 0.7821619f, 0.7606049f, 0.73658675f, 0.7086645f, 0.6777718f, 0.6447574f, 0.6098758f, 0.57134485f, 0.5272973f, 0.48562613f, 0.45167813f};
    private static final float[] BB = {0.0f, 0.0838426f, 0.1676852f, 0.2515278f, 0.3353704f, 0.419213f, 0.5030556f, 0.5868982f, 0.67182267f, 0.75336635f, 0.83518046f, 0.9153719f, 0.99339956f, 1.0687227f, 1.140665f, 1.2084153f, 1.2703506f, 1.31998f, 1.3523f};
    private static final float RADIAN = 0.017453292f;
    private Paint mDashPaint = new Paint(1);
    private int mEndFlightTimeZone = -1;
    private float mEndTimeZoneNormalized = -1.0f;
    private Path mEndTimeZonePath;
    private Path mFlightPath;
    private Paint mGradientPaint = new Paint(1);
    private int mHeight;
    private float mHourProgressNormalized;
    private List<PointF> mOriginalPoints;
    private Paint mPaint = new Paint(1);
    private List<PointF> mPoints;
    private float mProgress;
    private Rect mProgressRect;
    private float mRadius;
    private int mStartFlightTimeZone = -1;
    private float mStartTimeZoneNormalized = -1.0f;
    private int mWidth;

    public WorldMapRoadView(Context context) {
        super(context);
        init();
    }

    public WorldMapRoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorldMapRoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mPaint.setStrokeWidth(7.0f);
        this.mPaint.setColor(getResources().getColor(R.color.jet_lag_color));
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mDashPaint.setStrokeWidth(7.0f);
        this.mDashPaint.setColor(getResources().getColor(R.color.jet_lag_color));
        this.mDashPaint.setStyle(Paint.Style.STROKE);
        this.mDashPaint.setPathEffect(new DashPathEffect(new float[]{30.0f, 15.0f}, 0.0f));
        this.mGradientPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setFlights(List<Flight> flights) {
        List<PointF> points = new ArrayList<>(flights.size() + 1);
        for (Flight flight : flights) {
            if (points.size() == 0) {
                points.add(new PointF(flight.getOriginAirport().getLatitude(), flight.getOriginAirport().getLongitude()));
            }
            points.add(new PointF(flight.getDestinationAirport().getLatitude(), flight.getDestinationAirport().getLongitude()));
        }
        setPoints(points);
    }

    public void setPoints(List<PointF> points) {
        this.mOriginalPoints = points;
        calculatePoints();
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.mRadius = (((float) w) / 2.6662698f) / 2.0f;
        calculatePoints();
        countTimeZoneLines();
    }

    private void calculatePoints() {
        if (this.mPoints != null) {
            this.mPoints.clear();
        } else {
            this.mPoints = new ArrayList();
        }
        if (this.mOriginalPoints != null && this.mOriginalPoints.size() != 0) {
            for (PointF point : this.mOriginalPoints) {
                this.mPoints.add(getPointFromCoordinate(point.x, point.y));
            }
            generateFlightPath();
        }
    }

    private void generateFlightPath() {
        this.mFlightPath = new Path();
        if (this.mPoints != null && this.mPoints.size() > 0) {
            PointF startPoint = this.mPoints.get(0);
            this.mFlightPath.moveTo(startPoint.x, startPoint.y);
            for (int i = 1; i < this.mPoints.size(); i++) {
                PointF pointF = this.mPoints.get(i);
                this.mFlightPath.quadTo((startPoint.x + pointF.x) / 2.0f, Math.min(startPoint.y, pointF.y) - 50.0f, pointF.x, pointF.y);
                startPoint = pointF;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mFlightPath != null) {
            canvas.drawPath(this.mFlightPath, this.mPaint);
        }
        if (this.mEndTimeZonePath != null) {
            canvas.drawPath(this.mEndTimeZonePath, this.mDashPaint);
        }
        if (this.mProgressRect != null) {
            canvas.drawRect(this.mProgressRect, this.mGradientPaint);
        } else if (this.mStartFlightTimeZone > -1) {
            canvas.drawLine((float) this.mStartFlightTimeZone, 0.0f, (float) this.mStartFlightTimeZone, (float) getMeasuredHeight(), this.mPaint);
        }
    }

    private PointF getPointFromCoordinate(float latitude, float longitude) {
        int latLessThanZero = latitude < 0.0f ? -1 : 1;
        float latitude2 = Math.abs(latitude);
        int longLessThanZero = longitude < 0.0f ? -1 : 1;
        float longitude2 = Math.abs(longitude);
        float low = latitude2 == 0.0f ? 0.0f : (float) (Math.floor((((double) latitude2) - 1.0E-10d) / 5.0d) * 5.0d);
        int lowIndex = (int) (low / 5.0f);
        int highIndex = (int) ((low + 5.0f) / 5.0f);
        float ratio = (latitude2 - low) / 5.0f;
        return new PointF((((AA[highIndex] - (AA[lowIndex] * ratio)) + AA[lowIndex]) * longitude2 * RADIAN * ((float) longLessThanZero) * this.mRadius) + (((float) this.mWidth) / 2.0f), ((((float) this.mHeight) / 2.0f) - (((float) latLessThanZero) * ((BB[highIndex] - (BB[lowIndex] * ratio)) + BB[lowIndex]))) + this.mRadius);
    }

    public void setTimeZones(float startTimeZone, float endTimeZone, float hourProgress) {
        this.mStartTimeZoneNormalized = (startTimeZone + 12.0f) / 24.0f;
        this.mEndTimeZoneNormalized = (endTimeZone + 12.0f) / 24.0f;
        this.mHourProgressNormalized = (hourProgress + 12.0f) / 24.0f;
        countTimeZoneLines();
        invalidate();
    }

    private void countTimeZoneLines() {
        if (this.mStartTimeZoneNormalized != -1.0f && this.mStartTimeZoneNormalized != -1.0f) {
            this.mStartFlightTimeZone = (int) (((float) getMeasuredWidth()) * this.mStartTimeZoneNormalized);
            this.mEndFlightTimeZone = (int) (((float) getMeasuredWidth()) * this.mEndTimeZoneNormalized);
            this.mEndTimeZonePath = new Path();
            this.mEndTimeZonePath.moveTo((float) this.mEndFlightTimeZone, 0.0f);
            this.mEndTimeZonePath.lineTo((float) this.mEndFlightTimeZone, (float) getMeasuredHeight());
            if (((double) Math.abs(this.mHourProgressNormalized - this.mStartTimeZoneNormalized)) < 1.0E-7d && ((double) Math.abs(this.mHourProgressNormalized - this.mEndTimeZoneNormalized)) < 1.0E-7d) {
                if (this.mHourProgressNormalized < ((float) this.mStartFlightTimeZone)) {
                    this.mProgressRect = new Rect((int) (this.mHourProgressNormalized * ((float) getMeasuredWidth())), 0, this.mStartFlightTimeZone, getMeasuredHeight());
                    this.mGradientPaint.setShader(new LinearGradient((float) this.mStartFlightTimeZone, 0.0f, (float) ((int) (this.mHourProgressNormalized * ((float) getMeasuredWidth()))), 0.0f, getResources().getColor(R.color.jet_lag_color_alpha), getResources().getColor(R.color.jet_lag_color), Shader.TileMode.MIRROR));
                    return;
                }
                this.mProgressRect = new Rect(this.mStartFlightTimeZone, 0, (int) (this.mHourProgressNormalized * ((float) getMeasuredWidth())), getMeasuredHeight());
                this.mGradientPaint.setShader(new LinearGradient((float) this.mStartFlightTimeZone, 0.0f, (float) ((int) (this.mHourProgressNormalized * ((float) getMeasuredWidth()))), 0.0f, getResources().getColor(R.color.jet_lag_color), getResources().getColor(R.color.jet_lag_color_alpha), Shader.TileMode.MIRROR));
            }
        }
    }
}
