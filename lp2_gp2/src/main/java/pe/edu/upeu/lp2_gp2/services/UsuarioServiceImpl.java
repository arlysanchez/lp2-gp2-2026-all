package pe.edu.upeu.lp2_gp2.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.lp2_gp2.dto.RegistroDTO;
import pe.edu.upeu.lp2_gp2.dto.UsuarioDTO;
import pe.edu.upeu.lp2_gp2.entity.Persona;
import pe.edu.upeu.lp2_gp2.entity.Rol;
import pe.edu.upeu.lp2_gp2.entity.Usuario;
import pe.edu.upeu.lp2_gp2.exception.DuplicateResourceException;
import pe.edu.upeu.lp2_gp2.repository.PersonaRepository;
import pe.edu.upeu.lp2_gp2.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(PersonaRepository personaRepository, UsuarioRepository usuarioRepository) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Optional<UsuarioDTO> registrarCliente(RegistroDTO dto) {
       //validar que el email es unico
        if (personaRepository.existsByEmail(dto.email())){
            throw new DuplicateResourceException("El email'"+dto.email()+"'ya esta registrado");
        }
        //validar que el usuario sea unico
        if (usuarioRepository.existsByUsuario(dto.usuario())){
            throw new DuplicateResourceException("El nombre del usuario'"+dto.usuario()+"'ya existe");
        }
        //si todo va bien, procedemos a enviar los datos
        Persona p = new Persona();
        p.setNombre(dto.nombre());
        p.setEmail(dto.email());
        p.setTelefono(dto.telefono());
        p.setDireccion(dto.direccion());
        Persona personaGuardada = personaRepository.save(p);
        Usuario u = new Usuario();
        u.setUsuario(dto.usuario());
        u.setPassword(dto.password());
        u.setRol(Rol.CLIENTE);
        u.setPersona(personaGuardada);

        Usuario usuarioGuardado = usuarioRepository.save(u);
        return Optional.of(new UsuarioDTO(
                usuarioGuardado.getIdUsuario(),
                usuarioGuardado.getUsuario(),
                usuarioGuardado.getRol().name(),
                usuarioGuardado.getPersona().getNombre(),
                usuarioGuardado.getPersona().getEmail()
        ));
    }
}
