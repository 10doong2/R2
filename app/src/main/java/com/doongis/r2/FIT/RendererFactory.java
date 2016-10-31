package com.doongis.r2.FIT;

import android.graphics.RectF;

public class RendererFactory {
    public static Renderer getRenderer(AnimationMode mode, FitChartValue value, RectF drawingArea) {
        if (mode == AnimationMode.LINEAR) {
            return new LinearValueRenderer(drawingArea, value);
        } else {
            return new OverdrawValueRenderer(drawingArea, value);
        }
    }
}
