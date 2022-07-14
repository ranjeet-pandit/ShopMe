package com.cognizant.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="role")
@NoArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    private long id;

    private String name;
}
