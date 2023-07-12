package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.sharingservice.exception.NotUniqueDataException;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find user with id: " + id));
    }

    public User createUser(User user) {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser.isPresent()) {
            throw new NotUniqueDataException("User with email: " + user.getEmail() + " already exist.");
        }
        return userRepository.save(user);
    }

    public void updateUser(String email) {
        userRepository.findByEmail(email)
                .map(user -> {
//                    user.setImg();
                    return userRepository.save(user);
                }).orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .build()));
    }
}
