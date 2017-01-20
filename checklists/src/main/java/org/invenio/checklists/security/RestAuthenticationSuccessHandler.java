package org.invenio.checklists.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.invenio.checklists.orm.ChkUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author avillalobos
 */
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    /** Logger instance. */
    private final static Logger log = LoggerFactory.getLogger(RestAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.debug("User has {} been authenticated", ((ChkUser) authentication.getPrincipal()).getEmail());
    }

}
