package com.luxus.book_network_api.feedback;

import com.luxus.book_network_api.book.Book;
import com.luxus.book_network_api.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
/**
 * Représente un retour d'information avec une note et un commentaire.
 */
@Getter
@Setter
@SuperBuilder // Génère un constructeur avec un pattern de builder pour les classes héritées
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

    /**
     * La note donnée dans le retour d'information, allant de 1 à 5.
     */
    private Double note;

    /**
     * Le commentaire fourni dans le retour d'information.
     */
    private String comment;

    //book relationship
    @ManyToOne
    @JoinColumn(name = "book_id") //Nom de la colonne dans la table feedback
    private Book book;
}
