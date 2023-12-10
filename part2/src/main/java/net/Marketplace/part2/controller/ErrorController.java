package net.Marketplace.part2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom Error Controller handling error pages.
 */
@Component
public class ErrorController extends BasicErrorController {
    /**
     * Constructor for ErrorController.
     *
     * @param errorAttributes  The error attributes.
     * @param serverProperties The server properties.
     */
    public ErrorController(
            ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes, serverProperties.getError());
    }
    /**
     * Handles error requests.
     *
     * @param request The HTTP request.
     * @return Redirect to error page.
     */
    @RequestMapping("/error")
    public String handlingError(HttpServletRequest request){
        return "error";
    }
}
