package com.compass.ecommerce.Service;

import com.compass.ecommerce.DTO.User.*;
import com.compass.ecommerce.Exception.PasswordMismatchExcepiton;
import com.compass.ecommerce.Exception.ResourceAlredyExistsException;
import com.compass.ecommerce.Exception.ResourceNotFoundException;
import com.compass.ecommerce.model.User;
import com.compass.ecommerce.model.enums.UserRole;
import com.compass.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;


    public UserDTOResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo id: " + id));

        return new UserDTOResponse(user.getId(), user.getName(), user.getLogin(), user.getEmail());
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo id: " + id));

        userRepository.delete(user);
    }

    public UserDTOResponse updateUser(Long id, UpdateUserDTO updateUserDTO){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo id: " + id));

        // Pega o usuário autenticado.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        //Verifica se o login e o email ja estão sendo utilizados
        if (userRepository.existsByLoginAndIdNot(updateUserDTO.login(), id)) {
            throw new ResourceAlredyExistsException("Login ja está em uso");
        }

        if (userRepository.existsByEmailAndIdNot(updateUserDTO.email(), id)) {
            throw new ResourceAlredyExistsException("Email ja está em uso");
        }

        // Verifica se o usuário autenticado é ADMIN
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        // Se o usuário a ser atualizado for ADMIN, apenas administradores podem atualizá-lo
        if (user.getRole() == UserRole.ADMIN && !isAdmin) {
            throw new ResourceNotFoundException("Apenas administradores podem atualizar outros administradores!");
        }

        // Se o usuário autenticado não for ADMIN e estiver tentando atualizar outro usuário, não permite
        if (!isAdmin && !authenticatedUser.getId().equals(id)) {
            throw new ResourceNotFoundException("Usuários comuns só podem atualizar a si mesmos!");
        }

        user.setName(updateUserDTO.name());
        user.setLogin(updateUserDTO.login());
        user.setEmail(updateUserDTO.email());

        userRepository.save(user);

        return new UserDTOResponse(user.getId(), user.getName(), user.getLogin(), user.getEmail());
    }

    public RegisterDTO registerUser(RegisterDTO dto) {

        //Busca o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (userRepository.existsByLogin(dto.login())) {
            throw new ResourceAlredyExistsException("Login ja está em uso");
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new ResourceAlredyExistsException("Email ja está em uso");
        }

        //Verifica se o novo usuário é ADMIN, caso seja, verifica se o usuário autenticado é admin
        if (dto.role() == UserRole.ADMIN) {
            boolean isAdmin = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin) {
                throw new RuntimeException("Apenas administradores podem criar outros administradores!");
            }
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.name(), dto.email(), dto.login(), encryptedPassword, dto.role());

        userRepository.save(user);

        return new RegisterDTO(user.getName(), user.getEmail(), user.getLogin(), user.getPassword(), user.getRole());
    }


    public LoginDTOResponse login(AuthenticationDTORequest dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginDTOResponse(token);
    }

    //Método adicional, caso o usuário já esteja logado e queira alterar a sua senha!
    public void changePassword(UpdatePasswordDTO updatePasswordDTO){

        // Pega o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if(!passwordEncoder.matches(updatePasswordDTO.oldPassword(), user.getPassword())){
            throw new PasswordMismatchExcepiton("A senha antiga está incorreta");
        }

        //Verifica a igualdade das senhas
        if (!updatePasswordDTO.newPassword().equals(updatePasswordDTO.confirmPassword())) {
            throw new PasswordMismatchExcepiton("As senhas são diferentes");
        }

        System.out.println(user.getPassword());

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.newPassword()));
        userRepository.save(user);
    }

    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        // Verifica se o usuário com o login fornecido existe
        User user = userRepository.findByLogin(resetPasswordDTO.login())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o login: " + resetPasswordDTO.login()));

        // Verifica se as senhas coincidem
        if (!resetPasswordDTO.newPassword().equals(resetPasswordDTO.confirmPassword())) {
            throw new PasswordMismatchExcepiton("As senhas são diferentes");
        }

        // Atualiza a senha do usuário
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.newPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado, login ou senha errados: "));
    }
}
