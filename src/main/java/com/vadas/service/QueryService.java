package com.vadas.service;

import com.vadas.entity.Todo;
import com.vadas.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class QueryService {

    @Inject
    EntityManager entityManager;

    @Inject
    private SecurityUtil securityUtil;

    public User findUserByEmail(String email) {
        List<User> userList = entityManager.createNamedQuery(User.FIND_USER_BY_EMAIL, User.class).setParameter("email", email).getResultList();
        if (!userList.isEmpty()){
            return userList.get(0);
        }
        return null;
    }

    public List countUserByEmail(String email) {
        return entityManager.createNativeQuery("select count (id) from TodoUser where exists (select id from TodoUser where email = ?").setParameter(1, email).getResultList();
    }

    public Todo findTodoById(Long id, String email) {
        List<Todo> resultList = entityManager.createNamedQuery(Todo.FIND_TODO_BY_ID, Todo.class)
                .setParameter("id", id)
                .setParameter("email", email)
                .getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    public List<Todo> getAllTodos(String email) {
        return entityManager.createNamedQuery(Todo.FIND_ALL_TODOS_BY_USER, Todo.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<Todo> getAllTodosByTask(String taskText, String email) {
        return entityManager.createNamedQuery(Todo.FIND_TODO_BY_TASK, Todo.class)
                .setParameter("email", email)
                .setParameter("task", "%" + taskText + "%")
                .getResultList();
    }

}
