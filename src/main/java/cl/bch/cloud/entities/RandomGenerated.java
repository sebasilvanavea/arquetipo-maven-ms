package cl.bch.cloud.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * A Pojo class that represents a DataBase table
 */
@Entity //This tells Hibernate to create a table of this object
@Table(name = "random_generated") //This annotation stablishes the database table's name
// @Data  Avoid using @EqualsAndHashCode and @Data with JPA entities
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RandomGenerated {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private int min;

    @NonNull
    private int max;

    @NonNull
    private int random;

    @NonNull
    private String message;

    @NonNull
    private String status;

}