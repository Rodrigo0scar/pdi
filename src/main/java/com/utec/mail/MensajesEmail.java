package com.utec.mail;

import org.springframework.stereotype.Component;

@Component
public class MensajesEmail {
    private final EmailFacade emailFacade;

    public MensajesEmail(EmailFacade emailFacade) {
        this.emailFacade = emailFacade;
    }

    public void mensajeRegistroUsuario(String email) {
        String subject = "ASUR - Registro de usuario";
        String body = "Su registro de usuario está en período de aprobación";
        emailFacade.enviarCorreo(email, subject, body);
    }

    public void mensajeModificacionASocio(String email){
        String  subject="ASUR - Modificación de tipo de usuario";
        String  body = "Su registro de usuario ha quedado activo. El tipo de usuario ahora es Socio. ";
        emailFacade.enviarCorreo(email, subject, body);
    }
    public void mensajeModificacionAUsuario(String email){
        String  subject="ASUR - Modificación de tipo de usuario";
        String  body = "Su registro de usuario ha quedado activo. El tipo de usuario ahora es No Socio. ";
        emailFacade.enviarCorreo(email, subject, body);
    }

    public void mensajeActivacionUsuario(String email) {
        String subject="";
        String body="";

        subject="ASUR - Activación de usuario";
        body = "Su registro de usuario ha quedado en estado activo" ;


        emailFacade.enviarCorreo(email, subject, body);
    }

    public void mensajeBajaUsuario(String email) {
        String subject="";
        String body="";


        subject="ASUR - Baja de usuario";
        body = "Su registro de usuario ha quedado en estado inactivo";

        emailFacade.enviarCorreo(email, subject, body);
    }
}
