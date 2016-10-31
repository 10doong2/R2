package com.doongis.r2.FIT;

import android.graphics.Path;


public interface Renderer {
    Path buildPath(float animationProgress, float animationSeek);
}
