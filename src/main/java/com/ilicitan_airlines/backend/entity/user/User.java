package com.ilicitan_airlines.backend.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ilicitan_airlines.backend.entity.user.structure.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User implements Serializable {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String name;
    private String surname;
    private Passport passport;
    private Phone phone;
    private Instant birthDate;
    private Sex sex;
    private Integer miles;
    private String authProvider;
    @Field("password")
    @JsonIgnore
    private String password;
}
