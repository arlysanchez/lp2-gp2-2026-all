package pe.edu.upeu.lp2_gp2.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El user es obligatorio")
        String usuario,
        @NotBlank(message = "la clave es obligatorio")
        String password
) {
}
