package org.invenio.checklists.api.remote;

import com.google.gson.Gson;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.invenio.checklists.json.ChecklistSerializer;
import org.invenio.checklists.orm.Checklist;
import org.invenio.checklists.orm.ChkUser;
import org.invenio.checklists.service.ChecklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

/**
 *
 * @author avillalobos
 */
@Controller
@Path("checklists")
public class ChecklistAPI {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(ChecklistAPI.class);
    
    @Autowired
    private ChecklistService checklistService;
    
    @Autowired
    private Gson gson;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String save(String checklistJson) {
        
        ChkUser loggedUser = (ChkUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (StringUtils.isBlank(checklistJson)) {
            log.error("Checklist information is missing");
            return null;
        }
        
        Checklist checklist = gson.fromJson(checklistJson, Checklist.class);
        
        try {
            
            checklistService.save(loggedUser, checklist);
            
            String json = new ChecklistSerializer()
                    .addSimple()
                    .addFull()
                    .serialize(checklist);
            
            return json;
            
        } catch (Exception ex) {
            log.error("Error saving checklist {}. {}", checklist.toString(), ex.getMessage(), ex);
            return gson.toJson("Error saving checklist");
        }
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") String id) {
        
        try {
            
            Checklist checklist = checklistService.get(id);

            if (checklist == null) {
                return null;
            }
            
            String json = new ChecklistSerializer()
                    .addSimple()
                    .addFull()
                    .addCollections()
                    .serialize(checklist);
            
            return json;
            
        } catch (Exception ex) {
            log.error("Error getting checklist {}. {}", id, ex.getMessage(), ex);
        }
        
        return null;
        
    }
    
    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByOwner(@DefaultValue("true") @QueryParam("owner") boolean owner) {
        
        ChkUser loggedUser = (ChkUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        try {
            
            List<Checklist> checklists = Collections.EMPTY_LIST;
            
            if (owner) {
                
                checklists = checklistService.getByOwner(loggedUser.getId());
                
            } else {
                throw new UnsupportedOperationException("Currently collaboration is not supported.");
            }
            
            
            String json = new ChecklistSerializer()
                    .addSimple()
                    .addFull()
                    .addCollections()
                    .serializeCollection(checklists);
            
            return json;
            
        } catch (Exception ex) {
            log.error("Error getting checklists for {}. {}", loggedUser.getEmail(), ex.getMessage(), ex);
        }
        
        return null;
        
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@PathParam("id") String id) {
        
        try {
            
            checklistService.delete(id);
            
        } catch (Exception ex) {
            log.error("Error getting checklist {}. {}", id, ex.getMessage(), ex);
        }
        
        return "deleted";
        
    }

}
