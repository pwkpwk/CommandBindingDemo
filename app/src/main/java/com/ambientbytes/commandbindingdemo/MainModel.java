package com.ambientbytes.commandbindingdemo;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import static android.provider.Settings.System.CONTENT_URI;

public final class MainModel implements IMainModel, Handler.Callback {
    private final IDispatcher mDispatcher;
    private final ContentObserver mObserver;
    private final CopyOnWriteArraySet<ModelListener> mListeners;

    private ContentResolver mResolver;
    private boolean mIsDnd;

    public MainModel(@NonNull IDispatcher dispatcher) {
        mDispatcher = dispatcher;
        mObserver = new DndObserver(new Handler(this));
        mListeners = new CopyOnWriteArraySet<>();
        mIsDnd = false;
        mResolver = null;
    }

    @Override
    public void close() throws IOException {
        mResolver.unregisterContentObserver(mObserver);
        mResolver = null;
    }

    @Override
    public void start(ContentResolver resolver) {
        mResolver = resolver;
        resolver.registerContentObserver(CONTENT_URI, true, mObserver);
        setIsDND(getDndStatus());
    }

    @Override
    public boolean isDND() {
        return mIsDnd;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void addListener(final ModelListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeListener(final ModelListener listener) {
        mListeners.remove(listener);
    }

    private void setIsDND(final boolean isDND) {
        if (isDND != mIsDnd) {
            mIsDnd = isDND;
            for (ModelListener listener : mListeners) {
                listener.onDndChanged(this);
            }
        }
    }

    private boolean getDndStatus() {
        boolean dnd = false;

        if (mResolver != null) {
            try {
                switch (Settings.Global.getInt(mResolver, "zen_mode")) {
                    case 0:
                        break;

                    default:
                        dnd = true;
                        break;
                }

            } catch (Settings.SettingNotFoundException ex) {
            }
        }

        return dnd;
    }

    private final class DndObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DndObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            mDispatcher.dispatch(new Runnable() {
                @Override
                public void run() {
                    setIsDND(getDndStatus());
                }
            });
        }
    }
}
