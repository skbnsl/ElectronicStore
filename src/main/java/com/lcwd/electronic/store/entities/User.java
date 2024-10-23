package com.lcwd.electronic.store.entities;

//import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//lombok=> for creating all get and setter and constructor - below five annotation of lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="users")
public class User implements UserDetails {

        @Id
        //@GeneratedValue(strategy = GenerationType.AUTO)
        private String userId;

        @Column(name = "user_name")
        private String name;

        @Column(name = "user_email",unique = true)
        private String email;

        @Getter
        @Column(name = "user_password",length = 1000)
        private String password;

        private String gender;

        @Column(length = 1000)
        private String about;

        @Column(name = "user_image_name")
        private String imageName;

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
        private List<Order> orders = new ArrayList<>();

        @OneToMany(cascade = CascadeType.ALL)
        private List<Role> roles = new ArrayList<>();

        //important for roles
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                Set<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
                return authorities;
        }

        @Override
        public String getUsername() {
                return this.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }
}
