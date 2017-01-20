package org.invenio.checklists.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hibernate.Hibernate;
import org.invenio.checklists.orm.Checklist;
import org.invenio.checklists.orm.Task;

/**
 * Checklist serializer.
 * 
 * @author avillalobos
 */
public class ChecklistSerializer extends Serializer {

    @Override
    protected void addSimple(Object object, JsonObject json) {
        
        Checklist checklist = (Checklist) object;
        json.addProperty("id", checklist.getId());
        json.addProperty("name", checklist.getName());
    }

    @Override
    protected void addFull(Object object, JsonObject json) {
        
        Checklist checklist = (Checklist) object;
        json.addProperty("dateCreated", sdf.format(checklist.getDateCreated()));
        json.addProperty("dateModified", sdf.format(checklist.getDateModified()));
        
        if (checklist.getCreatedByUser() != null && Hibernate.isInitialized(checklist.getCreatedByUser())) {
            ChkUserSerializer userSerializer = new ChkUserSerializer();
            JsonObject jsonUser = new JsonObject();
            
            userSerializer.addSimple(checklist.getCreatedByUser(), jsonUser);
            json.add("createdByUser", jsonUser);
        }
    }

    @Override
    protected void addCollections(Object object, JsonObject json) {
        
        Checklist checklist = (Checklist) object;
        
        if (checklist.getTasks() != null && Hibernate.isInitialized(checklist.getTasks())) {
            
            JsonArray jsonTasks = new JsonArray();
            
            for (Task task : checklist.getTasks()) {
                
                JsonObject jsonTask = new JsonObject();
                
                TaskSerializer taskSerializer = new TaskSerializer();
                taskSerializer.addSimple(task, jsonTask);
                
                jsonTasks.add(jsonTask);
            }
            
            json.add("tasks", jsonTasks);
        }
    }

}
