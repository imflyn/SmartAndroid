package com.smartandroid.ui;

public interface ControllerListener
{
    <T extends BaseEvent> void onEvent(T event);
}