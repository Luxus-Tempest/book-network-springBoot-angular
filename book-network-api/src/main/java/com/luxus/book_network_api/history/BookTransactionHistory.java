package com.luxus.book_network_api.history;

import com.luxus.book_network_api.book.Book;
import com.luxus.book_network_api.common.BaseEntity;
import com.luxus.book_network_api.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder // Génère un constructeur avec un pattern de builder pour les classes héritées
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory  extends BaseEntity {

    //user relationship
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //book relationship
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


    private  boolean returned;
    private  boolean returnApproved;
}
