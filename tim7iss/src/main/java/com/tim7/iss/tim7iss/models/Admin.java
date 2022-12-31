package com.tim7.iss.tim7iss.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;


@Entity
@Getter
@Setter
public class Admin extends User {

    private String username;

}
