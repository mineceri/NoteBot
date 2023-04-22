package notebot.controllers;

import notebot.User_;
import notebot.data.UserRepository;
import notebot.services.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user",produces = "application/json")
public class UserController {
    private final UserRepository userRepo;
    private final RegisterService registerService;

    public UserController(UserRepository userRepo, RegisterService registerService) {
        this.userRepo = userRepo;
        this.registerService = registerService;
    }
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> postUser(@RequestBody  @Validated User_ user_, Errors errors){
        Optional<User_> users;
        String s = "";
        if (errors.hasErrors()) {
            users = Optional.empty();
           s = errors.getAllErrors().get(0).getDefaultMessage();
        } else {
            users=Optional.of(userRepo.save(user_));
        }
        String finalS = s;
        return users.map(value -> new ResponseEntity<Object>(value, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(finalS, HttpStatus.GONE));

    }
    @GetMapping("/user/{chatId}")
    public ResponseEntity<User_> findById(@PathVariable String chatId){
        List<User_> users=userRepo.findByChatId(chatId);
        if(users.isEmpty()){
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users.get(0),HttpStatus.OK);
    }


}
