package com.yjt.app.exception;

public class DeviceInfoException extends Throwable {

    private static final long serialVersionUID = 7834459996713041856L;

    private static final String NO_ANDROID_ID = "Could not retrieve a "
            + "device ID";

    public DeviceInfoException(Throwable throwable) {
        super(NO_ANDROID_ID, throwable);
    }

    public DeviceInfoException(String message) {
        super(message);
    }

    public DeviceInfoException() {
        super(NO_ANDROID_ID);
    }

}
