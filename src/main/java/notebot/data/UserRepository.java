package notebot.data;

import notebot.User_;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User_,Long> {
    List<User_> findByChatId(Long chatId);
}
