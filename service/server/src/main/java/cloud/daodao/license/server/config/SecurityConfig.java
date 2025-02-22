package cloud.daodao.license.server.config;

import cloud.daodao.license.common.error.AppError;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.model.Serializer;
import cloud.daodao.license.server.filter.TokenAuthenticationFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private AppConfig appConfig;

    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    private static final String[] PERMIT_PATHS = {
            "/",
            "/favicon.ico",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/actuator/**",
            "/api/user/login"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .cors(cors -> cors.configurationSource(request -> CorsConfig.config()))

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(PERMIT_PATHS).permitAll()
                                .anyRequest()
                                .hasRole("ADMIN")
                        // .authenticated()
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                            response.getWriter().write(Serializer.toJson(new Response<>(AppError.TOKEN_ERROR)));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.getWriter().write("Access Denied");
                        })
                )

                .formLogin(Customizer.withDefaults())

                .httpBasic(Customizer.withDefaults())

                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

        ;
        return http.build();
    }

}
