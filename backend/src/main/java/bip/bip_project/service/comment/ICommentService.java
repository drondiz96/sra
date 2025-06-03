package bip.bip_project.service.comment;

import bip.bip_project.model.comment.Comment;
import bip.bip_project.model.comment.CommentDto;
import bip.bip_project.model.comment.CommentRequestDto;
import bip.bip_project.model.comment.CommentResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ICommentService {
    List<CommentResponseDto> getCommentsByReviewId(Integer reviewId);

    Comment getCommentById(Integer id);
    CommentResponseDto getCommentResponseDtoById(Integer id);

    CommentResponseDto createComment(String email, Integer reviewId, CommentRequestDto commentRequestDto);

    CommentResponseDto updateCommentById(String email, CommentRequestDto commentRequestDto);

    void deleteCommentById(String email, Integer commentId);
}
