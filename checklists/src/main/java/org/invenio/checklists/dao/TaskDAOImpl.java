package org.invenio.checklists.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.invenio.checklists.orm.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class TaskDAOImpl extends BaseDAO implements TaskDAO {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(TaskDAOImpl.class);

    @Override
    public Task save(Task task) {
        
        try {
            
            Session session = getSession();
            
            session.save(task);
            
            return task;
            
        } catch (HibernateException ex) {
            log.error("Error saving task {}. {}", task.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public Task get(String id) {
        
        try {
            
            Session session = getSession();
            
            Task task = (Task) session.get(Task.class, id);
            
            return task;
            
        } catch (HibernateException ex) {
            log.error("Error getting task {}. {}", id, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<Task> getByChecklist(String checklistId) {
        
        try {
            
            Session session = getSession();
            
            Criteria criteria = session.createCriteria(Task.class, "task")
                    .add(Restrictions.eq("task.checklist.id", checklistId))
                    .addOrder(Order.asc("checklist.name"));
            
            List<Task> tasks = (List<Task>) criteria.list();
            
            return tasks;
            
        } catch (HibernateException ex) {
            log.error("Error getting tasks for checklist {}. {}", checklistId, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public Task update(Task task) {
        
        try {
            
            Session session = getSession();
            
            session.update(task);
            
            return task;
            
        } catch (HibernateException ex) {
            log.error("Error updating task {}. {}", task.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void delete(Task task) {
        
        try {
            
            Session session = getSession();
            
            session.delete(task);
            
        } catch (HibernateException ex) {
             log.error("Error deleting task {}. {}", task.toString(), ex.getMessage(), ex);
            throw ex;
        }
    }

}
