package com.dxc.orderservice.util.exceptions;

import com.dxc.orderservice.util.constants.ErrorCodes;

public final class ExceedsTotalException extends ServiceException {
    public ExceedsTotalException(final String message) {
        super(ErrorCodes.EXCEEDS_TOTAL, message);
    }
}
