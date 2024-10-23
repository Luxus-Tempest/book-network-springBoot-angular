package com.luxus.book_network_api.auth;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * Ce controlleur gère l'inscription et la connection du user
 * */
@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
// Dans applicaton.yml on a: context-path:  /api/v1/: Donc le chemin est : localhost:5234/api/v1/auth

public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
        @RequestBody @Valid RegistrationRequest request
        ) throws MessagingException {
        
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @GetMapping("/activate-account")
    public void  activate(
        @RequestParam String token
    ) throws MessagingException {

        service.activateAccount(token);
    }
    
}
