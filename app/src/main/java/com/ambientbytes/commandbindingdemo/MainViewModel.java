package com.ambientbytes.commandbindingdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintSet;

import java.io.Closeable;
import java.io.IOException;

public final class MainViewModel extends BaseObservable implements Closeable, IMainModel.ModelListener {
    @NonNull
    private final IMainModel mModel;
    private final RunnableCommand mPlopCommand;
    private final RunnableCommand mFlopCommand;
    private final IConstraintLayout mPlopLayout;
    private final IConstraintLayout mFlopLayout;
    private IConstraintLayout mBamLayout;

    public static final int BUTTON_SPACER_LABEL_ID = R.id.button_spacer_label;
    public static final int BUTTONSPACERCONSTRAINTS_PID = BR.buttonSpacerConstraints;

    public MainViewModel(@NonNull final IMainModel model) {
        mModel = model;
        mModel.addListener(this);

        mPlopCommand = new RunnableCommand(new Runnable() {
            @Override
            public void run() { executePlop(); }
        }, true);

        mFlopCommand = new RunnableCommand(new Runnable() {
            @Override
            public void run() { executeFlop(); }
        }, false);

        mPlopLayout = new IConstraintLayout() {
            @Override
            public void applyToConstraints(ILayoutConstraints constraints) {
                constraints.clear(BUTTON_SPACER_LABEL_ID, ConstraintSet.END);
                constraints.connect(BUTTON_SPACER_LABEL_ID, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            }
        };

        mFlopLayout = new IConstraintLayout() {
            @Override
            public void applyToConstraints(ILayoutConstraints constraints) {
                constraints.clear(BUTTON_SPACER_LABEL_ID, ConstraintSet.START);
                constraints.connect(BUTTON_SPACER_LABEL_ID, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            }
        };

        mBamLayout = null;
    }

    @Bindable
    public boolean isDND() {
        return mModel.isDND();
    }

    @Bindable
    public CharSequence getLabel() {
        return "Hello, World!";
    }

    @Bindable
    public ICommand getPlop() {
        return mPlopCommand;
    }

    @Bindable
    public ICommand getFlop() {
        return mFlopCommand;
    }

    @Bindable
    public IConstraintLayout getButtonSpacerConstraints() {
        return mBamLayout;
    }

    private void executePlop() {
        mPlopCommand.setAvailable(false);
        mFlopCommand.setAvailable(true);
        setButtonSpacerConstraints(mFlopLayout);
    }

    private void executeFlop() {
        mFlopCommand.setAvailable(false);
        mPlopCommand.setAvailable(true);
        setButtonSpacerConstraints(mPlopLayout);
    }

    private void setButtonSpacerConstraints(IConstraintLayout layout) {
        if (mBamLayout != layout) {
            mBamLayout = layout;
            notifyPropertyChanged(BUTTONSPACERCONSTRAINTS_PID);
        }
    }

    @Override
    public void close() throws IOException {
        mModel.removeListener(this);
    }

    @Override
    public void onDndChanged(IMainModel model) {
        notifyPropertyChanged(BR.dND);
    }
}
