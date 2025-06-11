package bip.bip_project.service.comment;

import bip.bip_project.exception.Comment.CommentNotFoundException;
import bip.bip_project.exception.review.ReviewNotFoundException;
import bip.bip_project.model.comment.*;
import bip.bip_project.model.review.Review;
import bip.bip_project.model.user.User;
import bip.bip_project.repository.comment.ICommentRepository;
import bip.bip_project.service.review.IReviewService;
import bip.bip_project.service.user.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    ICommentRepository commentRepository;
    IUserService userService;
    IReviewService reviewService;
    CommentMapper commentMapper;

    public CommentService(ICommentRepository commentRepository,
                          IReviewService reviewService,
                          IUserService userService,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.reviewService = reviewService;
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    @Override
    public List<CommentResponseDto> getCommentsByReviewId(Integer reviewId) {
        if (!reviewService.existsById(reviewId)){
            throw new ReviewNotFoundException("Not found Review with such ID");
        }
        List<CommentResponseDto> comments = new ArrayList<>();
        for (Comment comment : commentRepository.findByReviewId(reviewId)){
            comments.add(commentMapper.toDto(comment));
        }
        return comments;
    }

    @Override
    public Comment getCommentById(Integer id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new CommentNotFoundException("Not found Comment with such ID");
        }
        return comment.get();
    }

    @Override
    public CommentResponseDto getCommentResponseDtoById(Integer id) {
        return commentMapper.toDto(getCommentById(id));
    }

    @Override
    public CommentResponseDto createComment(String email, Integer reviewId, CommentRequestDto commentRequestDto) {
        User author = userService.getUserByEmail(email);
        Review review = reviewService.getReviewById(reviewId);
        Comment comment = commentMapper.toEntity(commentRequestDto);

        comment.setAuthor(author);
        comment.setReview(review);
        comment.setDateOfCreation(LocalDate.now());

        commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public CommentResponseDto updateCommentById(String email, CommentRequestDto commentRequestDto) {
        if (commentRequestDto.getId() == null) {
            throw new IllegalArgumentException("Comment ID must be provided for update");
        }
        Comment existingComment = getCommentById(commentRequestDto.getId());

        if (!existingComment.getAuthor().getEmail().equals(email)) {
            throw new SecurityException(String.format("User with email: %s do not have permission to update this comment", email));
        }
        // User author = userService.getUserByEmail(email);

        commentMapper.updateCommentFromDto(commentRequestDto, existingComment);
        existingComment.setDateOfChanged(LocalDate.now());
        commentRepository.save(existingComment);

        return commentMapper.toDto(existingComment);
    }

    @Override
    public void deleteCommentById(String email, Integer commentId) {
        if (!commentRepository.existsById(commentId)){
            throw new CommentNotFoundException("Not found Comment with such ID");
        }
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new SecurityException(String.format("User with email: %s do not have permission to delete this comment", email));
        }

        commentRepository.deleteById(commentId);
    }
}