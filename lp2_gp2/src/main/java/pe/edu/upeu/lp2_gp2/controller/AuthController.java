package pe.edu.upeu.lp2_gp2.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.lp2_gp2.dto.RegistroDTO;
import pe.edu.upeu.lp2_gp2.dto.UsuarioDTO;
import pe.edu.upeu.lp2_gp2.services.IUsuarioService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> register(@Valid @RequestBody RegistroDTO dto){
        return usuarioService.registrarCliente(dto)
                .map(user -> new ResponseEntity<>(user,HttpStatus.CREATED))
                .orElseThrow(()-> new RuntimeException("El email o nombre del usuario ya esta en uso"));
    }
}
