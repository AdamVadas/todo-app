package com.vadas.service;

import com.vadas.entity.Todo;
import com.vadas.entity.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class TodoService {

    @Inject
    EntityManager entityManager;

    @Inject
    private QueryService queryService;

    private String email;

    @PostConstruct
    private void init() {
        email = "";
    }

    public User saveUser(User user){
        entityManager.persist(user);
        return user;
    }

    public Todo createTodo(Todo todo) {
        User userByEmail = queryService.findUserByEmail(email);
        if(userByEmail != null){
            todo.setTodoOwner(userByEmail);
            entityManager.persist(todo);
        }
        return todo;
    }

    public Todo updateTodo(Todo todo) {
        entityManager.merge(todo);
        return todo;
    }

    public Todo findTodoById(Long id) {
        return entityManager.find(Todo.class, id);
    }

    public List<Todo> getTodos() {
        return entityManager.createQuery("SELECT t from Todo t", Todo.class).getResultList();
    }

}
