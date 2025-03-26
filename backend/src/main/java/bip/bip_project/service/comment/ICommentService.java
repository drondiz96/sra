package bip.bip_project.service.comment;

import bip.bip_project.model.comment.Comment;
import bip.bip_project.model.comment.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ICommentService {
    List<CommentDto> getCommentsByReviewId(Integer reviewId);

    CommentDto getCommentById(Integer id);

    CommentDto createComment(CommentDto commentDto);

    CommentDto updateCommentById(CommentDto commentDto);

    CommentDto deleteCommentById(Integer id);
}
