package com.management.authentication.RESTController;


import com.management.authentication.model.ERole;
import com.management.authentication.model.Role;
import com.management.authentication.model.User;
import com.management.authentication.payload.request.JwtResponse;
import com.management.authentication.payload.request.LoginRequest;
import com.management.authentication.payload.request.MessageResponse;
import com.management.authentication.payload.request.SignupRequest;
import com.management.authentication.repository.RoleRepository;
import com.management.authentication.repository.UserRepository;
import com.management.authentication.security.jwt.JwtUtils;
import com.management.authentication.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

@Autowired
    AuthenticationManager authenticationManager;
@Autowired
    UserRepository userRepository;
@Autowired
    RoleRepository roleRepository;
@Autowired
    PasswordEncoder encoder;
@Autowired
    JwtUtils jwtUtils;


@PostMapping("/signin")
public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){

    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtUtils.generateToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
            .collect(Collectors.toList());


    return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getId(),userDetails.getUsername(),roles));
}

@PostMapping("/signup")
public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
    if(userRepository.existsByUsername(signupRequest.getUsername())){
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken !"));
    }

    User user = new User(signupRequest.getUsername(),encoder.encode((signupRequest.getPassword())));

    Set<String> strRoles = signupRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if(strRoles == null){

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(()-> new RuntimeException("Error: Role is not fount."));
        roles.add(userRole);

    }else{
        strRoles.forEach(role -> {
          switch (role){
              case "admin":
                  Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                          .orElseThrow(()-> new RuntimeException("Error: Role is not found."));

                  roles.add(adminRole);
                  break;
              case "mod":
                  Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                          .orElseThrow(()-> new RuntimeException("Error: Role is not found."));

                  roles.add(modRole);
                  break;
              default:
                  Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                  roles.add(userRole);
          }
        });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return  ResponseEntity.ok(new MessageResponse("User Registerd sucessfully!"));

}

}
