package com.doongis.r2.FIT;

import android.graphics.Path;
import android.graphics.RectF;


public class OverdrawValueRenderer extends BaseRenderer implements Renderer {
    public OverdrawValueRenderer(RectF drawingArea, FitChartValue value) {
        super(drawingArea, value);
    }

    @Override
    public Path buildPath(float animationProgress, float animationSeek) {
        float startAngle = FitChart.START_ANGLE;
        float valueSweepAngle = (getValue().getStartAngle() +  getValue().getSweepAngle());
        valueSweepAngle -= startAngle;
        float sweepAngle = valueSweepAngle * animationProgress;
        Path path = new Path();
        path.addArc(getDrawingArea(), startAngle, sweepAngle);
        return path;
    }
}
