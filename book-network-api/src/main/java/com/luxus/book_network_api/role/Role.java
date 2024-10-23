package com.luxus.book_network_api.role;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luxus.book_network_api.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;
import java.util.Collection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Considerer la classe User comme une entité ou table d'un BD
 */
@Entity
@Table(name= "role")
/**
 * Pour permettre le suivis des date de création et de modification de l'utilisateur
 * on utilise : @EntityListeners(AuditingEntityListener.class) 
 */
@EntityListeners(AuditingEntityListener.class) 
public class Role {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;

    /**
     * Un role peut être attribué à plusieurs utilisateurs
     * Dans le classe USER nous avons: 
     * @ManyToMany(fetch = FetchType.EAGER)
     * private List<Role> roles;
     */
    /**
    * Relation Many-to-Many entre User et Role :
    * 
    * - Un utilisateur peut avoir plusieurs rôles, et un rôle peut être attribué à plusieurs utilisateurs.
    * - Dans cette classe `Role`, nous définissons la liste des utilisateurs (`List<User> users`) 
    *   associés à chaque rôle.
    * 
    * @JsonIgnore est utilisé pour éviter la sérialisation de la liste des utilisateurs dans le JSON
    * afin d'éviter les références circulaires qui provoqueraient une boucle infinie lors de la 
    * sérialisation d'un objet User ou Role.
    * 
    * Exemple de problème évité :
    * - Un objet `User` contient une liste de `Role`.
    * - Chaque `Role` contient une liste de `User` associés.
    * - Sans @JsonIgnore, cela créerait une boucle infinie lors de la conversion en JSON,
    *   car chaque `User` et `Role` se référeraient mutuellement.
    * 
    * En résumé, @JsonIgnore permet d'éviter ces boucles infinies et de limiter la quantité de 
    * données renvoyées dans les réponses JSON.
    */
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false, updatable = true) //Ne pas déclarer ce champ à la création
    private LocalDateTime lastModifiedDate;

}
