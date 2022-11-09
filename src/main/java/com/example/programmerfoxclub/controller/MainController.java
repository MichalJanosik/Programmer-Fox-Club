package com.example.programmerfoxclub.controller;

import com.example.programmerfoxclub.model.Drink;
import com.example.programmerfoxclub.model.Food;
import com.example.programmerfoxclub.model.Fox;
import com.example.programmerfoxclub.model.Trick;
import com.example.programmerfoxclub.service.FoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final FoxService foxService;

    @GetMapping("/")
    public String indexHandler(
            @RequestParam(value = "loggedInAs", required = false) String loggedInAs,
            Model model
    ) {
        if (Objects.nonNull(loggedInAs) && !loggedInAs.isBlank()) {
            Fox fox = foxService.getFoxes().stream()
                    .filter(x -> x.getName().equals(loggedInAs))
                    .findFirst()
                    .get();
            model.addAttribute("fox", fox);
            model.addAttribute("loggedInAs", fox.getName());
//            model.addAttribute("food", fox.getFood().toString().toLowerCase());
//            model.addAttribute("drink", fox.getDrink().toString().toLowerCase());
//            model.addAttribute("tricks",
//                    Objects.isNull(fox.getTricks()) ? "0" : fox.getTricks().size()
//            );
//            model.addAttribute("knownTricks", fox.getTricks());

            //debugging:
            System.out.println(
                    foxService.getFoxes().stream()
                            .map(Fox::getName)
                            .collect(Collectors.toList())
            );

            return "index";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginHandler(
            @RequestParam(value = "loggedInAs", required = false) String loggedInAs,
            Model model
    ) {
        model.addAttribute(
                "message",
                "Please provide a name, " +
                        "so you can associate it to your pet!");
        if (Objects.nonNull(loggedInAs) && !loggedInAs.isBlank()) {
            model.addAttribute("loggedInAs", loggedInAs);
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginHandler(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "newFox", required = false) String newFox,
            @RequestParam(value = "loggedInAs", required = false) String loggedInAs,
            Model model
    ) {
        if (Objects.nonNull(loggedInAs) && !loggedInAs.isBlank()) {
            model.addAttribute("loggedInAs", loggedInAs);
            return "login";
        }
        if (Objects.nonNull(newFox) && !newFox.isBlank()) {
            foxService.addFox(
                    Fox.builder()
                            .name(newFox)
                            .tricks(new ArrayList<>())
                            .build()
            );
            return "redirect:/?loggedInAs=" + newFox;
        }

        if ((Objects.nonNull(name) && !name.isBlank())
                && Objects.isNull(login)
        ) {
            if (!foxService.checkFox(name)) {
                model.addAttribute(
                        "message",
                        "You have provided a name that has " +
                                "not been used before, add it as a new one?");
                model.addAttribute("name", name);
                return "login";
            }
        }

        return "redirect:/?loggedInAs=" + name;
    }

    @GetMapping("/nutrition")
    public String nutritionHandler(
            @RequestParam(value = "loggedInAs") String loggedInAs,
            Model model
    ) {
        if (Objects.isNull(loggedInAs) || loggedInAs.isBlank()) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInAs", loggedInAs);
        model.addAttribute(
                "drinks",
                Arrays.stream(Drink.values())
                        .map(x -> x.toString().toLowerCase())
                        .collect(Collectors.toList()));
        model.addAttribute(
                "foods",
                Arrays.stream(Food.values())
                        .map(x -> x.toString().toLowerCase())
                        .collect(Collectors.toList()));
        return "nutrition-store";
    }

    @PostMapping("/nutrition")
    public String nutritionHandler(@RequestParam(value = "drink") String drink,
                                   @RequestParam(value = "food") String food,
                                   @RequestParam(value = "loggedInAs") String loggedInAs
    ) {
        System.out.printf("%s %s%n", drink, food);
        if (Objects.nonNull(loggedInAs) && !loggedInAs.isBlank()) {
            foxService.changeNutrition(
                    loggedInAs,
                    Drink.valueOf(drink.toUpperCase()),
                    Food.valueOf(food.toUpperCase())
            );
        }
        return "redirect:/?loggedInAs=" + loggedInAs;
    }

    @GetMapping("/trickCenter")
    public String trickCenterHandler(
            @RequestParam(value = "loggedInAs") String loggedInAs,
            Model model
    ) {
        if (Objects.isNull(loggedInAs) || loggedInAs.isBlank()) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInAs", loggedInAs);
        model.addAttribute(
                "tricks",
                Arrays.stream(Trick.values())
                        .map(Enum::toString)
                        .collect(Collectors.toList())
        );
        return "/trick-center";
    }

    @PostMapping("/trickCenter")
    public String trickCenterHandler(
            @RequestParam(value = "trick") String trick,
            @RequestParam(value = "loggedInAs") String loggedInAs,
            RedirectAttributes redirectAttributes
    ) {
        if (Objects.isNull(loggedInAs) || loggedInAs.isBlank()) {
            return "redirect:/login";
        }
        if (!foxService.checkTrick(Trick.valueOf(trick), loggedInAs)) {
            foxService.learnTrick(loggedInAs, Trick.valueOf(trick));
        } else {
            redirectAttributes.addFlashAttribute("knownTrick", trick);
            redirectAttributes.addFlashAttribute("loggedInAs", loggedInAs);
            return "redirect:/trickCenter?loggedInAs=" + loggedInAs;
        }
        return "redirect:/?loggedInAs=" + loggedInAs;
    }
}