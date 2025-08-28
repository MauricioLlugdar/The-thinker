package com.maullu.the_thinker.Controllers;

import com.maullu.the_thinker.Model.Idea;
import com.maullu.the_thinker.Model.User;
import com.maullu.the_thinker.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
}
