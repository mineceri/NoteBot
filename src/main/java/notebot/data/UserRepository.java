package notebot.data;

import notebot.User_;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User_,String> {
    List<User_> findByChatId(String chatId);
}
