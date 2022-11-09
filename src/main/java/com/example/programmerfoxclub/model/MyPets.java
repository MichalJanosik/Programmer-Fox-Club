package com.example.programmerfoxclub.model;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MyPets {

    private List<Fox> myPets;

    public void addFox(Fox fox) {
        myPets.add(fox);
    }
}
