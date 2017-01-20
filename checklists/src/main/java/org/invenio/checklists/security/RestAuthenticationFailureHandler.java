package org.invenio.checklists.security;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Handles the request if there is an error during the token validation.
 * 
 * @author avillalobos
 */
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
    
    /** Logger instance. */
    private final static Logger log = LoggerFactory.getLogger(RestAuthenticationFailureHandler.class);

     @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {

        log.error("User authentication failure");

        response.setContentType("application/json;charset=UTF-8");

        try (OutputStream out = response.getOutputStream()) {
            
            JsonObject json = new JsonObject();
            json.addProperty("message", ex.getMessage());

            String jsonString = json.toString();
            out.write(jsonString.getBytes());
        }
    }

}
