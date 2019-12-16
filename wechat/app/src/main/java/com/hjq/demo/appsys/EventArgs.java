package com.hjq.demo.appsys;


public class EventArgs<T>{
    private T mObject;

    private EventType mEventType;

    private int mEventHashCode;

    public T getObject() {
        return mObject;
    }

    public void setObject(T object) {
        this.mObject = object;
    }

    public EventType getEventType() {
        return this.mEventType;
    }

    public void setEventType(EventType eventType) {
        this.mEventType = eventType;
    }

    public int getEventHashCode() {
        return mEventHashCode;
    }

    public void setEventHashCode(int eventHashCode) {
        this.mEventHashCode = eventHashCode;
    }
}

