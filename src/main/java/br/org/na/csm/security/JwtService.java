package br.org.na.csm.security;

import br.org.na.csm.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    // Injeta o valor da nossa propriedade de configuração
    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private Long expiration;

    private final String ISSUER = "jwt-auth-api";

    /**
     * Gera um token JWT para o usuário fornecido.
     */
    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER) // Emissor do token
                    .withSubject(usuario.getLogin()) // Assunto (dono) do token (nosso usuário)
                    .withExpiresAt(generateExpirationDate()) // Data de expiração
                    .sign(algorithm); // Assina o token com o algoritmo
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    /**
     * Valida um token JWT e retorna o email (subject) do usuário se o token for válido.
     */
    public String validateTokenAndRetrieveSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER) // Verifica se o emissor é o mesmo
                    .build()
                    .verify(token) // Verifica a assinatura e a validade
                    .getSubject(); // Retorna o subject (email do usuário)
        } catch (JWTVerificationException exception) {
            // Se o token for inválido (expirado, assinatura incorreta, etc.), retorna uma string vazia.
            return "";
        }
    }

    /**
     * Calcula a data/hora de expiração do token.
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusMinutes(expiration) // Adiciona o tempo de expiração
                .toInstant(ZoneOffset.of("-03:00")); // Converte para Instant com fuso de Brasília
    }
}
