package com.compass.ecommerce.model;

import com.compass.ecommerce.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "TB_USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome do usuário não pode ser nulo")
    @Size(min =  2, max = 100, message = "O nome deve conter entre 2 e 100 caracteres")
    private String name;

    @NotNull(message = "O email do usuário não pode ser nulo")
    @Email(message = "O email tem que ser válido")
    private String email;

    @NotNull(message = "O login do usuário não pode ser nulo")
    @Size(min =  2, max = 100, message = "O login deve conter entre 2 e 100 caracteres")
    private String login;

    @NotNull(message = "A senha não pode ser nula")
    @Size(min = 8, message = "A senha tem que conter mais do que 8 caracteres")
    private String password;

    @NotNull(message = "O papel de usuário deve ser especificado")
    private UserRole role;

    public User(String name, String email, String login, String password, UserRole role){
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
