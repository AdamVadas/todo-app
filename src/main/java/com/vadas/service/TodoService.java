package com.vadas.service;

import com.vadas.entity.Todo;
import com.vadas.entity.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Stateless
public class TodoService {

    @Inject
    EntityManager entityManager;

    @Inject
    private QueryService queryService;

    @Inject
    private SecurityUtil securityUtil;

    private String email;

    @PostConstruct
    private void init() {
        email = "";
    }

    public User saveUser(User user){
        Integer count = (Integer) queryService.countUserByEmail(user.getEmail()).get(0);

        if(user.getId() == null && count == 0){
            Map<String, String> credMap = securityUtil.hashPassword(user.getPassword());
            user.setPassword(credMap.get(SecurityUtil.HASHED_PASSWORD_KEY));
            user.setSalt(credMap.get(SecurityUtil.SALT_KEY));
            entityManager.persist(user);
            credMap.clear();
        }
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
        return queryService.findTodoById(id);
    }

    public List<Todo> getTodos() {
        return queryService.getAllTodos();
    }

}
