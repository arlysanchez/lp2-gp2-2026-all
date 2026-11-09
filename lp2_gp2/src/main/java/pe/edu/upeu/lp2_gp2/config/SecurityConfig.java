package pe.edu.upeu.lp2_gp2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.upeu.lp2_gp2.exception.CustomAccessDeniedHandler;
import pe.edu.upeu.lp2_gp2.exception.CustomAuthenticationEntryPoint;
import pe.edu.upeu.lp2_gp2.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private  final JwtAuthenticationFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, AuthenticationProvider authenticationProvider, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(org.springframework.security.config.Customizer.withDefaults()) //conexion a angular
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //1. rutas publicas
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("swagger-ui/**","/v3/api-docs/**").permitAll()
                        //productos
                        .requestMatchers(HttpMethod.GET,"/api/v1/productos/**").permitAll() //cliente ve el catalogo
                        .requestMatchers("/api/v1/productos/**").hasAuthority("ADMIN") //SOLO EL ADMIN PUEDE crear, editar y borrar
                        //pedidos
                        .requestMatchers("/api/v1/pedidos/**").hasAuthority("CLIENTE")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exeption-> exeption
                        .authenticationEntryPoint(authenticationEntryPoint)//maneja el error 401
                        .accessDeniedHandler(accessDeniedHandler) //maneja el eror 403
                );
        return http.build();
    }

}
