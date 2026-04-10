package com.example.onskeskyen.Controller;

import com.example.onskeskyen.Models.User;
import com.example.onskeskyen.Models.Wish;
import com.example.onskeskyen.Repos.UserRepository;
import com.example.onskeskyen.Repos.WishRepository;
import com.example.onskeskyen.Services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password) {

        User user = new User();
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return "redirect:/login";
    }

    @GetMapping("/wishes")
    public String wishes(Model model) {

        User user = userService.getCurrentUser();

        model.addAttribute("wishes",
                wishRepository.findByUser(user));

        return "wishes";
    }

    @PostMapping("/wishes")
    public String addWish(@RequestParam String text) {

        User user = userService.getCurrentUser();

        Wish wish = new Wish();
        wish.setText(text);
        wish.setUser(user);

        wishRepository.save(wish);

        return "redirect:/wishes";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/wishes/delete")
    public String deleteWish(@RequestParam Long id) {

        Wish wish = wishRepository.findById(id).orElseThrow();

        User user = userService.getCurrentUser();

        if (wish.getUser().getId().equals(user.getId())) {
            wishRepository.delete(wish);
        }

        return "redirect:/wishes";
    }

    @GetMapping("/wishes/edit")
    public String editPage(@RequestParam Long id, Model model) {

        Wish wish = wishRepository.findById(id)
                .orElseThrow();

        model.addAttribute("wish", wish);

        return "editWish";
    }

    @PostMapping("/wishes/edit")
    public String updateWish(@RequestParam Long id,
                             @RequestParam String text) {

        Wish wish = wishRepository.findById(id).orElseThrow();

        User user = userService.getCurrentUser();

        if (wish.getUser().getId().equals(user.getId())) {
            wish.setText(text);
            wishRepository.save(wish);
        }

        return "redirect:/wishes";
    }

}
