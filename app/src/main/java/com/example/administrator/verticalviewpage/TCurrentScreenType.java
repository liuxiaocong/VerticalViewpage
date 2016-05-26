package com.example.administrator.verticalviewpage;

/**
 * Created by LiuXiaocong on 4/6/2016.
 */
public enum TCurrentScreenType {
    EFirstScreen(1), EMainScreen(2), ENextScreen(3);
    private final int value;

    TCurrentScreenType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
