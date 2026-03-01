package com.springbootacademy.security.controller;

import com.springbootacademy.security.model.User;
import com.springbootacademy.security.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class LoginController {

    // Injecting the passwordEncoder dependency to this class using @Autowired.
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register") // So this is the register method. The register method is used to insert data into the database. Here we are going to register a user. A registration comes for a user model.
    public ResponseEntity<String> registerUser(@RequestBody User user){
//      user is an object. A password has come from the user object. The password is hashed and saved separately. Hash the password means encrypt the password.
        ResponseEntity response = null; // response is the reference or the variable. responseEntity is not needed and only response is enough.
        try {
            String hashPassword = passwordEncoder.encode(user.getPassword()); // hashPassword is a variable. But assigning the password here like this means that the password will be saved as is ( without encryption ). passwordEncoder.encode() encodes or encrypts the password. user.getPassword() returns the password as a String. So this encodes or encrypts the password of the user object and assign it to the hashPassword.
            user.setPassword(hashPassword); // Now user object has the encoded password value.
            user.setRole("ROLE_"+user.getRole()); // This is to set ROLE_ for the roles in the database.
            User savedUser = userRepo.save(user); // Now we have to save the user object to the database.
            // We usually do not separately create the service package and other necessary packages for this task and code them. Most of the time, we code them here. We do not separately create the service package for the registration and login related things. We code them together. It's really easy. Let's call a repository here and save it. Then, if necessary, we can create a service package and code these things separately without coding them here. Or we can code them here.
            if (savedUser.getId()>0){
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Given User Details Are Successfully Registered");
            } // This is to return the response.

        }catch (Exception ex){
            response = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("An Exception occurred due to " + ex.getMessage());

        }
        return response;
    }
}

// There is no need to create a DTO for this because that data will come in.
// Let's say we're going to sign up on Facebook. We're trying to create a new account on Facebook. The /register method is what the frontend calls like this. When the call comes in, we send a username and a password or something like that. Then when we work inside this system from here on out, when we save it to the database, we need to encrypt and encode the password and send it. Because if the password is now 1234, if it is sent to the database as 1234, then those who have access to the database can get this password. Then the security is low. If the password is visible somewhere, then the security is low.