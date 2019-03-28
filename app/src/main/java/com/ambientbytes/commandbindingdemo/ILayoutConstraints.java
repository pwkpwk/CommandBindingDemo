package com.ambientbytes.commandbindingdemo;

/**
 * A subset of functionality of ConstraintSet.
 * The interface allows unit test of IConstraintLayout properties of view models by removing dependencies
 * to Android classes and by enabling mocking of layouts for verification.
 */
public interface ILayoutConstraints {
    void connect(int startID, int startSide, int endID, int endSide);
    void clear(int viewId, int anchor);
}
