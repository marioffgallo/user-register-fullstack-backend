package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * User model for handle data
 *
 * @author Mario F.F Gallo
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String name;
    private int age;
    private String email;
    private Date birthDate;

    @Override
    public String toString() {
        return id + "," + name + "," + age + "," + email + "," + birthDate;
    }
}
