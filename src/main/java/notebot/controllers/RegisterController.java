package notebot.controllers;

import notebot.User_;
import notebot.data.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/register",produces = "application/json")
public class RegisterController {
    private final UserRepository userRepo;

    public RegisterController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User_ postUser(@RequestBody User_ user_){
        try {
            return  userRepo.save(user_);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
