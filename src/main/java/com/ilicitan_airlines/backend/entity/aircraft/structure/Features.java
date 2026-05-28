package com.ilicitan_airlines.backend.entity.aircraft.structure;

import lombok.*;
import java.io.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Features implements Serializable {
    private boolean starlink;
    private boolean externalCameras;
    private boolean cabinScreens;
}
