package com.ambientbytes.commandbindingdemo;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;

public final class ConstraintUpdater {
    /**
     * Binding adapter that updates constraints of constraint layouts.
     * <p>
     * A vew model binds its property of the type IConstraintLayout to a ConstraintLayout element
     * of a layout and updates the property to apply changes to the bound element.
     *
     * @param layout  layout that's constraints should be updated by an updater.
     * @param updater optional updater that modifies constraints of the layout.
     */
    @BindingAdapter("layoutConstraints")
    public static void applyLayoutConstraints(
            @NonNull final ConstraintLayout layout,
            @Nullable final IConstraintLayout updater) {
        if (updater != null) {
            final ConstraintSet constraints = deriveConstraints(layout);
            updater.applyToConstraints(new Constraints(constraints));
            constraints.applyTo(layout);
        }
    }

    private static ConstraintSet deriveConstraints(@NonNull final ConstraintLayout layout) {
        final ConstraintSet constraints = new ConstraintSet();
        constraints.clone(layout);
        return constraints;
    }

    /**
     * Wrapper of ConstraintSet that implements ILayoutConstraints on top of an actual ConstraintSet
     * object derived from e ConstraintLayout view.
     */
    private static class Constraints implements ILayoutConstraints {
        @NonNull
        private final ConstraintSet mConstraints;

        public Constraints(@NonNull final ConstraintSet constraints) {
            mConstraints = constraints;
        }

        @Override
        public void connect(int startID, int startSide, int endID, int endSide) {
            mConstraints.connect(startID, startSide, endID, endSide);
        }

        @Override
        public void clear(int viewId, int anchor) {
            mConstraints.clear(viewId, anchor);
        }
    }
}
