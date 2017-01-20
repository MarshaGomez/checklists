package org.invenio.checklists.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.invenio.checklists.commons.SecurityConstants;
import org.invenio.checklists.orm.ChkUser;
import org.invenio.checklists.service.ChkUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Class checks if the token is valid or not, if so, the user may continue, if
 * is not valid returns an error.
 * 
 * @author avillalobos
 */
public class RestTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    
    /** Logger instance. */
    private final static Logger log = LoggerFactory.getLogger(RestTokenAuthenticationFilter.class);
    
    @Autowired
    private ChkUserService userService;
    
    private List<String> nonSecurePaths = new ArrayList<>();
    
    public RestTokenAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/**"));
    }
    
    /**
     * Returns true if we need the path to be secure.
     * 
     * @param pathInfo
     * @return 
     */
    private boolean isSecurePathInfo(String pathInfo) {
        
        if (StringUtils.isNotBlank(pathInfo)) {
            
            return !nonSecurePaths.contains(pathInfo);
            
        }

        return false;
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
    
    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        
        String pathInfo = request.getPathInfo();
        
        boolean isSecurePath = isSecurePathInfo(pathInfo);
        
        /*
        If the path is secure we need to make the call explicitly to the authenticate
        method, then return false.
        This will enforce authentication and will allow the application con continue
        with the corresponding flow to the web-service.
        */
        if (isSecurePath) {
           
            try {
                
                attemptAuthentication(request, response);

                return false;
                
            } catch (AuthenticationException ex) {
                
                try {
                    
                    /*
                    Since it failed to authenticate we need to process the error. 
                    This forwards the request to the RestAuthenticationFailureHandler.
                    */
                    unsuccessfulAuthentication(request, response, ex);
                    
                } catch (IOException | ServletException error) {
                    log.error("Unable to send the error back to the user", error);
                    return true;
                }
                
            } catch (IOException | ServletException error) {
                log.error("Unable to send the error back to the user", error);
                return true;
            }
            
        }
        
        return isSecurePath;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER_NAME);
        
        //Token must always be present.
        if (StringUtils.isBlank(token)) {
            String msg = "Trying to access the API without a token";
            log.error(msg);
            throw new AuthenticationCredentialsNotFoundException(msg);
        }
        
        try {
            
            ChkUser user = userService.getByToken(token);
            
            if (user == null) {
                log.error("There is no user associated to token {}", token);
                throw new Exception("Invalid token");
            }
            
            if (user.getTokenExpired()) {
                log.error("User token is expired {}", user.toString());
                throw new Exception("Token is expired, please log in");
            }
            
            Authentication authentication = new RunAsUserToken(token, user, null, null, null);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            //Update user's last access time.
            userService.touch(user.getId());
            
            return authentication;
            
        } catch (Exception ex) {
            log.error("Error authenticating user {}", ex.getMessage(), ex);
            throw new AuthenticationException(ex.getMessage()) {};
        }
        
    }

    public void setNonSecurePaths(List<String> nonSecurePaths) {
        this.nonSecurePaths = nonSecurePaths;
    }

}
