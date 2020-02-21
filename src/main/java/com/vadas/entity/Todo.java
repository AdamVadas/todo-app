package com.vadas.entity;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@NamedQuery(name = Todo.FIND_TODO_BY_TASK, query = "select t from Todo t where t.task like :task and t.todoOwner.email = :email")
@NamedQuery(name = Todo.FIND_ALL_TODOS_BY_USER, query = "select t from Todo t where t.todoOwner.email = :email")
@NamedQuery(name = Todo.FIND_TODO_BY_ID, query = "select t from Todo t where t.id = :id and t.todoOwner.email = :email")
public class Todo extends AbstractEntity {

    public static final String FIND_TODO_BY_TASK = "Todo.findByTask";
    public static final String FIND_ALL_TODOS_BY_USER = "Todo.findByUser";
    public static final String FIND_TODO_BY_ID = "Todo.findById";

    @NotEmpty(message = "Task must not be empty!")
    @Size(min = 10, message = "Minimum required characters: 10")
    private String task;

    @NotNull(message = "Due date is mandatory!")
    @FutureOrPresent(message = "Due date must be in the present or the future!")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate dueDate;

    private boolean isCompleted;
    private LocalDate dateCompleted;
    private LocalDate dateCreated;

    @ManyToOne
    private User todoOwner;

    @PrePersist
    private void init(){
        setDateCreated(LocalDate.now());
    }

    public User getTodoOwner() {
        return todoOwner;
    }

    public void setTodoOwner(User todoOwner) {
        this.todoOwner = todoOwner;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}
