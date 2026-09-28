package pe.edu.upeu.lp2_gp2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        @NotBlank(message = "el email es obligatorio")
        @Email(message = "Debe ser un email valido")
        String email,
        @NotBlank(message = "el telefono es obligatorio")
        @Pattern(regexp = "^9\\d{8}$",message = "el telefono debe tener 9 digitos y empezar con 9")
        String telefono,
        String direccion,
        @NotBlank(message = "el usuario es obligatorio")
        @Size(min = 4, max = 20, message = "El usuario debe tener entre 4 y 20 caracteres")
        String usuario,
        @NotBlank(message = "el password es obligatorio")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password

) {
}
