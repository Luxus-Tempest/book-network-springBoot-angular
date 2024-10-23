 package com.luxus.book_network_api.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
/*
 * Considerer la classe User comme une entité ou table d'un BD
 */
@Entity
public class Token {

    @Id
    @GeneratedValue
    private Integer id;

    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;

    /*
    * La relation ManyToOne signifie qu'il peut y avoir plusieurs instances de Token pour une seule instance de User
    * En d'autres termes, plusieurs tokens peuvent être liés à un même utilisateur, 
    * mais chaque token appartient à un seul utilisateur
     */
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
