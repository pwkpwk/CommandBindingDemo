package com.ambientbytes.commandbindingdemo;

import android.support.annotation.NonNull;

public interface IDispatcher {
    void dispatch(@NonNull Runnable runnable);
}
