package pe.edu.upeu.lp2_gp2.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VentaDTO(
        @NotNull
        Long idPersona,
        @NotEmpty
        List<ItemVentaDTO> items
) {
}
