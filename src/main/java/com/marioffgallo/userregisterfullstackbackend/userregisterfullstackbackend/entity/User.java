package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_user_seq")
    @SequenceGenerator(name = "id_user_seq", sequenceName = "id_user_seq", initialValue = 1, allocationSize = 1)
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
