package com.utec.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaDTO {
    private String usuario;
    private String terminal;
    private LocalDateTime fechaHora;
    private String operacion;

}
