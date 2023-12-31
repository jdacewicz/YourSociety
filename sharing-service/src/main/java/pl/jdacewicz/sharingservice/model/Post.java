package pl.jdacewicz.sharingservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.jdacewicz.sharingservice.util.FileStorageUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "t_posts")
public class Post {

    public static final String POSTS_DIRECTORY_PATH = "uploads/posts";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Builder.Default
    private LocalDateTime creationTime = LocalDateTime.now();

    private String content;

    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "posts_reactions",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "reaction_id"))
    @Builder.Default
    private List<Reaction> reactions = new LinkedList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new LinkedList<>();

    @ManyToMany(mappedBy = "posts")
    private Set<PostGroup> postGroupList = new HashSet<>();

    @Builder.Default
    private boolean visible = true;

    public String getImagePath() {
        return FileStorageUtils.getImagePath(this.id, this.image, POSTS_DIRECTORY_PATH);
    }

    public String getDirectoryPath() {
        return FileStorageUtils.getDirectoryPath(this.id, POSTS_DIRECTORY_PATH);
    }

    public void addReaction(Reaction reaction) {
        reactions.add(reaction);
        reaction.getPostList().add(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }
}
