package com.inteliclinic.neuroon.views;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class ArcUtils {
    private static final double FULL_CIRCLE_RADIANS = Math.toRadians(360.0d);

    private ArcUtils() {
    }

    public static void drawArc(@NonNull Canvas canvas, PointF circleCenter, float circleRadius, float startAngle, float sweepAngle, @NonNull Paint paint) {
        drawArc(canvas, circleCenter, circleRadius, startAngle, sweepAngle, paint, 8, false);
    }

    public static void drawArc(@NonNull Canvas canvas, PointF circleCenter, float circleRadius, float startAngle, float sweepAngle, @NonNull Paint paint, int arcsPointsOnCircle, boolean arcsOverlayPoints) {
        if (sweepAngle != 0.0f) {
            canvas.drawPath(createBezierArcDegrees(circleCenter, circleRadius, startAngle, sweepAngle, arcsPointsOnCircle, arcsOverlayPoints, (Path) null), paint);
        }
    }

    public static double normalizeRadians(double radians) {
        double radians2 = radians % FULL_CIRCLE_RADIANS;
        if (radians2 < 0.0d) {
            radians2 += FULL_CIRCLE_RADIANS;
        }
        if (Math.abs(radians2 - FULL_CIRCLE_RADIANS) < 1.0E-7d) {
            return 0.0d;
        }
        return radians2;
    }

    @NonNull
    public static PointF pointFromAngleRadians(@NonNull PointF center, float radius, double angleRadians) {
        return new PointF((float) (((double) center.x) + (((double) radius) * Math.cos(angleRadians))), (float) (((double) center.y) + (((double) radius) * Math.sin(angleRadians))));
    }

    @NonNull
    public static PointF pointFromAngleDegrees(@NonNull PointF center, float radius, float angleDegrees) {
        return pointFromAngleRadians(center, radius, Math.toRadians((double) angleDegrees));
    }

    public static void addBezierArcToPath(@NonNull Path path, @NonNull PointF center, @NonNull PointF start, @NonNull PointF end, boolean moveToStart) {
        if (moveToStart) {
            path.moveTo(start.x, start.y);
        }
        if (!start.equals(end)) {
            double ax = (double) (start.x - center.x);
            double ay = (double) (start.y - center.y);
            double bx = (double) (end.x - center.x);
            double by = (double) (end.y - center.y);
            double q1 = (ax * ax) + (ay * ay);
            double q2 = (ax * bx) + q1 + (ay * by);
            double k2 = (1.3333333333333333d * (Math.sqrt((2.0d * q1) * q2) - q2)) / ((ax * by) - (ay * bx));
            path.cubicTo((float) ((((double) center.x) + ax) - (k2 * ay)), (float) (((double) center.y) + ay + (k2 * ax)), (float) (((double) center.x) + bx + (k2 * by)), (float) ((((double) center.y) + by) - (k2 * bx)), end.x, end.y);
        }
    }

    @NonNull
    public static Path createBezierArcRadians(@NonNull PointF center, float radius, double startAngleRadians, double sweepAngleRadians, int pointsOnCircle, boolean overlapPoints, @Nullable Path addToPath) {
        double d;
        Path path = addToPath != null ? addToPath : new Path();
        if (sweepAngleRadians != 0.0d) {
            if (pointsOnCircle >= 1) {
                double threshold = FULL_CIRCLE_RADIANS / ((double) pointsOnCircle);
                if (Math.abs(sweepAngleRadians) > threshold) {
                    double angle = normalizeRadians(startAngleRadians);
                    PointF start = pointFromAngleRadians(center, radius, angle);
                    path.moveTo(start.x, start.y);
                    if (overlapPoints) {
                        boolean cw = sweepAngleRadians > 0.0d;
                        double angleEnd = angle + sweepAngleRadians;
                        while (true) {
                            double next = (cw ? Math.ceil(angle / threshold) : Math.floor(angle / threshold)) * threshold;
                            if (Math.abs(angle - next) < 1.0E-7d) {
                                next += (cw ? 1.0d : -1.0d) * threshold;
                            }
                            boolean isEnd = cw ? angleEnd <= next : angleEnd >= next;
                            if (isEnd) {
                                d = angleEnd;
                            } else {
                                d = next;
                            }
                            PointF end = pointFromAngleRadians(center, radius, d);
                            addBezierArcToPath(path, center, start, end, false);
                            if (isEnd) {
                                break;
                            }
                            angle = next;
                            start = end;
                        }
                    } else {
                        int n = Math.abs((int) Math.ceil(sweepAngleRadians / threshold));
                        double sweep = sweepAngleRadians / ((double) n);
                        int i = 0;
                        while (i < n) {
                            angle += sweep;
                            PointF end2 = pointFromAngleRadians(center, radius, angle);
                            addBezierArcToPath(path, center, start, end2, false);
                            i++;
                            start = end2;
                        }
                    }
                }
            }
            addBezierArcToPath(path, center, pointFromAngleRadians(center, radius, startAngleRadians), pointFromAngleRadians(center, radius, startAngleRadians + sweepAngleRadians), true);
        }
        return path;
    }

    @NonNull
    public static Path createBezierArcDegrees(@NonNull PointF center, float radius, float startAngleDegrees, float sweepAngleDegrees, int pointsOnCircle, boolean overlapPoints, @Nullable Path addToPath) {
        return createBezierArcRadians(center, radius, Math.toRadians((double) startAngleDegrees), Math.toRadians((double) sweepAngleDegrees), pointsOnCircle, overlapPoints, addToPath);
    }
}
