package me.zuif.teashop.config;


import me.zuif.teashop.repository.UserRepository;
import me.zuif.teashop.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;

    @Autowired
    public WebSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/about", "/", "/home", "/index", "/help", "/register", "/cart").permitAll();
        http.authorizeRequests().antMatchers("/user/edit/**", "/user", "/rating/**", "/cart/checkout", "/order/list", "order/about/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/admin/**", "/tea/new/**", "/tea/edit/**", "/tea/delete/**", "/user/delete/**", "/order/delete/**", "user/list").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().and().formLogin().loginPage("/login").permitAll()
                .and().logout().invalidateHttpSession(true).
                clearAuthentication(true).
                logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                .and().headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("templates/error/**", "/static/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());

    }
}