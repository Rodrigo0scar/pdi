package com.utec.auditoria;

import com.utec.jwt.JwtTokenUtil;
import com.utec.model.Auditoria;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {

    private final AuditoriaRepository auditoriaRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;

    @AfterReturning("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Service *)")
    public void auditarMetodo(JoinPoint joinPoint) {
        try {
            String claseNombre = joinPoint.getSignature().getDeclaringType().getSimpleName(); // solo el nombre de la clase

            List<String> exclusiones = List.of(
                    "SwaggerConfigResource",
                    "OpenApiWebMvcResource",
                    "CustomUsuarioDetailsService"
            );

            if (exclusiones.contains(claseNombre)) {
                return; // Ignorar para estas clases
            }

            String token = request.getHeader("Authorization");
            String usuario = "Desconocido";

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // quitar "Bearer "
                usuario = jwtTokenUtil.extractUsername(token);
            }

            String ip = request.getRemoteAddr();
            String metodo = joinPoint.getSignature().toShortString();

            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(usuario);
            auditoria.setTerminal(ip);
            //auditoria.setFechaHora(LocalDateTime.now());
            auditoria.setOperacion(metodo);

            auditoriaRepository.save(auditoria);
        } catch (Exception e) {
            System.err.println("Error en auditoría: " + e.getMessage());
        }
    }
}