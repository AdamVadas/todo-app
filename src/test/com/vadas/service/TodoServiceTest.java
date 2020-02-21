package com.vadas.service;

import com.vadas.entity.Todo;
import com.vadas.entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class TodoServiceTest {

    @Inject
    private User user;

    @Inject
    TodoService todoService;
    Logger logger;


    @Deployment
    public static JavaArchive createDeployment(){
        return ShrinkWrap.create(JavaArchive.class, "todo-app")
                .addPackage(Todo.class.getPackage())
                .addPackage(TodoService.class.getPackage())
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {

        logger = Logger.getLogger(TodoServiceTest.class.getName());
        user.setEmail("bla@bla.com");
        user.setFullName("John Doe");
        user.setPassword("blablabla");

        todoService.saveUser(user);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void saveUser(){
        assertNotNull(user.getId());
        logger.log(Level.INFO, user.getId().toString());
        assertNotEquals("The user password is not the same as the hashed one", "blablabla", user.getPassword());
        logger.log(Level.INFO, user.getPassword());
    }

}