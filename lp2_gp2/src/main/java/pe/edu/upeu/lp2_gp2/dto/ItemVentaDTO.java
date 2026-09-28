package pe.edu.upeu.lp2_gp2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemVentaDTO(
        @NotNull
        Long idProducto,
        @Min(1)
        Integer cantidad

) {
}
