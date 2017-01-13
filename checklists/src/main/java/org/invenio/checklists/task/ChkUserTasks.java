package org.invenio.checklists.task;

import java.util.List;
import org.invenio.checklists.orm.ChkUser;
import org.invenio.checklists.service.ChkUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author avillalobos
 */
@Component
public class ChkUserTasks {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(ChkUserTasks.class);
    
    @Autowired
    private ChkUserService userService;
    
    
    
    /**
     * Expires the user token which last access date was longer than configure.
     */
    @Scheduled(cron = "${user.token.expiration.cron}")
    public void expireUsersToken() {
        
        //Get the list of all users.
        List<ChkUser> users = userService.get();
        
        for (ChkUser user : users) {
            
            userService.expireUserToken(user);
            
        }
        
    }

}
