package com.ambientbytes.commandbindingdemo;

import android.databinding.Bindable;
import android.databinding.Observable;

/**
 * A construct similar to ICommand found in WPF and UWP and designed to be exposed as a property
 * of a view model that can be attached by a binding adapter to a View to listen to the view's
 * "onClick" events and to update the view's "clickable" property.
 *
 * By design, an instance of ICommand can be bound to only one UI element and change its "clickable"
 * property. If availability of a command object should trigger changes of more than one UI element,
 * the "available" property of the command may be bound to properties those additional elements.
 */
public interface ICommand extends Observable {
    /**
     * Execute the command.
     */
    void execute();

    /**
     * Observable read-only property that the command sets to true when it can be executed.
     * Change of the value is reported through the Observable interface after the attached
     * availability listener has been called.
     * @return true if the command can be executed; otherwise, false.
     */
    @Bindable
    public boolean isAvailable();

    /**
     * Attach a listener of changes of the command execution availability.
     * @param listener object that the command will call when availability of its execution has changed.
     */
    void setAvailabilityListener(IAvailabilityListener listener);

    interface IAvailabilityListener {
        void setAvailable(boolean isAvailable);
    }
}
