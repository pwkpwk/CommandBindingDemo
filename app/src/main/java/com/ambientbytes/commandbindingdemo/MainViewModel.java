package com.ambientbytes.commandbindingdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public final class MainViewModel extends BaseObservable {
    private final RunnableCommand mPlopCommand;
    private final RunnableCommand mFlopCommand;

    public MainViewModel() {
        mPlopCommand = new RunnableCommand(new Runnable() {
            @Override
            public void run() { executePlop(); }
        }, true);

        mFlopCommand = new RunnableCommand(new Runnable() {
            @Override
            public void run() { executeFlop(); }
        }, false);
    }

    @Bindable
    public CharSequence getLabel() {
        return "Hello, World!";
    }

    public ICommand getPlop() {
        return mPlopCommand;
    }

    public ICommand getFlop() {
        return mFlopCommand;
    }

    private void executePlop() {
        mPlopCommand.setAvailable(false);
        mFlopCommand.setAvailable(true);
    }

    private void executeFlop() {
        mPlopCommand.setAvailable(true);
        mFlopCommand.setAvailable(false);
    }
}
