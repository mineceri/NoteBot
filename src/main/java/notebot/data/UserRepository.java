package notebot.data;

import notebot.User_;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User_,String> {
}
