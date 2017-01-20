package org.invenio.checklists.service;

import java.util.List;
import org.invenio.checklists.orm.Checklist;
import org.invenio.checklists.orm.ChkUser;

/**
 *
 * @author avillalobos
 */
public interface ChecklistService {
    
    /**
     * Validates a checklist before save.
     * 
     * @param checklist
     * @throws Exception
     */
    public void validate(Checklist checklist) throws Exception;
    
    /**
     * Saves a checklist to the database. Makes a call to validate().
     * 
     * @param loggedUser
     * @param checklist
     * @return
     * @throws Exception 
     */
    public Checklist save(ChkUser loggedUser, Checklist checklist) throws Exception;
    
    /**
     * Gets a checklist by its id. Returns null if not found.
     * 
     * @param id
     * @return 
     * @throws java.lang.Exception 
     */
    public Checklist get(String id) throws Exception;
    
    /**
     * Gets a checklist by its creator. Returns empty list if not found.
     * 
     * @param userId
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Checklist> getByOwner(String userId) throws Exception;
    
    /**
     * Return the list of all the checklists in the database.
     * 
     * @return 
     */
    public List<Checklist> get();
    
    /**
     * Updates an existing checklist.
     * 
     * @param id
     * @param checklist
     * @return 
     * @throws java.lang.Exception 
     */
    public Checklist update(String id, Checklist checklist) throws Exception;
    
    /**
     * Deletes a checklist from the database.
     * 
     * @param id 
     * @throws java.lang.Exception 
     */
    public void delete(String id) throws Exception;

}
