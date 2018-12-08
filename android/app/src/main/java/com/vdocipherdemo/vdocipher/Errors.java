package com.vdocipherdemo.vdocipher;

public class Errors {

    public final static String UNSUPPORTED_SDK_ERROR = "UnsupportedSDKError";
    public final static String UNSUPPORTED_SDK_ERROR_MESSAGE = "Minimum api level required is 21";

    public static final String OPTIONS_NOT_RECEIVED = "OptionsNotReceivedError";
    
    public static final String NO_AVAILABLE_TRACK_ERROR = "NoAvailableTrackError";
    public static final String NO_AVAILABLE_TRACK_MESSAGE = "No downloadable track in available";
    public static final String JSON_EXCEPTION = "JsonException";
    public static final String EXTERNAL_STORAGE_UNAVAILABLE_ERROR = "ExternalStorageUnavailableError";
    public static final String EXTERNAL_STORAGE_UNAVAILABLE_MESSAGE = "External storage is not available";

    public static final String CREATE_DIRECTORY_ERROR = "CreateDirectoryError";
    public static final String CREATE_DIRECTORY_MESSAGE = "failed to create storage directory";

    public static final String ENQUEUE_FAILED_ERROR = "EnqueueFailedError";
    public static final String ENQUEUE_FAILED_MESSAGE = "error enqueuing download request";

}
