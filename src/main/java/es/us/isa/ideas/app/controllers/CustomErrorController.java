/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author isa-tecnico-01-asm
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView result = null;
        
        Integer statusCode = Integer.valueOf(status.toString());

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            result = new ModelAndView("misc/404");
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            result = new ModelAndView("misc/500");
        } else {
            result = new ModelAndView("misc/panic");
        }
        
        return result;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
