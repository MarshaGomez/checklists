package org.invenio.checklists.orm;
// Generated Jan 10, 2017 5:36:21 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * Task generated by hbm2java
 */
@Entity
@Table(name = "task")
public class Task implements java.io.Serializable {

    private String id;
    private Checklist checklist;
    private Task task;
    private String name;
    private Date dateCreated;
    private Date dateModified;
    private Date dateExpires;
    private Date dateReminder;
    private String description;
    private boolean completed;
    private Set<Note> notes = new HashSet<>(0);
    private Set<Task> tasks = new HashSet<>(0);
    private Set<Issue> issues = new HashSet<>(0);

    @Id
    @GenericGenerator(name = "id", strategy = "org.invenio.checklists.util.RandomUUIDGenerator")
    @GeneratedValue(generator = "id")
    @Column(name = "id", unique = true, nullable = false, length = 36)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id", nullable = false)
    public Checklist getChecklist() {
        return this.checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Column(name = "name", nullable = false, length = 128)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false, length = 19)
    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_modified", nullable = false, length = 19)
    public Date getDateModified() {
        return this.dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_expires", length = 19)
    public Date getDateExpires() {
        return this.dateExpires;
    }

    public void setDateExpires(Date dateExpires) {
        this.dateExpires = dateExpires;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_reminder", length = 19)
    public Date getDateReminder() {
        return this.dateReminder;
    }

    public void setDateReminder(Date dateReminder) {
        this.dateReminder = dateReminder;
    }

    @Column(name = "description", length = 65535)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "completed", nullable = false)
    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    public Set<Issue> getIssues() {
        return this.issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", name=" + name + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", dateExpires=" + dateExpires + ", dateReminder=" + dateReminder + ", completed=" + completed + '}';
    }

}