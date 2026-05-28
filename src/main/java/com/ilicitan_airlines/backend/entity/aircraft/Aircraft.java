package com.ilicitan_airlines.backend.entity.aircraft;

import com.ilicitan_airlines.backend.entity.aircraft.structure.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
import java.io.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "aircrafts")
public class Aircraft implements Serializable {
    @Id
    private String id;
    private String model;
    private Features features;
    private Seats seats;
}
