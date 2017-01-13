package org.invenio.checklists.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author avillalobos
 */
@Component
public class BaseDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
