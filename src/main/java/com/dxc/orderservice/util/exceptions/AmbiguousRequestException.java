package com.dxc.orderservice.util.exceptions;
import com.dxc.orderservice.util.constants.ErrorCodes;

public final class AmbiguousRequestException extends ServiceException {
    public AmbiguousRequestException(final String message) {
        super(ErrorCodes.AMBIGUOUS_REQUEST, message);
    }

}
