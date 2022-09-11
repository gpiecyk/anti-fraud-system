package antifraud.user.rest;

import antifraud.user.data.UserRepository;
import antifraud.user.data.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserResource {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResource(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws URISyntaxException {
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username '" + user.getUsername() + "' already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/auth/" + createdUser.getUsername())).body(createdUser);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> findUsers() {
        List<User> users = userRepository.findAllByOrderByIdAsc();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.ok(Map.of("username", username, "status", "Deleted successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
