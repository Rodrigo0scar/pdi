package com.utec.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class AuthRequest {

    private String correo;
    private String contrasenia;


}
