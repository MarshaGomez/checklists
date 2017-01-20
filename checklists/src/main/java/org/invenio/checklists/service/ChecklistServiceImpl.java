package org.invenio.checklists.service;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.invenio.checklists.dao.ChecklistDAO;
import org.invenio.checklists.orm.Checklist;
import org.invenio.checklists.orm.ChkUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author avillalobos
 */
@Service
public class ChecklistServiceImpl implements ChecklistService {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(ChecklistServiceImpl.class);
    
    @Autowired
    private ChecklistDAO checklistDAO;

    @Override
    public void validate(Checklist checklist) throws Exception {
         
        boolean isValid = true;
        
        if (StringUtils.isBlank(checklist.getName())) {
            log.error("Checklist name is empty {}", checklist.toString());
            isValid = false;
        }
        
        if (!isValid) {
            throw new Exception("Checklist is not valid");
        }
    }

    @Override
    public Checklist save(ChkUser loggedUser, Checklist checklist) throws Exception {
        
        validate(checklist);
        
        Date date = new Date();
        
        //Add dates.
        checklist.setDateCreated(date);
        checklist.setDateModified(date);
        checklist.setCreatedByUser(loggedUser);
        
        checklistDAO.save(checklist);
        
        return checklist;
    }

    @Override
    public Checklist get(String id) throws Exception {
        
        if (StringUtils.isBlank(id)) {
            log.error("Can not get a checklist by id without an id");
            throw new Exception("Missing checklistId");
        }
        
        Checklist checklist = checklistDAO.get(id);
        
        return checklist;
    }

    @Override
    public List<Checklist> getByOwner(String userId) throws Exception {
        
        if (StringUtils.isBlank(userId)) {
            log.error("Can not get a checklist by user id without a user id");
            throw new Exception("Missing userId");
        }
        
        List<Checklist> checklists = checklistDAO.getByOwner(userId);
        
        return checklists;
    }

    @Override
    public List<Checklist> get() {
        
        List<Checklist> checklists = checklistDAO.get();
        
        return checklists;
    }

    @Override
    public Checklist update(String id, Checklist checklist) throws Exception {
        
        Checklist existingChecklist = get(id);
        
        //Updateble fields.
        existingChecklist.setName(StringUtils.isNotBlank(checklist.getName()) ? checklist.getName() : existingChecklist.getName());
        existingChecklist.setDateModified(new Date());
        
        checklistDAO.update(existingChecklist);
        
        return existingChecklist;
    }

    @Override
    public void delete(String id) throws Exception {
        
        if (StringUtils.isBlank(id)) {
            log.error("Can not delete a checklist without an id");
        }
        
        Checklist checklist = get(id);
        
        if (checklist == null) {
            log.warn("Trying to delete a non-existing checklist {}", id);
            return;
        }
        
        checklistDAO.delete(checklist);
    }

}
