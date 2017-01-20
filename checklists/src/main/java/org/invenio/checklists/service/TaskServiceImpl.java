package org.invenio.checklists.service;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.invenio.checklists.dao.TaskDAO;
import org.invenio.checklists.orm.Checklist;
import org.invenio.checklists.orm.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author avillalobos
 */
@Service
public class TaskServiceImpl implements TaskService {
    
    /** Logger instance. */
    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    
    @Autowired
    private TaskDAO taskDAO;
    
    @Autowired
    private ChecklistService checklistService;

    @Override
    public void validate(Task task) throws Exception {
        
        boolean isValid = true;
        
        if (StringUtils.isBlank(task.getName())) {
            log.error("Task name is empty {}", task.toString());
            isValid = false;
        }
        
        if (!isValid) {
            throw new Exception("Task is not valid");
        }
    }

    @Override
    public Task save(String checklistId, Task task) throws Exception {
        
        if (StringUtils.isBlank(checklistId)) {
            log.error("Can not save a task without a checklist id");
            throw new Exception("Missing checklistId");
        }
        
        Checklist checklist = checklistService.get(checklistId);
        
        if (checklist == null) {
            log.error("Trying to save a task for a non-existing checklist [checklistId={}]", checklistId);
            throw new Exception("Checklist not exists");
        }
        
        validate(task);
        
        Date date = new Date();
        
        //Add defaults.
        task.setDateCreated(date);
        task.setDateModified(date);
        task.setCompleted(Boolean.FALSE);
        task.setChecklist(checklist);
        
        taskDAO.save(task);
        
        return task;
    }

    @Override
    public Task get(String id) throws Exception {
        
        if (StringUtils.isBlank(id)) {
            log.error("Can not get a task by id without an id");
            throw new Exception("Missing taskId");
        }
        
        Task task = taskDAO.get(id);
        
        return task;
    }

    @Override
    public List<Task> getByChecklist(String checklistId) throws Exception {
        
        if (StringUtils.isBlank(checklistId)) {
            log.error("Can not get a task by checklist id without a checklist id");
            throw new Exception("Missing checklistId");
        }
        
        List<Task> tasks = taskDAO.getByChecklist(checklistId);
        
        return tasks;
    }

    @Override
    public Task update(String id, Task task) throws Exception {
        
        Task existingTask = get(id);
        
        //Updateble fields.
        existingTask.setName(StringUtils.isNotBlank(task.getName()) ? task.getName() : existingTask.getName());
        existingTask.setDateExpires(task.getDateExpires() != null ? task.getDateExpires() : existingTask.getDateExpires());
        existingTask.setDateReminder(task.getDateReminder() != null ? task.getDateReminder() : existingTask.getDateReminder());
        existingTask.setDescription(StringUtils.isNotBlank(task.getDescription()) ? task.getDescription() : existingTask.getDescription());
        existingTask.setDateModified(new Date());
        
        taskDAO.update(existingTask);
        
        return existingTask;
    }

    @Override
    public void delete(String id) throws Exception {
        
        if (StringUtils.isBlank(id)) {
            log.error("Can not delete a task without an id");
        }
        
        Task task = get(id);
        
        if (task == null) {
            log.warn("Trying to delete a non-existing task {}", id);
            return;
        }
        
        taskDAO.delete(task);
    }

    @Override
    public void complete(String id, boolean completed) throws Exception {
        
        if (StringUtils.isBlank(id)) {
            log.error("Can not complete a task without an id");
        }
        
        Task task = get(id);
        
        if (task == null) {
            log.warn("Trying to complete a non-existing task {}", id);
            return;
        }
        
        task.setCompleted(completed);
        task.setDateModified(new Date());
        
        taskDAO.update(task);
    }

}
