package guru.sfg.watery.config;

import guru.sfg.watery.security.SfgPasswordEncoderFactories;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Creates a {@link DelegatingPasswordEncoder} with default mappings. Additional
     * mappings may be added and the encoding will be updated to conform with best
     * practices. However, due to the nature of {@link DelegatingPasswordEncoder} the
     * updates should not impact users. The mappings current are:
     *
     * <ul>
     * <li>bcrypt - {@link BCryptPasswordEncoder} (Also used for encoding)</li>
     * <li>ldap - {@link org.springframework.security.crypto.password.LdapShaPasswordEncoder}</li>
     * <li>MD4 - {@link org.springframework.security.crypto.password.Md4PasswordEncoder}</li>
     * <li>MD5 - {@code new MessageDigestPasswordEncoder("MD5")}</li>
     * <li>noop - {@link org.springframework.security.crypto.password.NoOpPasswordEncoder}</li>
     * <li>pbkdf2 - {@link Pbkdf2PasswordEncoder}</li>
     * <li>scrypt - {@link SCryptPasswordEncoder}</li>
     * <li>SHA-1 - {@code new MessageDigestPasswordEncoder("SHA-1")}</li>
     * <li>SHA-256 - {@code new MessageDigestPasswordEncoder("SHA-256")}</li>
     * <li>sha256 - {@link org.springframework.security.crypto.password.StandardPasswordEncoder}</li>
     * <li>argon2 - {@link Argon2PasswordEncoder}</li>
     * </ul>
     *
     * @return the {@link PasswordEncoder} to use
     */
    @Bean
    PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance();
//        return new LdapShaPasswordEncoder();
//        return  new StandardPasswordEncoder();
//        return  new BCryptPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
          return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/waters/find","/waters*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/water/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/waterUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();


    }

    /**
     * Used by the default implementation of {@link #authenticationManager()} to attempt
     * to obtain an {@link AuthenticationManager}. If overridden, the
     * {@link AuthenticationManagerBuilder} should be used to specify the
     * {@link AuthenticationManager}.
     *
     * <p>
     * The {@link #authenticationManagerBean()} method can be used to expose the resulting
     * {@link AuthenticationManager} as a Bean. The {@link #userDetailsServiceBean()} can
     * be used to expose the last populated {@link UserDetailsService} that is created
     * with the {@link AuthenticationManagerBuilder} as a Bean. The
     * {@link UserDetailsService} will also automatically be populated on
     * {@link HttpSecurity#getSharedObject(Class)} for use with other
     * {@link SecurityContextConfigurer} (i.e. RememberMeConfigurer )
     * </p>
     *
     * <p>
     * For example, the following configuration could be used to register in memory
     * authentication that exposes an in memory {@link UserDetailsService}:
     * </p>
     *
     * <pre>
     * &#064;Override
     * protected void configure(AuthenticationManagerBuilder auth) {
     * 	auth
     * 	// enable in memory based authentication with a user named
     * 	// &quot;user&quot; and &quot;admin&quot;
     * 	.inMemoryAuthentication().withUser(&quot;user&quot;).password(&quot;password&quot;).roles(&quot;USER&quot;).and()
     * 			.withUser(&quot;admin&quot;).password(&quot;password&quot;).roles(&quot;USER&quot;, &quot;ADMIN&quot;);
     * }
     *
     * // Expose the UserDetailsService as a Bean
     * &#064;Bean
     * &#064;Override
     * public UserDetailsService userDetailsServiceBean() throws Exception {
     * 	return super.userDetailsServiceBean();
     * }
     *
     * </pre>
     *
     * @param auth the {@link AuthenticationManagerBuilder} to use
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{bcrypt}$2a$10$HQbYwdw6AP5nmkTTfOpGh.2ahlSz.UXvEQA6M4CXTLkDrK0ZNAina")
//                .password("1819626f312a4cd55f1e83bbe95d5413cb3f02e07c12bdfa319567019ee5363637ceba07ab9cc2e5")
//                .password("{SSHA}H0AMNQn3XaUvvuSJvyd4sZ9Zb/QPrOcstt0lig==")
//                .password("{noop}guru")
//                .password("guru")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}d00474a98723afa963d6eead60edf6210648b47c00fd9c3491b273c3fbf13b63a0d1d8ba2c248f7c")
//                .password("$2a$10$4XMVzUKbgNEvXygLteOq3e62qDuVsVBDEt6YSx6O8GPUy7JlyWjKa")
//                .password("f0db6ef825ac0965d19f4427788bb18e289dd45714bd6f2e70e540922bb74c5f8012d18dc9e020e5")
//                .password("{SSHA}H0AMNQn3XaUvvuSJvyd4sZ9Zb/QPrOcstt0lig==")
//                .password("{noop}password")
//                .password("password")
                .roles("USER");

        auth.inMemoryAuthentication()
                .withUser("scott")
                .password("{bcrypt15}$2a$15$DLiP65r9AqXZlpK6DvyuE.7/o7qyCfFH71PB9FtroeK98ypBIB7KS")
//                .password("{ldap}{SSHA}MJjG3r3FwUEr5qqhACnuNjA28CeILn2mZGXFFw==")
//                .password("{noop}tiger")
//                .password("tiger")
                .roles("CUSTOMER");
    }

    /**
     * Allows modifying and accessing the {@link UserDetailsService} from
     * {@link #userDetailsServiceBean()} without interacting with the
     * {@link ApplicationContext}. Developers should override this method when changing
     * the instance of {@link #userDetailsServiceBean()}.
     *
     * @return the {@link UserDetailsService} to use
     */
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//
//    }
}
