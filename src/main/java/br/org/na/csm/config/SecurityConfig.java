package br.org.na.csm.config;

import br.org.na.csm.security.JwtAuthenticationFilter;
import br.org.na.csm.service.UsuarioSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // Indica que esta é uma classe de configuração.
@EnableWebSecurity // Habilita a segurança web do Spring Security.
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UsuarioSecurityService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UsuarioSecurityService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Define a cadeia de filtros de segurança. Aqui configuramos o comportamento geral da segurança HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                // Desabilita a proteção CSRF, pois não usamos sessões baseadas em cookies.
                .csrf(AbstractHttpConfigurer::disable)
                // Configura a política de criação de sessão para STATELESS (sem estado).
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura as regras de autorização para as requisições HTTP.
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público aos endpoints de autenticação.
                        .requestMatchers(HttpMethod.GET,
                                "/grupos",
                                "/materiais/grupos/venda/*",
                                "/materiais/imagem/*",
                                "/login"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/login",
                                "/pedidos"
                        ).permitAll()
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/cadastrar",
                                "/editar",
                                "/efetuar-pedido",
                                "/confirmar-pedido",
                                "/login",
                                "/*.js",
                                "/*.css",
                                "/*.ico",
                                "/*.png"
                        ).permitAll()
                        // Exige autenticação para qualquer outra requisição.
                        .anyRequest().authenticated()
                )
                // Define o provedor de autenticação que vamos usar.
                .authenticationProvider(authenticationProvider())
                // Adiciona nosso filtro JWT antes do filtro padrão de username/password.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Define o provedor de autenticação. Ele usa o UserDetailsService para buscar usuários
     * e o PasswordEncoder para verificar senhas.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Como encontrar o usuário.
        authProvider.setPasswordEncoder(passwordEncoder()); // Como verificar a senha.
        return authProvider;
    }

    /**
     * Expõe o AuthenticationManager como um Bean para ser usado em outras partes da aplicação,
     * como no nosso controller de autenticação.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define o codificador de senhas. Usamos o BCrypt, que é um algoritmo forte e padrão de mercado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
