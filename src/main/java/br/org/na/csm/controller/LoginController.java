package br.org.na.csm.controller;

import br.org.na.csm.dto.login.LoginRequestDto;
import br.org.na.csm.dto.login.LoginResponseDto;
import br.org.na.csm.dto.login.TelefoneDto;
import br.org.na.csm.model.Usuario;
import br.org.na.csm.repository.UsuarioRepository;
import br.org.na.csm.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<LoginResponseDto> efetuarLogin(
            @RequestBody LoginRequestDto requestDto
            ) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @GetMapping
    public ResponseEntity<TelefoneDto> buscarTelefone() {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdDesc();
        return ResponseEntity.ok(new TelefoneDto(usuario.getTelefone()));
    }
}
