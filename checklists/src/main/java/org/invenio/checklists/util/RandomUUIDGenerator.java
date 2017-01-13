package org.invenio.checklists.util;

import java.io.Serializable;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

/**
 *
 * @author avillalobos
 */
@Component
public class RandomUUIDGenerator implements IdentifierGenerator {
    
    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        return UUID.randomUUID().toString();
    }
    
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

}
