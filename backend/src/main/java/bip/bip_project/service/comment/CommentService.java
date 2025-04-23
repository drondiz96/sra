package bip.bip_project.service.comment;

import bip.bip_project.model.comment.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements ICommentService{
    @Override
    public List<CommentDto> getCommentsByReviewId(Integer reviewId) {
        return List.of();
    }

    @Override
    public CommentDto getCommentById(Integer id) {
        return null;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        return null;
    }

    @Override
    public CommentDto updateCommentById(CommentDto commentDto) {
        return null;
    }

    @Override
    public CommentDto deleteCommentById(Integer id) {
        return null;
    }
}
