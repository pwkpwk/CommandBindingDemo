package com.ambientbytes.commandbindingdemo;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;

import java.util.HashMap;
import java.util.Map;

final class ConstraintManager {
    private final Map<String, IConstraintUpdater> mUpdaters;
    private final ConstraintLayout mRoot;
    private IConstraintUpdater mDefaultUpdater;

    public ConstraintManager(ConstraintLayout root) {
        mUpdaters = new HashMap<>();
        mRoot = root;
        mDefaultUpdater = null;
    }

    public void register(Object key, IConstraintUpdater updater) {
        mUpdaters.put(key.toString(), updater);
    }

    public void registerDefault(IConstraintUpdater updater) {
        mDefaultUpdater = updater;
    }

    public void apply(Object key) {
        IConstraintUpdater updater = mUpdaters.get(key.toString());

        if (updater == null) {
            updater = mDefaultUpdater;
        }

        if (updater != null) {
            ConstraintSet constraints = new ConstraintSet();
            constraints.clone(mRoot);
            updater.update(constraints);
            constraints.applyTo(mRoot);
        }
    }
}
