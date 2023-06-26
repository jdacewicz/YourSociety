package pl.jdacewicz.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.postservice.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
