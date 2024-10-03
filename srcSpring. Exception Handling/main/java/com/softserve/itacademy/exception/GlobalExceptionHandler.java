package com.softserve.itacademy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Видалимо один з обробників для EntityNotFoundException для уникнення неоднозначності
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error/not_found_error");
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorMessage", "Page not found");
        return "404-page";
    }

    @ExceptionHandler(NullEntityReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNullEntityReferenceException(NullEntityReferenceException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "400-page";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "500-page";
    }
}
