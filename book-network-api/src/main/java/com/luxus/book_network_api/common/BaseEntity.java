package com.luxus.book_network_api.common;

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

@Getter
@Setter
@SuperBuilder // Génère un constructeur avec un pattern de builder pour les classes héritées
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass // Indique que cette classe est une superclasse mappée dont les propriétés sont héritées par les entités JPA
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue
    private  Integer id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false) //Ne pas insérer de date de modification lors de la création
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false) //Ne pas insérer de modificateur lors de la création
    private Integer lastModifiedBy;
}
