package com.example.programmerfoxclub.service;

import com.example.programmerfoxclub.model.Drink;
import com.example.programmerfoxclub.model.Food;
import com.example.programmerfoxclub.model.Fox;
import com.example.programmerfoxclub.model.Trick;

import java.util.List;

public interface FoxService {

    void addFox(Fox fox);
    List<Fox> getFoxes();
    boolean checkFox(String name);
    void changeNutrition(String name, Drink drink, Food food);
    void learnTrick(String name, Trick trick);
    Fox getFox(String name);
    boolean checkTrick(Trick trick, String name);
}
