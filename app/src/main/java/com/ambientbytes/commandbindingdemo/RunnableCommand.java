package com.ambientbytes.commandbindingdemo;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Implementation if ICommand that executes a specified Runnable object and allows external changes
 * of the "available" bindable property.
 */
public final class RunnableCommand extends BaseObservable implements ICommand {
    @NonNull
    private final Runnable mAction;
    private IAvailabilityListener mListener;
    private boolean mIsAvailable;

    public static final int ISAVAILABLE_PID = BR.available;

    public RunnableCommand(@NonNull Runnable action, boolean canExecute) {
        if (action == null) {
            throw new IllegalArgumentException("action cannot be null.");
        }
        mAction = action;
        mListener = null;
        mIsAvailable = canExecute;
    }

    @Override
    public void execute() {
        if (mIsAvailable) {
            mAction.run();
        }
    }

    @Override
    public boolean isAvailable() {
        return mIsAvailable;
    }

    @Override
    public void setAvailabilityListener(IAvailabilityListener listener) {
        if (mListener != listener) {
            if (mListener != null) {
                mListener.setAvailable(false);
            }

            mListener = listener;

            if (mListener != null) {
                mListener.setAvailable(mIsAvailable);
            }
        }
    }

    public void setAvailable(boolean available) {
        if (available != mIsAvailable) {
            mIsAvailable = available;

            if (mListener != null) {
                mListener.setAvailable(mIsAvailable);
            }
            notifyPropertyChanged(ISAVAILABLE_PID);
        }
    }

    @BindingAdapter("android:onClick")
    public static void attachComand(final View view, final ICommand oldCommand, final ICommand newCommand) {
        if (oldCommand != null) {
            oldCommand.setAvailabilityListener(null);
        }

        if (newCommand != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newCommand.execute();
                }
            });
            newCommand.setAvailabilityListener(new ICommand.IAvailabilityListener() {
                @Override
                public void setAvailable(boolean isAvailable) {
                    view.setClickable(isAvailable);
                }
            });
        } else {
            view.setClickable(false);
            view.setOnClickListener(null);
        }
    }
}
