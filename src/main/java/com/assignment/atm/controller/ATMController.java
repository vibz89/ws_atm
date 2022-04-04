package com.assignment.atm.controller;

import com.assignment.atm.exception.ATMException;
import com.assignment.atm.exception.ErrorResponse;
import com.assignment.atm.model.UserRequest;
import com.assignment.atm.service.IATMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


@Slf4j
@RestController
@RequestMapping("/atm/api/v1")
public class ATMController {

    @Autowired
    private IATMService atmService;

    @RequestMapping(value = "/checkBalance/{account}/{pin}", method = RequestMethod.GET)
    public ResponseEntity<String> checkBalance(@PathVariable String account, @PathVariable String pin) {
        String response = atmService.checkBalance(account, pin);
        return new ResponseEntity<String>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<String> withdraw(@RequestBody UserRequest userReq) {
        String response = atmService.withdraw(userReq);
        return new ResponseEntity<String>(response, new HttpHeaders(), HttpStatus.OK);
    }

    //Exception Handling

    @ExceptionHandler(ATMException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponse> handleATMException(
            ATMException exception,
            WebRequest request
    ) {
        log.error("Failed to find the requested element", exception);
        return buildErrorResponse(exception, HttpStatus.SERVICE_UNAVAILABLE, request);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {
        log.error("Unknown error occurred", exception);
        return buildErrorResponse(
                exception,
                "Unknown error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus,
                request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                exception.getMessage()
        );


        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

}
