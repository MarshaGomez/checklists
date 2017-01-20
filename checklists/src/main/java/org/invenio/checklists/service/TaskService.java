package org.invenio.checklists.service;

import java.util.List;
import org.invenio.checklists.orm.Task;

/**
 *
 * @author avillalobos
 */
public interface TaskService {
    
    /**
     * Validates a task before save.
     * 
     * @param task
     * @throws Exception
     */
    public void validate(Task task) throws Exception;
    
    /**
     * Saves a task to the database. Makes a call to validate().
     * 
     * @param checklistId
     * @param task
     * @return
     * @throws Exception 
     */
    public Task save(String checklistId, Task task) throws Exception;
    
    /**
     * Gets a task by its id. Returns null if not found.
     * 
     * @param id
     * @return 
     * @throws java.lang.Exception 
     */
    public Task get(String id) throws Exception;
    
    /**
     * Gets a task by its creator. Returns empty list if not found.
     * 
     * @param checklistId
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Task> getByChecklist(String checklistId) throws Exception;
    
    /**
     * Updates an existing task.
     * 
     * @param id
     * @param task
     * @return 
     * @throws java.lang.Exception 
     */
    public Task update(String id, Task task) throws Exception;
    
    /**
     * Deletes a task from the database.
     * 
     * @param id 
     * @throws java.lang.Exception 
     */
    public void delete(String id) throws Exception;
    
    /**
     * Marks a task as completed or not completed.
     * 
     * @param id
     * @param completed
     * @throws Exception 
     */
    public void complete(String id, boolean completed) throws Exception;

}
