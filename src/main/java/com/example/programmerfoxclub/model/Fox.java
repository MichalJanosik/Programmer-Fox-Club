package com.example.programmerfoxclub.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Fox {

    private String name;
    private List<Trick> tricks;
    private Food food;
    private Drink drink;

    public void addTrick(Trick trick) {
        tricks.add(trick);
    }
}
