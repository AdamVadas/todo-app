package com.vadas.rest;

import com.vadas.entity.User;
import com.vadas.service.SecurityUtil;
import com.vadas.service.TodoService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

@Path("user")
public class UserRest {

    @Inject
    private SecurityUtil securityUtil;

    @Inject
    private TodoService todoService;

    @Context
    private UriInfo uriInfo;

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@NotNull @FormParam("email") String email,
                          @NotNull @FormParam("password") String password) {

        // Authenticate user
        boolean isUserAuthenticated = securityUtil.authenticateUser(email, password);
        if (!isUserAuthenticated) {
            throw new SecurityException("Email or password invalid!");
        }

        // Generate token
        String token = generateToken(email);

        // Return the token in Response header to the client
        return Response.ok().header(HttpHeaders.AUTHORIZATION, SecurityUtil.BEARER + " " + token).build();
    }

    private String generateToken(String email) {
        Key securityKey = securityUtil.getSecurityKey();
        return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setIssuer(uriInfo.getBaseUri().toString())
                .setAudience(uriInfo.getAbsolutePath().toString())
                .setExpiration(securityUtil.toDate(LocalDateTime.now().plusMinutes(15)))
                .signWith(SignatureAlgorithm.HS512, securityKey).compact();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUser(@NotNull User user){
        todoService.saveUser(user);
        return Response.ok(user).build();
    }
}
