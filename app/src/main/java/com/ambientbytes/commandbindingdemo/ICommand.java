package com.ambientbytes.commandbindingdemo;

import android.databinding.Bindable;
import android.databinding.Observable;

public interface ICommand extends Observable {
    void execute();

    @Bindable
    public boolean isAvailable();

    void setAvailabilityListener(IAvailabilityListener listener);

    interface IAvailabilityListener {
        void setAvailable(boolean isAvailable);
    }
}
