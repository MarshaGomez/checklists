package org.invenio.checklists.api.remote;

import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.invenio.checklists.json.TaskSerializer;
import org.invenio.checklists.orm.Task;
import org.invenio.checklists.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author avillalobos
 */
@Controller
@Path("tasks")
public class TaskAPI {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(TaskAPI.class);
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private Gson gson;
    
    @POST
    @Path("checklists/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String save(@PathParam("id") String checklistId, String taskJson) {
        
        if (StringUtils.isBlank(taskJson)) {
            log.error("Task information is missing");
            return null;
        }
        
        Task task = gson.fromJson(taskJson, Task.class);
        
        try {
            
            taskService.save(checklistId, task);
            
            String json = new TaskSerializer()
                    .addSimple()
                    .addFull()
                    .serialize(task);
            
            return json;
            
        } catch (Exception ex) {
            log.error("Error saving task {}. {}", task.toString(), ex.getMessage(), ex);
            return gson.toJson("Error saving task");
        }
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") String id) {
        
        try {
            
            Task task = taskService.get(id);

            if (task == null) {
                return null;
            }
            
            String json = new TaskSerializer()
                    .addSimple()
                    .addFull()
                    .addCollections()
                    .serialize(task);
            
            return json;
            
        } catch (Exception ex) {
            log.error("Error getting task {}. {}", id, ex.getMessage(), ex);
        }
        
        return null;
        
    }
    
    @GET
    @Path("checklists/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByChecklist(@PathParam("id") String checklistId) {
        
        
        try {
            
            List<Task> tasks = taskService.getByChecklist(checklistId);
            
            String json = new TaskSerializer()
                    .addSimple()
                    .addFull()
                    .addCollections()
                    .serializeCollection(tasks);
            
            return json;
            
        } catch (Exception ex) {
            log.error("Error getting tasks for {}. {}", checklistId, ex.getMessage(), ex);
        }
        
        return null;
        
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@PathParam("id") String id) {
        
        try {
            
            taskService.delete(id);
            
        } catch (Exception ex) {
            log.error("Error getting task {}. {}", id, ex.getMessage(), ex);
        }
        
        return "deleted";
        
    }
    
    @PUT
    @Path("complete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String complete(@PathParam("id") String taskId, @DefaultValue("true") @QueryParam("completed") boolean completed) {
        
        try {
            
            taskService.complete(taskId, completed);
            
            return "completed";
            
        } catch (Exception ex) {
            log.error("Error completing task {} as {}. {}", taskId, completed, ex.getMessage(), ex);
        }
        
        return null;
    }

}
