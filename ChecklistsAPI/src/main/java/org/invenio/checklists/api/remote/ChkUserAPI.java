package org.invenio.checklists.api.remote;

import com.google.gson.Gson;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.invenio.checklists.dto.LoginForm;
import org.invenio.checklists.orm.ChkUser;
import org.invenio.checklists.service.ChkUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author avillalobos
 */
@Controller
@Path("users")
public class ChkUserAPI {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(ChkUserAPI.class);

    @Autowired
    private ChkUserService userService;
    
    @Autowired
    private Gson gson;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String save(String userJson) {
        
        if (StringUtils.isBlank(userJson)) {
            log.error("User information is missing");
            return null;
        }
        
        ChkUser user = gson.fromJson(userJson, ChkUser.class);
        
        try {
            
            userService.save(user);
            
            return gson.toJson(user);
            
        } catch (Exception ex) {
            log.error("Error saving user {}. {}", user.toString(), ex.getMessage(), ex);
            return gson.toJson("Error saving user");
        }
    }
    
    @GET
    @Path("{id}")
    public String get(@PathParam("id") String id) {
        
        ChkUser user = userService.get(id);
        
        if (user == null) {
            return null;
        }
        
        return gson.toJson(user);
        
    }
    
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(String loginJson) {
        
        if (StringUtils.isBlank(loginJson)) {
            log.error("Login information is missing");
            return null;
        }
        
        LoginForm login = gson.fromJson(loginJson, LoginForm.class);
        
        try {
            
            String token = userService.login(login);
            
            return token;
            
        } catch (Exception ex) {
            return StringUtils.EMPTY;
        }
        
    }
    
    @GET
    @Path("/{id}/logout")
    @Produces(MediaType.TEXT_PLAIN)
    public String logout(@PathParam("id") String id) {
        
        try {
            
            userService.logout(id);
            
        } catch (Exception ex) {
            return "Error loggin out";
        }
        
        return "logged-out";
    }
}
