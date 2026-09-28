package pe.edu.upeu.lp2_gp2.dto;

public record UsuarioDTO(
      Long idUsuario,
      String Usuario,
      String rol,
      String nombrePersona,
      String email
) {
}
