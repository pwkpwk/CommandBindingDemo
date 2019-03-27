package com.ambientbytes.commandbindingdemo;

import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
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
        binding.setConverters(new Converters());
        binding.setViewModel(mViewModel);
        binding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot());
                return super.onPreBind(binding);
            }
        });
    }
}
