package com.ambientbytes.commandbindingdemo;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import com.ambientbytes.commandbindingdemo.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements IDispatcher {
    private final IMainModel mModel;
    private final MainViewModel mViewModel;

    public MainActivity() {
        mModel = new MainModel(this);
        mViewModel = new MainViewModel(mModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //
        // Attach a constraint manager that handles boolean true/false constraint states of the message
        // container that slides in and out at the bottom of the activity.
        //
        ConstraintManager manager = new ConstraintManager(binding.layoutRoot);
        manager.register(true, new IConstraintUpdater() {
            @Override
            public void update(ConstraintSet constraints) {
                constraints.connect(R.id.spacer, ConstraintSet.BOTTOM, R.id.message_container, ConstraintSet.TOP);
                constraints.connect(R.id.message_container, ConstraintSet.TOP, R.id.spacer, ConstraintSet.BOTTOM);
                constraints.connect(R.id.message_container, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            }
        });
        manager.registerDefault(new IConstraintUpdater() {
            @Override
            public void update(ConstraintSet constraints) {
                constraints.connect(R.id.spacer, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                constraints.connect(R.id.message_container, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                constraints.clear(R.id.message_container, ConstraintSet.BOTTOM);
            }
        });
        binding.messageContainer.setTag(R.id.tag_constraint_manager, manager);
        //
        // Attach a constraint manager that handles boolean true/false constraint states of the spacer
        // that slides in and out at the top of the activity.
        //
        manager = new ConstraintManager(binding.layoutRoot);
        manager.register(true, new IConstraintUpdater() {
            @Override
            public void update(ConstraintSet constraints) {
                constraints.connect(R.id.top_spacer, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraints.connect(R.id.greeting_label, ConstraintSet.TOP, R.id.top_spacer, ConstraintSet.BOTTOM);
                constraints.connect(R.id.top_spacer, ConstraintSet.BOTTOM, R.id.greeting_label, ConstraintSet.TOP);
            }
        });
        manager.registerDefault(new IConstraintUpdater() {
            @Override
            public void update(ConstraintSet constraints) {
                constraints.connect(R.id.top_spacer, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraints.connect(R.id.greeting_label, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraints.clear(R.id.top_spacer, ConstraintSet.TOP);
            }
        });
        binding.topSpacer.setTag(R.id.tag_constraint_manager, manager);

        binding.setConverters(new Converters());
        binding.setViewModel(mViewModel);
        binding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                AutoTransition transition = new AutoTransition();
                transition.setDuration(125);
                TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot(), transition);
                return super.onPreBind(binding);
            }
        });
        binding.executePendingBindings();

        mModel.start(getContentResolver());
    }

    @Override
    protected void onDestroy() {
        try {
            mViewModel.close();
        } catch (IOException ex) {
        }
        try {
            mModel.close();
        } catch (IOException ex) {
        }
        super.onDestroy();
    }

    @BindingAdapter("constraintsState")
    public static void applyConstraintState(View view, Object stateKey) {
        Object tag = view.getTag(R.id.tag_constraint_manager);

        if (tag instanceof ConstraintManager) {
            ConstraintManager manager = (ConstraintManager) tag;
            manager.apply(stateKey);
        }
    }

    @Override
    public void dispatch(@NonNull Runnable runnable) {
        runOnUiThread(runnable);
    }
}
