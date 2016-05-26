package com.example.administrator.verticalviewpage;

/**
 * Created by LiuXiaocong on 4/6/2016.
 */
public enum TCurrentTouchAreaType {
    ENone(0), EFirstScreen(1), EHotArea(2), ENextScreen(3);
    private final int value;

    TCurrentTouchAreaType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}