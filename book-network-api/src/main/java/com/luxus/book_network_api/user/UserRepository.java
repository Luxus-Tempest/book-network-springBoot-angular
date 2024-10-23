package com.luxus.book_network_api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


/*
 * L'interface UserRepository étend JpaRepository<User, Integer>, ce qui signifie qu'elle hérite des fonctionnalités de base pour interagir avec la base de données.
 * Ici, User est l'entité que nous manipulons, et Integer est le type de la clé primaire de cette entité (identifiant).
 */
public interface UserRepository extends JpaRepository<User, Integer> {

        /*
     * Optional<User> findByEmail(String email);
     * Cette méthode est utilisée pour rechercher un utilisateur dans la base de données par son email.
     * Elle retourne un Optional<User>, ce qui signifie qu'il peut y avoir un utilisateur correspondant ou non (pour éviter les erreurs si l'utilisateur n'existe pas).
     */
    Optional<User> findByEmail(String email);

}
