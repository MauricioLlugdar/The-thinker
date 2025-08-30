package com.maullu.the_thinker.Controllers;

import com.maullu.the_thinker.Model.Idea;
import com.maullu.the_thinker.Model.User;
import com.maullu.the_thinker.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    private UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    ResponseEntity<User> findById(@PathVariable Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucb){
        User savedUser = userRepository.save(user);
        URI locationUser = ucb
                .path("/user/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(locationUser).build();
    }
}
