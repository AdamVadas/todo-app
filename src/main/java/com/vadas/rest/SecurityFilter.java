package com.vadas.rest;

import com.vadas.service.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;
import java.security.Principal;

import static io.jsonwebtoken.Jwts.claims;
import static io.jsonwebtoken.Jwts.parser;

@Authz
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    @Inject
    private SecurityUtil securityUtil;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        // Grab token from the header of the request using the AUTHORIZATION constant
        String authString = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Throw an exception if no token received
        if(authString == null || authString.isEmpty() || !authString.startsWith(SecurityUtil.BEARER)){
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        }

        // Parse token
        String token = authString.substring((SecurityUtil.BEARER).length()).trim();
        try{
            Key key = securityUtil.getSecurityKey();
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            SecurityContext originalContext = containerRequestContext.getSecurityContext();
            containerRequestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal(){
                        @Override
                        public String getName(){
                            return claimsJws.getBody().getSubject();
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String role) {
                    return originalContext.isUserInRole(role);
                }

                @Override
                public boolean isSecure() {
                    return originalContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return originalContext.getAuthenticationScheme();
                }
            });
        } catch (Exception e) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
