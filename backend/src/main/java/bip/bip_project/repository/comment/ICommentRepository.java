package bip.bip_project.repository.comment;

import bip.bip_project.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommentRepository extends JpaRepository<Comment, Integer> {
}
