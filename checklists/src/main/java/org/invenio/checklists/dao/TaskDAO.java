package org.invenio.checklists.dao;

import java.util.List;
import org.invenio.checklists.orm.Task;

/**
 *
 * @author avillalobos
 */
public interface TaskDAO {
    
    public Task save(Task task);
    public Task get(String id);
    public List<Task> getByChecklist(String checklistId);
    public Task update(Task task);
    public void delete(Task task);

}
