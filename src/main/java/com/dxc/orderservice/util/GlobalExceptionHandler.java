package com.dxc.orderservice.util;

import com.dxc.orderservice.responses.ExceptionResponseWrapper;
import com.dxc.orderservice.util.constants.ErrorCodes;
import com.dxc.orderservice.responses.Status;
import com.dxc.orderservice.util.exceptions.EntityNotFoundException;
import com.dxc.orderservice.util.exceptions.MissingParameterException;
import com.dxc.orderservice.util.exceptions.ExceedsTotalException;
import com.dxc.orderservice.util.exceptions.AmbiguousRequestException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public final class GlobalExceptionHandler {

    public static final String ERROR_TYPE = "error";
    private final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseWrapper>
    handleEntityNotFoundException(final EntityNotFoundException ex) {
        logger.info(ex.getMessage());
        Status status = new Status(
                ErrorCodes.ENTITY_NOT_FOUND, ex.getMessage(), ERROR_TYPE);
        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ExceptionResponseWrapper>
    handleOrderTypeMismatchException(final TypeMismatchException ex) {
        logger.info(ex.getMessage());
        String message = ex.getValue() + " is not a valid value, "
                + "the property value must be " + ex.getRequiredType();

        Status status = new Status(
                ErrorCodes.TYPE_MISMATCH, message, ERROR_TYPE);

        if (((String) ex.getValue()).isEmpty()) {
            status = new Status(ErrorCodes.EMPTY_REQUEST_PARAM,
                    "The request parameter cannot be empty", ERROR_TYPE);
        }

        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<ExceptionResponseWrapper>
    handleMissingParameterException(final MissingParameterException ex) {
        logger.info(" [" + ex.getErrorCode() + "] -" + ex.getMessage());

        Status status = new Status(ex);

        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ExceedsTotalException.class)
    public ResponseEntity<ExceptionResponseWrapper>
    handleExceedsTotalException(final ExceedsTotalException ex) {
        logger.info(" [" + ex.getErrorCode() + "] -" + ex.getMessage());

        Status status = new Status(ex);

        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AmbiguousRequestException.class)
    public ResponseEntity<ExceptionResponseWrapper>
    handleTwoParameterException(final AmbiguousRequestException ex) {
        logger.info(ex.getMessage());

        Status status = new Status(ex);

        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseWrapper>
    handleBodyValidationException(
            final MethodArgumentNotValidException ex
    ) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        logger.info("[" + ErrorCodes.INVALID_BODY + "] - " + errors);

        Status status = new Status(
                ErrorCodes.INVALID_BODY,
                String.join(" and ", errors),
                ERROR_TYPE
        );

        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NoHandlerFoundException.class,
    HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ExceptionResponseWrapper>
    handleNoPathFoundException(
            final Exception ex,
            final HttpServletRequest request
    ) {
        logger.error("[" + ErrorCodes.INVALID_PATH + "] - " + ex.getMessage());

        Status status = new Status(
                ErrorCodes.INVALID_PATH,
                request.getMethod() + " " + request.getRequestURI()
                        + " is not a valid path",
                ERROR_TYPE
        );

        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseWrapper>
    handleOtherExceptions(
            final Exception ex
    ) {
        logger.error("[" + ErrorCodes.GENERAL_EXCEPTION + "] - "
                + ex.getMessage());

        Status status = new Status(
                ErrorCodes.GENERAL_EXCEPTION,
                "An internal server error has occurred. Please try again",
                ERROR_TYPE
        );

        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseWrapper>
    handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException ex
    ) {
        logger.info("[" + ErrorCodes.INVALID_BODY + "] - " + ex.getMessage());

        Status status = new Status(
                ErrorCodes.INVALID_BODY,
                "Malformed JSON body. Please check your JSON object is valid"
                        + " and the properties have the correct types",
                ERROR_TYPE
        );

        return new ResponseEntity<>(
                new ExceptionResponseWrapper(status),
                HttpStatus.BAD_REQUEST
        );
    }

}
