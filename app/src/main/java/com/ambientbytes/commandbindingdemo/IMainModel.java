package com.ambientbytes.commandbindingdemo;

import android.content.ContentResolver;

import java.io.Closeable;

public interface IMainModel extends Closeable {
    void start(ContentResolver resolver);
    boolean isDND();
    void addListener(ModelListener listener);
    void removeListener(ModelListener listener);

    interface ModelListener {
        void onDndChanged(IMainModel model);
    }
}
