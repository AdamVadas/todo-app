package com.vadas.service;

import com.vadas.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class QueryService {

    @Inject
    EntityManager entityManager;

    public User findUserByEmail (String email){

        return entityManager.createNamedQuery(User.FIND_USER_BY_EMAIL, User.class).setParameter("email", email).getResultList().get(0);
    }

}
