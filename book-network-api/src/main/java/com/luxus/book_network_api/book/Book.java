package com.luxus.book_network_api.book;

import com.luxus.book_network_api.common.BaseEntity;
import com.luxus.book_network_api.feedback.Feedback;
import com.luxus.book_network_api.history.BookTransactionHistory;
import com.luxus.book_network_api.user.User;
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
import java.util.List;

@Getter
@Setter
@SuperBuilder // Génère un constructeur avec un pattern de builder pour les classes héritées
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private  boolean shareable;

    //user relationship
    @ManyToOne
    @JoinColumn(name = "owner_id") //Nom de la colonne dans la table book
    private User owner;

    //feedback relationship
    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    //Book transaction history relationship
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate =  feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        // 3.25 -> 3.0 || 3.75 -> 4.0
        double roundedRate = Math.round(rate * 10.0) / 10.0;
        return roundedRate;
    }

}
