package org.invenio.checklists.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hibernate.Hibernate;
import org.invenio.checklists.orm.Checklist;
import org.invenio.checklists.orm.ChkUser;

/**
 * ChkUser serializer.
 * 
 * @author avillalobos
 */
public class ChkUserSerializer extends Serializer {

    @Override
    protected void addSimple(Object object, JsonObject json) {
        
        ChkUser user = (ChkUser) object;
        json.addProperty("id", user.getId());
        json.addProperty("email", user.getEmail());
        
    }

    @Override
    protected void addFull(Object object, JsonObject json) {
        
        ChkUser user = (ChkUser) object;
        json.addProperty("firstName", user.getFirstName());
        json.addProperty("lastName", user.getLastName());
        json.addProperty("company", user.getCompany());
        json.addProperty("dateCreated", sdf.format(user.getDateCreated()));
        json.addProperty("dateModified", sdf.format(user.getDateModified()));
    }

    @Override
    protected void addCollections(Object object, JsonObject json) {
        
        ChkUser user = (ChkUser) object;
        
        if (user.getChecklists() != null && Hibernate.isInitialized(user.getChecklists())) {
            
            JsonArray jsonChecklists = new JsonArray();
            
            for (Checklist checklist : user.getChecklists()) {
                
                JsonObject jsonChecklist = new JsonObject();
                
                ChecklistSerializer checklistSerializer = new ChecklistSerializer();
                checklistSerializer.addSimple(checklist, jsonChecklist);
                
                jsonChecklists.add(jsonChecklist);
            }
            
            json.add("checklists", jsonChecklists);
        }
    }

}
