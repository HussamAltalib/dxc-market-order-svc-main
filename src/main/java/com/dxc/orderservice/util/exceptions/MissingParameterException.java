package com.dxc.orderservice.util.exceptions;

import com.dxc.orderservice.util.constants.ErrorCodes;

public final class MissingParameterException extends ServiceException {
    public MissingParameterException(final String message) {
        super(ErrorCodes.MISSING_REQUEST_PARAM, message);
    }
}
