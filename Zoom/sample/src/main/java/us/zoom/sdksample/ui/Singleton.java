/*
 * Copyright (C) 2017 Orion Technology Co., Ltd. All Rights Reserved.
 */
package us.zoom.sdksample.ui;

public abstract class Singleton<T> {
    private volatile T mInstance;

    protected abstract T create();

    public final T get() {
        if (mInstance == null) {
            synchronized (this) {
                if (mInstance == null) {
                    mInstance = create();
                }
            }
        }
        return mInstance;
    }
}
