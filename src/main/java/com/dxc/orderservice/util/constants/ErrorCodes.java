package com.dxc.orderservice.util.constants;

public final class ErrorCodes {

    // Private constructor to prevent instantiation
    private ErrorCodes() {
    }
    public static final int ENTITY_NOT_FOUND = 3001;
    public static final int TYPE_MISMATCH = 3002;
    public static final int MISSING_REQUEST_PARAM = 3003;
    public static final int EMPTY_REQUEST_PARAM = 3004;
    public static final int INVALID_BODY = 3005;
    public static final int AMBIGUOUS_REQUEST = 3007;
    public static final int INVALID_PATH = 3008;
    public static final int EXCEEDS_QUANTITY = 3009;
    public static final int EXCEEDS_TOTAL = 3010;
    public static final int GENERAL_EXCEPTION = 3999;


}
