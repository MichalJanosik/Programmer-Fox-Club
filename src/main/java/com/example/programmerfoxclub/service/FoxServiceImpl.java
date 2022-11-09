package com.example.programmerfoxclub.service;

import com.example.programmerfoxclub.model.Drink;
import com.example.programmerfoxclub.model.Food;
import com.example.programmerfoxclub.model.Fox;
import com.example.programmerfoxclub.model.Trick;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@AllArgsConstructor
public class FoxServiceImpl implements FoxService {

    private List<Fox> myPets;

    @Override
    public void addFox(Fox fox) {
        fox.setFood(Food.PIZZA);
        fox.setDrink(Drink.TABASCO);
        myPets.add(fox);
    }

    @Override
    public List<Fox> getFoxes() {
        return myPets;
    }

    @Override
    public boolean checkFox(String name) {
        return myPets.stream()
                .anyMatch(x -> x.getName().equals(name));
    }

    @Override
    public void changeNutrition(String name, Drink drink, Food food) {
        Fox fox = getFox(name);
        fox.setDrink(drink);
        fox.setFood(food);
    }

    @Override
    public void learnTrick(String name, Trick trick) {
        Fox fox = getFox(name);
        fox.addTrick(trick);
    }

    @Override
    public Fox getFox(String name) {
        return myPets.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .get();
    }

    @Override
    public boolean checkTrick(Trick trick, String name) {
        Fox fox = getFox(name);
        return fox.getTricks().stream()
                .anyMatch(x -> x.equals(trick));
    }
}