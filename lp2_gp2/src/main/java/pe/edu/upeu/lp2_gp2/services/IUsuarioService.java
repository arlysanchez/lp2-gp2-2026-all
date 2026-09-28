package pe.edu.upeu.lp2_gp2.services;

import pe.edu.upeu.lp2_gp2.dto.RegistroDTO;
import pe.edu.upeu.lp2_gp2.dto.UsuarioDTO;
import pe.edu.upeu.lp2_gp2.entity.Persona;

import java.util.Optional;

public interface IUsuarioService {
    Optional<UsuarioDTO> registrarCliente(RegistroDTO dto);
}
