package antifraud.user.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    List<User> findAllByOrderByIdAsc();
}
