package cloud.daodao.license.server.config;

import cloud.daodao.license.server.filter.TokenAuthenticationFilter;
import cloud.daodao.license.server.handler.AppAccessDeniedHandler;
import cloud.daodao.license.server.handler.AppAuthenticationEntryPoint;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private AppAccessDeniedHandler appAccessDeniedHandler;

    @Resource
    private AppAuthenticationEntryPoint appAuthenticationEntryPoint;

    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    private static final String[] PERMIT_PATHS = {
            "/",
            "/favicon.ico",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**",
            "/login",
            "/api/user/login",
            "/api/license/introspect",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .cors(cors -> cors.configurationSource(request -> CorsConfig.config()))

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
//                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(PERMIT_PATHS)
                                .permitAll()
                                .anyRequest()
                                .hasRole("ADMIN")
                        // .authenticated()
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(appAuthenticationEntryPoint)
                        .accessDeniedHandler(appAccessDeniedHandler)
                )

                .formLogin(Customizer.withDefaults())

                .httpBasic(Customizer.withDefaults())

                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

        ;
        return http.build();
    }

}
