package notebot.services;

import notebot.User_;
import notebot.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    UserRepository userRepo;

    public boolean userIdService(User_ user_) {
        return userRepo.findByChatId(user_.getChatId()).size()!=0;
    }
}
