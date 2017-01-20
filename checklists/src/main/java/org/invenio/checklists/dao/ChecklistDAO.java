package org.invenio.checklists.dao;

import java.util.List;
import org.invenio.checklists.orm.Checklist;

/**
 *
 * @author avillalobos
 */
public interface ChecklistDAO {
    
    public Checklist save(Checklist checklist);
    public Checklist get(String id);
    public List<Checklist> getByOwner(String userId);
    public List<Checklist> get();
    public Checklist update(Checklist checklist);
    public void delete(Checklist checklist);

}
