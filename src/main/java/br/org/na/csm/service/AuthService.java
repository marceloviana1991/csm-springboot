package br.org.na.csm.service;

import br.org.na.csm.dto.login.LoginRequestDto;
import br.org.na.csm.dto.login.LoginResponseDto;
import br.org.na.csm.repository.UsuarioRepository;
import br.org.na.csm.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public LoginResponseDto login(LoginRequestDto request) {
        // Autentica o usuário usando o AuthenticationManager do Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.login(),
                        request.password()
                )
        );

        // Se a autenticação for bem-sucedida, busca o usuário e gera o token
        var user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos."));

        var jwtToken = jwtService.generateToken(user);
        return new LoginResponseDto(jwtToken);
    }
}
