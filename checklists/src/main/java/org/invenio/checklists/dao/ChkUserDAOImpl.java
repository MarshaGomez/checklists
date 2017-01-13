package org.invenio.checklists.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.invenio.checklists.orm.ChkUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class ChkUserDAOImpl extends BaseDAO implements ChkUserDAO {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(ChkUserDAOImpl.class);

    @Override
    public ChkUser save(ChkUser user) {
        
        try {
            
            Session session = getSession();
            
            session.save(user);
            
            return user;
            
        } catch (HibernateException ex) {
            log.error("Error saving user {}. {}", user.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public ChkUser get(String id) {
        
        try {
            
            Session session = getSession();
            
            ChkUser user = (ChkUser) session.get(ChkUser.class, id);
            
            return user;
            
        } catch (HibernateException ex) {
            log.error("Error getting user {}. {}", id, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<ChkUser> get() {
        
        try {
            
            Session session = getSession();
            
            Criteria criteria = session.createCriteria(ChkUser.class, "user");
            
            List<ChkUser> users = (List<ChkUser>) criteria.list();
            
            return users;
            
        } catch (HibernateException ex) {
            log.error("Error getting users. {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public ChkUser update(ChkUser user) {
        
        try {
            
            Session session = getSession();
            
            session.update(user);
            
            return user;
            
        } catch (HibernateException ex) {
            log.error("Error updating user {}. {}", user.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void delete(ChkUser user) {
        
        try {
            
            Session session = getSession();
            
            session.delete(user);
            
        } catch (HibernateException ex) {
             log.error("Error deleting user {}. {}", user.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public ChkUser getByEmail(String email) {
        
        try {
            
            Session session = getSession();
            
            Criteria criteria = session.createCriteria(ChkUser.class, "user")
                    .add(Restrictions.eq("user.email", email));
            
            ChkUser user = (ChkUser) criteria.uniqueResult();
            
            return user;
            
        } catch (HibernateException ex) {
            log.error("Error getting user by email {}. {}", email, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public ChkUser getByToken(String token) {
        
        try {
            
            Session session = getSession();
            
            Criteria criteria = session.createCriteria(ChkUser.class, "user")
                    .add(Restrictions.eq("user.token", token));
            
            ChkUser user = (ChkUser) criteria.uniqueResult();
            
            return user;
            
        } catch (HibernateException ex) {
            log.error("Error getting user by token. {}", ex.getMessage(), ex);
            throw ex;
        }
    }

}
