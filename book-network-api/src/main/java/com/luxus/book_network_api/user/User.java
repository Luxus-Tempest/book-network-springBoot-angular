package com.luxus.book_network_api.user;

import com.luxus.book_network_api.book.Book;
import com.luxus.book_network_api.history.BookTransactionHistory;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.luxus.book_network_api.role.Role;

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
/*
 * Considerer la classe User comme une entité ou table d'un BD
 */
@Entity
@Table(name= "_user")
/*
 * Pour permettre le suivis des date de création et de modification de l'utilisateur
 * on utilise : @EntityListeners(AuditingEntityListener.class) 
 */
@EntityListeners(AuditingEntityListener.class)

public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private Integer id;

    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    /*
     * La coulumn(unique = true) permet d'avoir une contrainte d'unicité. 
     * Chaque utilisateur doit avoir un email unique
     */
    @Column(unique = true)
    private String email;
    private String password;
    private boolean accountLocked;
    private boolean enabled;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false, updatable = true) //Ne pas déclarer ce champ à la création
    private LocalDateTime lastModifiedDate;

    /**
     * Récupérer la liste des roles quand les données de l'utilisateur sont demandées
        * Utilisation de FetchType.EAGER pour charger immédiatement
        * les rôles associés à l'utilisateur lors de la récupération de l'entité User.
        * Cela évite de faire des requêtes supplémentaires au moment où les rôles sont accédés.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToMany(mappedBy = "owner")
    /* One user to many books (Book)
     * mappedBy = "owner" : indique que la relation est gérée par la propriété "owner" de la class Book
     * */
    private List<Book> books;

    //Book transaction history relationship
    @OneToMany(mappedBy = "user")
    private List<BookTransactionHistory> histories;

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Récupère les rôles associés à l'utilisateur et les convertit en instances de SimpleGrantedAuthority
        return this.roles
                .stream() // Crée un flux à partir de la liste des rôles
                .map(r -> new SimpleGrantedAuthority(r.getName())) // Transforme chaque rôle en une autorité simple
                .toList(); // Collecte les résultats dans une liste
    }
    

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return enabled;
    }

   public String getFullName() {
       return this.firstname + " " + this.lastname;
   }


}
