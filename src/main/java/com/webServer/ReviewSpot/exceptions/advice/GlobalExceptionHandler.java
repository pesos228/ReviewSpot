package com.webServer.ReviewSpot.exceptions.advice;

import com.webServer.ReviewSpot.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ClientEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String clientEmailAlreadyExistsException(ClientEmailAlreadyExistsException e){return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String clientNotFoundException(ClientNotFoundException e){ return  e.getMessage();}

    @ResponseBody
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String commentNotFoundException(CommentNotFoundException e){ return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(GenreAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String genreAlreadyExistsException(GenreAlreadyExistsException e){return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(GenreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String genreNotFoundException(GenreNotFoundException e){return  e.getMessage();}

    @ResponseBody
    @ExceptionHandler(MediaAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String mediaAlreadyExistsException(MediaAlreadyExistsException e){ return  e.getMessage();}

    @ResponseBody
    @ExceptionHandler(MediaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String mediaNotFoundException(MediaNotFoundException e){ return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String reviewNotFoundException(ReviewNotFoundException e){ return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(UnknownTargetTypeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String unknownTargetTypeException(UnknownTargetTypeException e){ return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(ClientAlreadyHaveReviewException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String clientAlreadyHaveReviewException(ClientAlreadyHaveReviewException e){ return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(ClientAlreadyHaveReactionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String clientAlreadyHaveReactionException(ClientAlreadyHaveReactionException e){ return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(ClientReactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String clientReactionNotFoundException(ClientReactionNotFoundException e){ return e.getMessage();}

    @ResponseBody
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String roleNotFoundException(RoleNotFoundException e){ return e.getMessage();}

}
