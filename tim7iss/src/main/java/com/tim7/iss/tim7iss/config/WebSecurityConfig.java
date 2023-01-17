package com.tim7.iss.tim7iss.config;

import com.tim7.iss.tim7iss.auth.RestAuthenticationEntryPoint;
import com.tim7.iss.tim7iss.auth.TokenAuthenticationFilter;
import com.tim7.iss.tim7iss.services.UserService;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    // Handler za vracanje 401 kada klijent sa neodogovarajucim korisnickim imenom i lozinkom pokusa da pristupi resursu
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private TokenUtils tokenUtils;

    @Bean
    // UserDetails ugradjeni interfejs koji sadrzi metu findByUsername, koristi se dobavljanje korisnika iz base
    // UserService mora da implementira interfejs i pregazi metodu
    // User mora da implementira UserDetails interfejs i pregazi metode
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    // Koristi se za ekodovanje sifre
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Ovde se definise koji servis koristimo za dobavljanje korisnika iz baze kao i koji enkoder koristimo
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    // Registrujemo authentication manager koji ce da uradi autentifikaciju korisnika za nas
    // U sustini on ce pozvati automatsiki sirvis za pronalazenje korisnika kao i enkoder
    // On vrsi samo autentifikaciju korisnika
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    // Definisemo prava pristupa za zahteve ka odredjenim URL-ovima/rutama
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/unregisteredUser").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                .anyRequest().authenticated().and()
                .cors().and()

                // umetni custom filter TokenAuthenticationFilter kako bi se vrsila provera JWT tokena umesto cistih korisnickog imena i lozinke (koje radi BasicAuthenticationFilter)
                .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userDetailsService()), BasicAuthenticationFilter.class);


        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authenticationProvider(authenticationProvider());

        return http.build();
        // .antMatchers("/admin").hasRole("ADMIN") ili .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
    }

//    @Bean
//    // metoda u kojoj se definisu putanje za igorisanje autentifikacije
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // Autentifikacija ce biti ignorisana ispod navedenih putanja (kako bismo ubrzali pristup resursima)
//        // Zahtevi koji se mecuju za web.ignoring().antMatchers() nemaju pristup SecurityContext-u
//        // Dozvoljena POST metoda na ruti /auth/login, za svaki drugi tip HTTP metode greska je 401 Unauthorized
//        return (web) -> web.ignoring()
//                .antMatchers(HttpMethod.POST, "/api/user/login")
//                .antMatchers(HttpMethod.POST, "/api/unregisteredUser")
//
//
//                // Ovim smo dozvolili pristup statickim resursima aplikacije
//                .antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico",
//                        "/**/*.html", "/**/*.css", "/**/*.js");
//
//    }


}
