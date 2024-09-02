package com.compass.ecommerce.Service;

import com.compass.ecommerce.DTO.UserDTORequest;
import com.compass.ecommerce.DTO.UserDTOResponse;
import com.compass.ecommerce.Exception.ResourceNotFoundException;
import com.compass.ecommerce.model.User;
import com.compass.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTOResponse create(UserDTORequest userDTORequest) {
        User user = new User();
        user.setName(userDTORequest.name());
        user.setEmail(userDTORequest.email());
        user.setLogin(userDTORequest.login());
        user.setPassword(userDTORequest.password());

        userRepository.save(user);
        return new UserDTOResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserDTOResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo id: " + id));

        return new UserDTOResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserDTOResponse updateUser(Long id, UserDTORequest userDTORequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo id: " + id));

        user.setName(userDTORequest.name());
        user.setLogin(userDTORequest.login());
        user.setEmail(userDTORequest.email());
        user.setPassword(userDTORequest.password());


        userRepository.save(user);

        return new UserDTOResponse(user.getId(), user.getName(), user.getEmail());
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo id: " + id));

        userRepository.delete(user);
    }


}
