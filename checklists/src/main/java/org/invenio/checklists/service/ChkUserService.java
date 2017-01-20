package org.invenio.checklists.service;

import java.util.List;
import org.invenio.checklists.dto.LoginForm;
import org.invenio.checklists.orm.ChkUser;

/**
 *
 * @author avillalobos
 */
public interface ChkUserService {
    
    /**
     * Validates an user before save.
     * 
     * @param user
     * @throws Exception
     */
    public void validate(ChkUser user) throws Exception;
    
    /**
     * Saves an user to the database. Makes a call to validate().
     * 
     * @param user
     * @return
     * @throws Exception 
     */
    public ChkUser save(ChkUser user) throws Exception;
    
    /**
     * Gets an user by its id. Returns null if not found.
     * 
     * @param id
     * @return 
     * @throws java.lang.Exception 
     */
    public ChkUser get(String id) throws Exception;
    
    /**
     * Gets an user by its email. Returns null if not found.
     * 
     * @param email
     * @return 
     */
    public ChkUser getByEmail(String email);
    
    /**
     * Gets an user by its token. Returns null if not found.
     * 
     * @param token
     * @return 
     * @throws java.lang.Exception 
     */
    public ChkUser getByToken(String token) throws Exception;
    
    /**
     * Return the list of all the users in the database.
     * 
     * @return 
     */
    public List<ChkUser> get();
    
    /**
     * Updates an existing user. Makes a call to validate().
     * 
     * @param id
     * @param user
     * @return 
     * @throws java.lang.Exception 
     */
    public ChkUser update(String id, ChkUser user) throws Exception;
    
    /**
     * Deletes an user from the database.
     * 
     * @param id 
     * @throws java.lang.Exception 
     */
    public void delete(String id) throws Exception;
    
    /**
     * Updates the token last access date of an user.
     * 
     * @param id 
     * @throws java.lang.Exception 
     */
    public void touch(String id) throws Exception;
    
    /**
     * Verifies the user credentials and returns the user token.
     * 
     * @param loginForm
     * @return
     * @throws Exception 
     */
    public String login(LoginForm loginForm) throws Exception;
    
    /**
     * Logs out an user.
     * Removes the token information from the database.
     * 
     * @param id
     * @throws Exception 
     */
    public void logout(String id) throws Exception;
    
    /**
     * Expires a user token.
     * 
     * @param user 
     */
    public void expireUserToken(ChkUser user);

}
