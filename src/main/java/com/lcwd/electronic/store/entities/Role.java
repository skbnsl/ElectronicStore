package com.lcwd.electronic.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Role {

    @Id
    private String roleId;
    private String name;
    //ADMIN,NORMAL

    @ManyToMany(mappedBy = "roles"/*,fetch = FetchType.EAGER*/)
    private List<User> users = new ArrayList<>();


}
