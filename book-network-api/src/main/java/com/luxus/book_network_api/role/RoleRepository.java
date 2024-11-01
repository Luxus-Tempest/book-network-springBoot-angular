package com.luxus.book_network_api.role;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * L'interface RoleRepository étend JpaRepository<Role, Integer>, ce qui signifie qu'elle hérite des fonctionnalités de base pour interagir avec la base de données.
 * Ici, Role est l'entité que nous manipulons, et Integer est le type de la clé primaire de cette entité (identifiant).
 * @RoleRepository extends JpaRepository<Role, Integer> 
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * @Optional <Role> findByName(String role);
     * Cette méthode est utilisée pour rechercher un rôle dans la base de données par son nom.
     * Elle retourne un Optional<Role>, ce qui signifie qu'il peut y avoir un rôle correspondant ou non (pour éviter les erreurs si le rôle n'existe pas).
     */
    Optional<Role> findByName(String role);
}
