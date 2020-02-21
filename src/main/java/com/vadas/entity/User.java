package com.vadas.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "TodoUser")
public class User extends AbstractEntity {

    @NotNull(message = "Full name is mandatory.")
    @Size(min = 4, message = "The minimum required characters for the full name is 4.")
    @Pattern(regexp = "", message = "Full name must be alphabetical.")
    private String fullName;

    @NotNull(message = "Email is mandatory.")
    @Email(message = "Email must be in the following form: user@domain.com")
    private String email;

    @NotNull(message = "Password is mandatory.")
    @Size(min = 8, message = "Password length must be at least 8 characters.")
    @Pattern(regexp = "", message = "Password must be a combination of alphabetical, numeric and special characters!")
    private String password;
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
