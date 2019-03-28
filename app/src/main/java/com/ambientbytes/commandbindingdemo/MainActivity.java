package com.ambientbytes.commandbindingdemo;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import com.ambientbytes.commandbindingdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final MainViewModel mViewModel;

    public MainActivity() {
        mViewModel = new MainViewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.messageContainer.setTag(R.id.tag_layout_root, binding.layoutRoot);
        binding.topSpacer.setTag(R.id.tag_layout_root, binding.layoutRoot);
        binding.setConverters(new Converters());
        binding.setViewModel(mViewModel);
        binding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                AutoTransition transition = new AutoTransition();
                transition.setDuration(100);
                TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot(), transition);
                return super.onPreBind(binding);
            }
        });
        binding.executePendingBindings();
    }

    @BindingAdapter("app:rolledInFromTop")
    public static void setRolledInFromTop(View view, boolean isAvailable) {
        ConstraintLayout layout = (ConstraintLayout) view.getTag(R.id.tag_layout_root);

        if (layout != null) {
            ConstraintSet constraints = new ConstraintSet();

            constraints.clone(layout);

            if (isAvailable) {
                constraints.connect(R.id.top_spacer, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraints.connect(R.id.greeting_label, ConstraintSet.TOP, R.id.top_spacer, ConstraintSet.BOTTOM);
                constraints.connect(R.id.top_spacer, ConstraintSet.BOTTOM, R.id.greeting_label, ConstraintSet.TOP);
            } else {
                constraints.connect(R.id.top_spacer, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraints.connect(R.id.greeting_label, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraints.clear(R.id.top_spacer, ConstraintSet.TOP);
            }

            constraints.applyTo(layout);
        }
    }

    @BindingAdapter("app:rolledInFromBottom")
    public static void setRolledInFromBottom(View view, boolean isAvailable) {
        ConstraintLayout layout = (ConstraintLayout) view.getTag(R.id.tag_layout_root);

        if (layout != null) {
            ConstraintSet constraints = new ConstraintSet();

            constraints.clone(layout);

            if (isAvailable) {
                constraints.connect(R.id.spacer, ConstraintSet.BOTTOM, R.id.message_container, ConstraintSet.TOP);
                constraints.connect(R.id.message_container, ConstraintSet.TOP, R.id.spacer, ConstraintSet.BOTTOM);
                constraints.connect(R.id.message_container, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            } else {
                constraints.connect(R.id.spacer, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                constraints.connect(R.id.message_container, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                constraints.clear(R.id.message_container, ConstraintSet.BOTTOM);
            }

            constraints.applyTo(layout);
        }
    }
}
