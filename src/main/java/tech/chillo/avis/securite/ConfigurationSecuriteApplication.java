package tech.chillo.avis.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tech.chillo.avis.service.UtilisateurService;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class ConfigurationSecuriteApplication{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return
                httpSecurity
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                authorize ->
                                        authorize
                                                .requestMatchers(POST,"/inscription").permitAll()
                                                .requestMatchers(POST,"/activation").permitAll()
                                                .anyRequest().authenticated()
                        ).build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {


        return  authenticationConfiguration.getAuthenticationManager();
    }
   /* @Bean
    public UserDetailsService userDetailsService(){

        return new UtilisateurService();
    }*/
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoauthenticationProvider = new DaoAuthenticationProvider();

            daoauthenticationProvider.setPasswordEncoder(this.passwordEncoder());
            daoauthenticationProvider.setUserDetailsService(userDetailsService);

        return daoauthenticationProvider;
    }





}
