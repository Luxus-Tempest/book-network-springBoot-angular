package com.luxus.book_network_api.auth;

import com.luxus.book_network_api.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luxus.book_network_api.email.EmailService;
import com.luxus.book_network_api.email.EmailTemplateName;
import com.luxus.book_network_api.role.RoleRepository;
import com.luxus.book_network_api.user.User;
import com.luxus.book_network_api.user.Token;
import com.luxus.book_network_api.user.TokenRepository;
import com.luxus.book_network_api.user.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.IllegalStateException;
import java.util.HashMap;
import java.util.List;
import java.security.SecureRandom;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    private final AuthenticationManager authenticationManager;


    public void register(RegistrationRequest request) throws MessagingException {

            //RECUPERER LE ROLE USER DE LA BDD
            var userRole = roleRepository.findByName("USER")

                //TO DO BETTER WAY - BETTER EXECPTION HANDLING
                .orElseThrow(() -> new IllegalStateException("Role de l utisateur non défini"));

            var user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .roles(List.of(userRole))
                    .build();

            userRepository.save(user);
            sendValidationEmail(user);
            }

            /**
             * Cette fonction envoie un email de validation
             * @param user
             * @throws MessagingException
             */
            private void sendValidationEmail(User user) throws MessagingException {

                var newToken = generateAndSendActivationToken(user);

                //send mail
                emailService.sendEmail(
                    user.getEmail(),
                    user.getFullName(),
                    EmailTemplateName.ACTIVATE_ACCOUNT,
                    activationUrl,
                newToken,
                "Account activation"
                );

        }


        /**
         * Cette methode crée  un token pour un user qui est
         * en attente de validation pour finaliser la création de son compte
         * @param user
         * @return  generatedTken
         */
        private String generateAndSendActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
        }




        /**
         * Cette fonction genere un code aleatoire de 6 chiffres
         * @param length
         * @return un code de length chiffres
         */
        public String generateActivationCode(int length) {
            String characters = "0123456789";
            StringBuilder code = new StringBuilder(length);
            SecureRandom secureRandom = new SecureRandom();

            for (int i = 0; i < length; i++) {
                int index = secureRandom.nextInt(characters.length());
                code.append(characters.charAt(index));
            }
            return code.toString();
        }


        //------------------------ authenticate ----------------
    /**
     * Authentifie un utilisateur en fonction de l'email et du mot de passe fournis.
     *
     * @param request la requête d'authentification contenant l'email et le mot de passe de l'utilisateur
     * @return une AuthenticationResponse contenant le token JWT généré
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


        /**
         * Active le compte d'un utilisateur en fonction du token fourni.
         *
         * @param token le token d'activation du compte
         */

//        @Transactional
        public void activateAccount(String token) throws MessagingException {
            var savedToken = tokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Token invalide"));

            if (savedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                sendValidationEmail(savedToken.getUser());
                throw new IllegalStateException("Token expiré. Un nouveau token a été envoyé à votre adresse email.");
            }

//            var user = savedToken.getUser();
            var user = userRepository.findById(savedToken.getUser().getId())
                    .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));
            user.setEnabled(true);
            userRepository.save(user);
            savedToken.setValidatedAt(LocalDateTime.now());
            tokenRepository.save(savedToken);
        }

}
