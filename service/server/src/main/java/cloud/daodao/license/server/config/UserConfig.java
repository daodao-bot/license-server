package cloud.daodao.license.server.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    @Resource
    private AppConfig appConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        User.UserBuilder userBuilder = User.builder().passwordEncoder(passwordEncoder()::encode);
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        String adminUsername = appConfig.getAdminUsername();
        String adminPassword = appConfig.getAdminPassword();
        String[] adminRoles = appConfig.getAdminRoles();
        UserDetails userDetails = userBuilder.username(adminUsername)
                .password(adminPassword)
                .roles(adminRoles)
                .build();
        userDetailsManager.createUser(userDetails);
        return userDetailsManager;
    }

}
