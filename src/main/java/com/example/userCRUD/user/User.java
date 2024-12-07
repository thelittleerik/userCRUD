package com.example.userCRUD.user; // package names should be all lowercase

import java.util.List;
import java.util.UUID;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data // avoid @Data on @Entity: reason is that it also generates toString and equals methods depending on foreign-keys.
// ...which might trigger lots of additional selects on an non-EAGER FK for a simple log-statement down the line.
@Builder
@AllArgsConstructor
@Table(name="users")
public class User {
    @Id // is anyway a @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "first_name", nullable = false) // can be @Column() if you call your attribute firstName (camel)
    private String firstname;

    @Column(name = "last_name", nullable = false)
    private String lastname;

    @Column // we usually prefer less code
    private int age;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_hobbies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "hobby", nullable = false)
    private List<String> hobbies;

    @Column(name = "smoker", nullable = false)
    private boolean smoker = false;

    @Column(name = "fav_food", nullable = false)
    private String favoriteFood;
}
