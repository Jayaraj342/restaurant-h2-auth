package surabi.restaurants.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.jdbcAuthentication().dataSource(dataSource).withDefaultSchema()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser(
                        User.withUsername("user").password(encoder.encode("user@password")).roles("USER_ROLE")
                )
                .withUser(
                        User.withUsername("user1").password(encoder.encode("user1@password")).roles("USER_ROLE")
                )
                .withUser(
                        User.withUsername("admin").password(encoder.encode("admin@password")).roles("ADMIN_ROLE")
                );
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/surabi-restaurant/view-menu", "/surabi-restaurant/select-menu-with-bill", "/surabi-restaurant/last-bill")
                .hasRole("USER_ROLE")
                .antMatchers("/surabi-restaurant/last-one-day-bill", "/surabi-restaurant/last-one-month-bill")
                .hasRole("ADMIN_ROLE")
                .antMatchers("/").permitAll()
                .and()
                .formLogin();
    }
}
