package com.compass.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

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


}
