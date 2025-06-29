package com.utec.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.persistence.Entity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

//lombok
@Data
@ToString(exclude = "telefonos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity //MAPEO JPA
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    //id
    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //documento /evaluar caso para el digito verificador
    @Column(name = "nume_documento", unique = true, nullable = false, length = 15)
    @Size(min = 1, max = 15, message = "No puede ingresar mas de 15 digitos")
    @Pattern(regexp = "^[A-Za-z0-9]{1,15}$") //letras y numeros, sin espacios ni simbolos, logitud entre 1 y 15.
    @NotNull(message = "Este campo es obligatorio")
    private String numeDocumento;


    //primer nombre
    @Column(name = "prim_nombre",nullable = false, length = 20) //validar largo
    @NotNull(message = "Este campo es obligatorio")
    @Size(min = 1, max = 20, message = "No puede ingresar mas de 20 digitos")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$") //solo letras, incluyendo tildes, no permite ni signos de puntuacion, ni numeros ni caracteres especiales
    private String primNombre;

    //segundo nombre, PUEDE SER NULO
    @Column(name = "segu_nombre", length = 20)
    @Size(min = 1, max = 20, message = "No puede ingresar mas de 20 digitos")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")//solo letras, incluyendo tildes, no permite ni signos de puntuacion, ni numeros ni caracteres especiales
    private String seguNombre;

    //primer apellido
    @Column(name = "prim_apellido",nullable = false, length = 20)
    @NotNull(message = "Este campo es obligatorio")
    @Size(max = 20, message = "No puede ingresar mas de 20 digitos")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$") //solo letras, incluyendo tildes, no permite ni signos de puntuacion, ni numeros ni caracteres especiales
    private String primApellido;

    //segundo apellido, PUEDE SER NULO
    @Column(name = "segu_apellido", length = 20)
    @Size(max = 20, message = "No puede ingresar mas de 20 digitos")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")//solo letras, incluyendo tildes, no permite ni signos de puntuacion, ni numeros ni caracteres especiales
    private String seguApellido;

    //contrasenia
    @Column(name = "contrasenia", nullable = false, length = 60) //muy larga
    @NotNull(message = "Este campo es obligatorio")
    @NotBlank
    @Size(min = 8, max = 60, message = "No puede ingresar mas de 60 digitos") //esto es asi?
    private String contrasenia;

    //correo
    @NotBlank
    @Email
    @Size(max = 100) //muy larga
    @Pattern(regexp ="^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|asur\\.com|([a-zA-Z0-9-]+\\.)*utec\\.edu\\.uy)$") //valida varios formatos de correo, incluyendo el de utec
    @Column(name = "correo", unique = true, nullable = false, length = 100)
    @NotNull(message = "Este campo es obligatorio")
    private String correo;

    //fecha
    @PastOrPresent //para fechas validas
    @Column(name = "fech_nacimiento",nullable = false)
    @NotNull(message = "Este campo es obligatorio")
    private LocalDate fechNacimiento;

    //apartamento
    @Column(name = "apartamento", length = 10) //no null?
    @Pattern(regexp = "^[A-Za-z0-9 ]*$") //solo letras mayusculas o minusculas, numeros y espacios.
    @Size(max = 10, message = "No puede ingresar mas de 10 digitos")
    @NotNull(message = "Este campo es obligatorio")
    private String apartamento;

    //calle
    @Column(name = "calle",nullable = false, length = 50)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,\\- ]+$") //letras y acentos, numeros, comas, puntos, guines y espacios.
    @Size(min =1 ,max = 50, message = "No puede ingresar mas de 50 digitos")
    @NotNull(message = "Este campo es obligatorio")
    private String calle;

    //numero de puerta
    @Column(name = "nume_puerta",nullable = false)
    @NotNull(message = "Este campo es obligatorio")
    private Integer numePuerta;

    //id estado, FK
    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false, foreignKey = @ForeignKey(name = "FK_Usuario_Estado"))
    private Estado estado;

    //tipo de docuemnto -> FK
    @ManyToOne
    @JoinColumn(name = "Id_Tipo_Documento", nullable = false,  foreignKey = @ForeignKey(name = "FK_Usuario_Tipo_Documento"))
    private TipoDocumento tipoDocumento;

    //perfil -> FK
    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false, foreignKey = @ForeignKey(name = "FK_Usuario_Perfil"))
    private Perfil perfil;

    @CreatedBy
    @Column(name = "crea_Por")
    private String creadoPor;

    @CreatedDate
    @Column(name = "fech_Creacion")
    private LocalDate fechaCreacion;

    @LastModifiedBy
    @Column(name = "modi_Por")
    private String modificadoPor;

    @LastModifiedDate
    @Column(name = "fech_Modificacion")
    private LocalDate fechaModificacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Telefono> telefonos;
    //telefono
  /*  @Column(name = "teléfono", nullable = false)
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    @NotNull(message = "Este campo es Obligatorio")
    private String telefono;*/

}
