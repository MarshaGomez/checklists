package org.invenio.checklists.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hibernate.Hibernate;
import org.invenio.checklists.orm.Issue;
import org.invenio.checklists.orm.Note;
import org.invenio.checklists.orm.Task;

/**
 * Task serializer.
 * 
 * @author avillalobos
 */
public class TaskSerializer extends Serializer {

    @Override
    protected void addSimple(Object object, JsonObject json) {
        
        Task task = (Task) object;
        json.addProperty("id", task.getId());
        json.addProperty("name", task.getName());
    }

    @Override
    protected void addFull(Object object, JsonObject json) {
        
        Task task = (Task) object;
        json.addProperty("dateCreated", sdf.format(task.getDateCreated()));
        json.addProperty("dateModified", sdf.format(task.getDateModified()));
        
        if (task.getDateExpires() != null) {
            json.addProperty("dateExpires", sdf.format(task.getDateExpires()));
        }
        
        if (task.getDateReminder() != null) {
            json.addProperty("dateReminder", sdf.format(task.getDateReminder()));
        }
        
        json.addProperty("description", task.getDescription());
        json.addProperty("completed", task.isCompleted());
        
        if (task.getChecklist() != null && Hibernate.isInitialized(task.getChecklist())) {
            ChecklistSerializer checklistSerializer = new ChecklistSerializer();
            JsonObject jsonChecklist = new JsonObject();
            
            checklistSerializer.addSimple(task.getChecklist(), jsonChecklist);
            json.add("checklist", jsonChecklist);
        }
    }

    @Override
    protected void addCollections(Object object, JsonObject json) {
        
        Task task = (Task) object;
        
        if (task.getIssues() != null && Hibernate.isInitialized(task.getIssues())) {
            
            JsonArray jsonIssues = new JsonArray();
            
            for (Issue issue : task.getIssues()) {
                
                JsonObject jsonIssue = new JsonObject();
                
                //IssueSerializer issueSerializer = new IssueSerializer();
                //issueSerializer.addSimple(issue, jsonIssue);
                
                jsonIssues.add(jsonIssue);
                
            }
            
            json.add("issues", jsonIssues);
        }
        
        if (task.getNotes() != null && Hibernate.isInitialized(task.getNotes())) {
            
            JsonArray jsonNotes = new JsonArray();
            
            for (Note note : task.getNotes()) {
                
                JsonObject jsonNote = new JsonObject();
                
                //NoteSerializer noteSerializer = new NoteSerializer();
                //noteSerializer.addSimple(note, jsonNote);
                
                jsonNotes.add(jsonNote);
            }
            
            json.add("notes", jsonNotes);
        }
    }

}
