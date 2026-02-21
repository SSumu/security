package com.springbootacademy.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration // When we write a configuration class by putting this annotation, we put it to the container as a bean after it was annotated.
public class ProjectSecurityConfig {

    @Bean // @Bean creates a bean by defining like this.
//  All these paths first come inside the brackets of defaultSecurityFilterChain(). After we call something. As if we called the link from the address bar of the browser. That is what the http parameter created by the HttpSecurity class in defaultSecurityFilterChain() is to catch that incoming path.
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{ // This is an interface.
//  I am saying, authorize the requests that come in this http request.
//        http.authorizeRequests() // This authorizeRequests() which video showed was deprecated in the Spring Boot 3 / Spring Security 6.1+. Instead of this method, now it uses authorizeHttpRequests().
//        http.csrf().and().cors().disable() // This csrf() method was deprecated Old (Deprecated) Style which the video has shown.
        http
                .csrf(csrf -> csrf.disable()) // This is the New (Recommended) Lambda Style is used in current version. csrf() protection is given from the Spring security by default.
//                .cors(cors -> cors.disable()) // This is the New (Recommended) Lambda Style is used in current version.
//                Spring moved to this style in csrf and cors for:
//                      Better readability
//                      Type safety
//                      Consistency with modern Java config
                .authorizeHttpRequests( auth -> auth
//                .antMatchers() // This tells us which paths require authenticate. In the video, it shows that this method(antMatchers()) is used to catch those paths. But it was removed from current Spring Boot versions. So current versions use requestMatchers(). These matcher methods were unified into requestMatchers() in Spring Security 6. This auth means that authorizationManagerRequestMatcherRegistry.
                .requestMatchers("/api/v1/account/my-account","/api/v1/loan/my-loan").authenticated() // Pattern must be written inside "". Pattern of the AccountController is /my-account. authenticated() means that this pattern requires an authentication. We can give as many paths as we want like this, separated by commas(,). The full paths(end-points) must be correctly given. Otherwise, it cannot be identified.
//                .requestMatchers("/api/v1/account/my-account","/api/v1/loan/my-loan").hasRole("admin")
                .requestMatchers("/api/v1/notice/my-notice","/api/v1/user/register").permitAll() // We catch another path. permitAll() means that this pattern or path has been given all the permissions. So anyone is permitted to access the my-notice. The full path(end-point) must be correctly given. Otherwise, it cannot be identified.
//                .requestMatchers("/my-notice").denyAll() // denyAll() means that this pattern or path must not be given any permissions as soon as this pattern or path has been accessed.
//                .and().formLogin() // Both and() and form() are deprecated in the current versions. No parameters → Deprecated since 6.1. This is the old chained DSL style that required .and() so this was deprecated.
//                It is limited to paths. Now, do not we need to give permitAll() or authenticated() to this register? We need to give permitAll(). But authenticated() is not required. SignUp does not ask for anything (a key or a password). But it does ask when logging in.
        )
                .formLogin(form -> {} // This formLogin() tells us to log in if the above authentications are successful.
        )
                .httpBasic(basic -> {});
    // This is the Lambda DSL version. This way is used in the current versions.
//      Benefits of new DSL uses functional lambdas ( Related to the formLogin() ):
//        # Type-safe
//        # Cleaner
//        # No .and() needed
//        # Better Kotlin compatibility
//      The video shows the Old (Deprecated) Style. Spring Security 6.1 deprecated the no-arg DSL method. It’s marked for removal in a future version — so you should migrate to the new lambda / Customizer style configuration. So here, I used the new lambda / Customizer style configuration.
        return http.build(); // We have to return the type of SecurityFilterChain. So this http.build() means build the above written part inside the defaultSecurityFilterChain() and return it.
    }

//  This explains how to keep a user in memory in Spring.
//  This is the old authentication code and withDefaultPasswordEncoder() was deprecated in current versions.
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(){
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("12345")
//                .authorities("admin")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin); // Returns the new object is InMemoryUserDetailsManager.
//    }

//    Once you have the UserDetails, you need to add the UserDetailsService to the bean to make this work.
//    If you try to do this with the things that Spring provides.
//    @Bean
//    public UserDetailsService jdbcUserDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

//  Creating a bean by @Bean means we put this into the container.
    @Bean
    public PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder(); // This is what is usually used to convert passwords. This is good for password encryption. Passwords must be encoded before it store in databases.
    }

//  passwordEncoder is a dependency.

//    Why it was deprecated
//    withDefaultPasswordEncoder():
//      Uses a hard-coded, in-memory password encoder
//      Was intended only for demos / quick tests
//      Encourages bad security practices (plain/default encoding setup)
//      Doesn’t follow modern password-hashing recommendations (e.g., BCrypt with configurable strength)
//    So Spring Security removed it to push developers toward explicit password encoding.

//  This is the modern replacement code for the above authentication process.
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(encoder.encode("12345"))
//                .roles("admin")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin); // Returns the new object is InMemoryUserDetailsManager.
//    }

//  Once you have the UserDetails, you need to add the UserDetailsService to the bean to make this work.
//  If you try to do this with the things that Spring provides.
//    @Bean
//    public UserDetailsService jdbcUserDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

}

// This is a configuration class.
// Its type is that it has an interface that we use to authorize incoming requests. That is, to secure these parts. Which parts should this go to? Which parts should we give access to? Which parts should we not give access to? Here are some things like that we use the ProjectSecurityConfig configuration class.

// Why authorizeRequests() was deprecated
// Spring replaced the old authorization system with a newer one based on AuthorizationManager API which:
// Simplifies configuration
// Improves performance (authentication lookup only when needed)
// Supports bean-based customization

// Why it was deprecated
// Spring Security is moving away from the old chained DSL style toward lambda-based configuration because it:
// Is more type-safe
// Improves readability
// Aligns with modern Spring configuration pattern