package com.alpsmobi.mobile.listeners;

import android.app.Fragment;

public interface OnActivityCallBacks {
    void addFragment(Fragment fragment);
    void popFragment();
    void setValue(int value);
    int getValue();
}
