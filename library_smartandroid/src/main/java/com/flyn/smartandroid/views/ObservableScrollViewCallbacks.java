package com.flyn.smartandroid.views;

/**
 * Created by flyn on 2014-12-16.
 */
public interface ObservableScrollViewCallbacks
{
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    public void onDownMotionEvent();

    public void onUpOrCancelMotionEvent(ScrollState scrollState);
}
