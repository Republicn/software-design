package ru.itmo.republicn.react.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.itmo.republicn.react.model.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.republicn.react.repository.UserRepository;

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public void register(@RequestBody User user) {
        userRepository.save(user);
    }

    @GetMapping
    public Flux<User> getAll() {
        return userRepository.findAll();
    }

}