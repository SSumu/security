package com.springbootacademy.security.config;

import com.springbootacademy.security.model.User;
import com.springbootacademy.security.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component // This tells the container that this class is a bean.
// Then, if this class is definitely an authentication provider, we have an interface called authentication provider supplied by Spring security.
public class BankUsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

//  Authentication occurs within this method.
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        // After a request is received, we check to see if a user corresponding to the username exists in our system. email is the username.
        List<User> users = userRepo.findByEmail(username); // Then, after this email(username) is sent to the database, if there is a user associated with it, it will be assigned to the users. There must be one user. I got this List, so I would not have to check it, and it would not be null. It is easy for me to check.
//        If a user exists, it is OK to check if the password of that user is correct. We never send the password directly to the database to check it. Now we can directly provide the username and password to the userRepo to check both. We usually do not send a password like that. We fetch an existing one like this. Now this will bring up the entire object for users if there is one associated with this username. That means all the complete details of the user in the user table will come to users. A hash will come up. We need to decrypt that hash again and see if the password we have entered matches the password in the users.
        if (users.size() > 0){ // If the value of users.size() is greater than 0, there is a user.
//          There is only one user in the username. If there is, there is only one. Because one cannot have many users with one username. The first one in users. That user is at index 0.
            if (passwordEncoder.matches(pwd,users.get(0).getPassword())){
//              We need to return a UsernamePasswordAuthenticationToken. Then we need to set the authorities for the UsernamePasswordAuthenticationToken. Authorities are what access paths this user has. What level of user is it? Is it an admin or a user? So, which person is it? We need to mention and send it in this UsernamePasswordAuthenticationToken.
                List<GrantedAuthority> authorities = new ArrayList<>(); // Create an object called authorities.
                authorities.add(new SimpleGrantedAuthority(users.get(0).getRole())); // Where do authorities come from? Now I have fetched this user. I fetched it from the database. That user's data is at index 0. If there is a user. getRole() is at index 0 of get(0). Then we set the user's authority or role as admin here. If the role is admin, we can specify which paths can be accessed.
                return new UsernamePasswordAuthenticationToken(username,pwd,authorities);
            }else {
                throw new BadCredentialsException("Invalid Password");
            }
        }else {
            throw new BadCredentialsException("No User Registered with this details");
        }

//        return null; // This was deleted in the video because it was returned at the top.
    }

//  The supports method has its own internal process. The authentication is passed here by itself. We need to return a token.
    @Override
    public boolean supports(Class<?> authentication) {
//        return false; // false is removed.
//      We need to return a token.
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
