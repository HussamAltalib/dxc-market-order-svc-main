package com.dxc.orderservice.util.exceptions;

import com.dxc.orderservice.util.constants.ErrorCodes;


public final class EntityNotFoundException extends ServiceException {
    public EntityNotFoundException(final String message) {

        super(ErrorCodes.ENTITY_NOT_FOUND, message);
    }
}
