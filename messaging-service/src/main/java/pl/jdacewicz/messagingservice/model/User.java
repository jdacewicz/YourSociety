package pl.jdacewicz.messagingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_users")
public class User {

    public final static String USERS_DIRECTORY_PATH = "uploads/messaging/profiles";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String profilePicture;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "friends", joinColumns = {
            @JoinColumn(name = "idA")}, inverseJoinColumns = {
            @JoinColumn(name = "idB")
    })
    private Set<User> friends;

    public String getDirectoryPath() {
        return USERS_DIRECTORY_PATH + "/" + id;
    }
}
