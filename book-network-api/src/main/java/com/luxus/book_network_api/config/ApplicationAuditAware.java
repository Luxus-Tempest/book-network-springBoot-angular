package com.luxus.book_network_api.config;

import com.luxus.book_network_api.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Cette classe permet de récupérer les informations de l'auditeur actuel.
 * Elle gère les informations d'audit de l'application.
 *
 * Elle implémente l'interface :
 * @AuditorAware<Integer> qui définit une méthode pour retourner l'auditeur actuel.
 *
 * "@param Integer> le type de l'auditeur actuel
 * "@param <Integer> (dans notre cas, l'auditeur actuel est un entier représentant l'identifiant de l'utilisateur)
 *
 * @return Optional<Integer> un objet optionnel contenant l'identifiant de l'utilisateur actuel
 */
public class ApplicationAuditAware implements AuditorAware<Integer> {
    /**
     * @return Optional.empty() retourne un objet optionnel vide
     */
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}
