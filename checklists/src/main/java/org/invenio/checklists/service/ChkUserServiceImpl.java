package org.invenio.checklists.service;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.invenio.checklists.dao.ChkUserDAO;
import org.invenio.checklists.dto.LoginForm;
import org.invenio.checklists.orm.ChkUser;
import org.invenio.checklists.util.ApplicationConfigPropertiesUtils;
import org.invenio.checklists.util.RandomUUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author avillalobos
 */
@Service
public class ChkUserServiceImpl implements ChkUserService {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(ChkUserServiceImpl.class);
    
    @Autowired
    private ChkUserDAO userDAO;
    
    @Autowired
    private RandomUUIDGenerator tokenGenerator;
    
    @Autowired
    private ApplicationConfigPropertiesUtils properties;

    @Override
    public void validate(ChkUser user) throws Exception {
        
        boolean isValid = true;
        
        if (StringUtils.isBlank(user.getEmail())) {
            log.error("User email is empty {}", user.toString());
            isValid = false;
        }
        
        if (StringUtils.isBlank(user.getFirstName())) {
            log.error("User first name is empty {}", user.toString());
            isValid = false;
        }
        
        if (StringUtils.isBlank(user.getLastName())) {
            log.error("User last name is empty {}", user.toString());
            isValid = false;
        }
        
        if (StringUtils.isBlank(user.getPassword())) {
            log.error("User password is empty {}", user.toString());
            isValid = false;
        }
        
        if (!isValid) {
            throw new Exception("User is not valid");
        }
    }

    @Override
    public ChkUser save(ChkUser user) throws Exception {
        
        validate(user);
        
        userDAO.save(user);
        
        return user;
    }

    @Override
    public ChkUser get(String id) {
        
        if (StringUtils.isBlank(id)) {
            log.error("Can not get a user by id without an id");
        }
        
        ChkUser user = userDAO.get(id);
        
        return user;
    }

    @Override
    public List<ChkUser> get() {
        
        List<ChkUser> users = userDAO.get();
        
        return users;
    }

    @Override
    public ChkUser update(ChkUser user) throws Exception {
        
        validate(user);
        
        userDAO.update(user);
        
        return user;
    }

    @Override
    public void delete(String id) {
        
        if (StringUtils.isBlank(id)) {
            log.error("Can not delete a user without an id");
        }
        
        ChkUser user = get(id);
        
        if (user == null) {
            log.warn("Trying to delete a non-existing user {}", id);
            return;
        }
        
        userDAO.delete(user);
    }

    @Override
    public void touch(String id) {
        
        if (StringUtils.isBlank(id)) {
            log.error("Can not touch a user without an id");
        }
        
        ChkUser user = get(id);
        
        if (user == null) {
            log.warn("Trying to touch a non-existing user {}", id);
            return;
        }
        
        //Update the token last access.
        user.setTokenLastAccess(new Date());
        
        try {
            
            update(user);
            
        } catch (Exception ex) {
            log.warn("Error updating the user last access token {}.", id, ex.getMessage());
        }
    }

    @Override
    public ChkUser getByEmail(String email) {
        
        if (StringUtils.isBlank(email)) {
            log.error("Can not get a user by email without an email");
            return null;
        }
        
        ChkUser user = userDAO.getByEmail(email);
        
        return user;
    }

    @Override
    public ChkUser getByToken(String token) throws Exception {
        
        if (StringUtils.isBlank(token)) {
            log.error("Can not get a user by token without a token");
            return null;
        }
        
        ChkUser user = userDAO.getByToken(token);
        
        if (user.getTokenExpired()) {
            log.error("User token is expired {}", user.toString());
            throw new Exception("Token is expired, please log in");
        }
        
        return user;
    }

    @Override
    public String login(LoginForm loginForm) throws Exception {
        
        if (loginForm == null) {
            log.error("Login information is null");
            return null;
        }
        
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();
        
        ChkUser user = getByEmail(email);
        
        if (user == null) {
            log.error("Trying to login a non-existing user {}", email);
            return null;
        }
        
        if (!StringUtils.equals(password, user.getPassword())) {
            log.error("Trying to login a user with an incorrect password {}", email);
            return null;
        }
        
        //Login was successfull.
        user.setTokenExpired(Boolean.FALSE);
        user.setTokenLastAccess(new Date());
        user.setToken(tokenGenerator.generateToken());
        
        update(user);
        
        return user.getToken();
    }

    @Override
    public void logout(String id) throws Exception {
        
        if (StringUtils.isBlank(id)) {
            log.error("Trying to logout an user without an id");
            return;
        }
        
        ChkUser user = get(id);
        
        if (user == null) {
            log.error("Trying to logout a non-existing user {}", id);
            return;
        }
        
        //Remove all token information.
        user.setTokenExpired(null);
        user.setTokenLastAccess(null);
        user.setToken(null);
        
        update(user);
    }

    @Override
    public void expireUserToken(ChkUser user) {
        
        int daysToExpireUnusedTokens = properties.getIntProperty("user.token.expiration.age");
        
        if (user.getTokenLastAccess() != null) {
                
            //Get the token last access and today.
            Date tokenLastAccess = user.getTokenLastAccess();
            Date now = new Date();

            //Calculate the difference in milis.
            long dateDiff = now.getTime() - tokenLastAccess.getTime();

            //Calculate the difference in days.
            long diffDays = dateDiff / (24 * 60 * 60 * 1000);

            //If the difference is greater/equal than configured, expire token.
            if (diffDays >= daysToExpireUnusedTokens) {

                user.setTokenExpired(Boolean.TRUE);

                try {

                    update(user);

                } catch (Exception ex) {
                    log.error("Error expiring token of user {}. {}", user.toString(), ex.getMessage(), ex);
                }
            }
        }
    }

}
