package com.ambientbytes.commandbindingdemo;

import android.view.View;

public final class Converters {
    public int visibilityFromAvailability(boolean isAvailable) {
        return isAvailable ? View.VISIBLE : View.GONE;
    }
}
