package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll() {
        var result = userRepository.findAll();
        return result.stream().map(u -> userMapper.map(u)).toList();
    }

    public UserDTO findById(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id " + id + " not found"));
        return userMapper.map(user);
    }

    public UserDTO create(UserCreateDTO data) {
        try {
            var user = userMapper.map(data);
            userRepository.save(user);
            return userMapper.map(user);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public UserDTO update(long id, UserUpdateDTO data) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id " + id + " not found"));
        userMapper.update(data, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void delete(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id " + id + " not found"));
        userRepository.deleteById(id);
    }

}