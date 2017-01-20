package org.invenio.checklists.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.invenio.checklists.orm.Checklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class ChecklistDAOImpl extends BaseDAO implements ChecklistDAO {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(ChecklistDAOImpl.class);

    @Override
    public Checklist save(Checklist checklist) {
        
        try {
            
            Session session = getSession();
            
            session.save(checklist);
            
            return checklist;
            
        } catch (HibernateException ex) {
            log.error("Error saving checklist {}. {}", checklist.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public Checklist get(String id) {
        
        try {
            
            Session session = getSession();
            
            Criteria criteria = session.createCriteria(Checklist.class, "checklist")
                    .createAlias("checklist.tasks", "task", JoinType.LEFT_OUTER_JOIN)
                    .add(Restrictions.eq("checklist.id", id));
            
            Checklist checklist = (Checklist) criteria.uniqueResult();
            
            return checklist;
            
        } catch (HibernateException ex) {
            log.error("Error getting checklist {}. {}", id, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<Checklist> getByOwner(String userId) {
        
        try {
            
            Session session = getSession();
            
            Criteria criteria = session.createCriteria(Checklist.class, "checklist")
                    .add(Restrictions.eq("checklist.createdByUser.id", userId))
                    .addOrder(Order.asc("checklist.name"));
            
            List<Checklist> checklists = (List<Checklist>) criteria.list();
            
            return checklists;
            
        } catch (HibernateException ex) {
            log.error("Error getting checklists for user {}. {}", userId, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<Checklist> get() {
        
        try {
            
            Session session = getSession();
            
            Criteria criteria = session.createCriteria(Checklist.class, "checklist")
                    .addOrder(Order.asc("checklist.name"));
            
            List<Checklist> checklists = (List<Checklist>) criteria.list();
            
            return checklists;
            
        } catch (HibernateException ex) {
            log.error("Error getting checklists. {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public Checklist update(Checklist checklist) {
        
        try {
            
            Session session = getSession();
            
            session.update(checklist);
            
            return checklist;
            
        } catch (HibernateException ex) {
            log.error("Error updating checklist {}. {}", checklist.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void delete(Checklist checklist) {
        
        try {
            
            Session session = getSession();
            
            session.delete(checklist);
            
        } catch (HibernateException ex) {
             log.error("Error deleting checklist {}. {}", checklist.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

}
