package com.doongis.r2.drawing;

public class Point {
    int x;
    int y;

    @SuppressWarnings("unused")
    private Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
