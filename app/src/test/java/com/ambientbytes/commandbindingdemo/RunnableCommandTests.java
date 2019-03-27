package com.ambientbytes.commandbindingdemo;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class RunnableCommandTests {
    @Test(expected = IllegalArgumentException.class)
    public void newRunnableCommand_nullAction_Throws() {
        new RunnableCommand(null, false);
    }

    @Test
    public void newRunnable_correctAvailability() {
        Runnable action = mock(Runnable.class);
        ICommand command1 = new RunnableCommand(action, false);
        ICommand command2 = new RunnableCommand(action, true);

        assertFalse(command1.isAvailable());
        assertTrue(command2.isAvailable());
    }

    @Test
    public void available_execute_callsRunnable() {
        Runnable action = mock(Runnable.class);
        ICommand command = new RunnableCommand(action, true);

        command.execute();

        verify(action, times(1)).run();
    }

    @Test
    public void unavailable_execute_doesNotCallRunnable() {
        Runnable action = mock(Runnable.class);
        ICommand command = new RunnableCommand(action, false);

        command.execute();

        verifyZeroInteractions(action);
    }

    @Test
    public void available_changeAvailability_callsListener() {
        ICommand.IAvailabilityListener listener = mock(ICommand.IAvailabilityListener.class);
        Runnable action = mock(Runnable.class);
        RunnableCommand command = new RunnableCommand(action, true);
        command.setAvailabilityListener(listener);
        verify(listener, times(1)).setAvailable(true);
        verify(listener, never()).setAvailable(false);

        command.setAvailable(false);

        verify(listener, times(1)).setAvailable(false);
        verifyNoMoreInteractions(listener);
    }
}