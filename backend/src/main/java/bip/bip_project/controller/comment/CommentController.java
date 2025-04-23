package bip.bip_project.controller.comment;

import bip.bip_project.model.comment.CommentDto;
import bip.bip_project.service.comment.ICommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Комментарии", description = "Операции с комментариями к обзорам")
@RestController
@RequestMapping("/comments")
public class CommentController {
    ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Получить комментарий по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий найден"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            })
    @GetMapping("/{commentId}")
    ResponseEntity<CommentDto> getCommentDtoById(@PathVariable("commentId") Integer commentId){
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @Operation(summary = "Получить список комментариев по reviewId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарии найдены")
            })
    @GetMapping("/byReviews/{reviewId}")
    ResponseEntity<List<CommentDto>> getCommentsByReviewId(@PathVariable("reviewId") Integer reviewId){
        return ResponseEntity.ok(commentService.getCommentsByReviewId(reviewId));
    }

    @Operation(summary = "Создать комментарий",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий создан"),
                    @ApiResponse(responseCode = "400", description = "Ошибка при создании комментария")
            })
    @PostMapping("/")
    ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.createComment(commentDto));
    }

    @Operation(summary = "Обновить комментарий",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий обновлён"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            })
    @PutMapping("/")
    ResponseEntity<CommentDto> updateCommentById(@RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.updateCommentById(commentDto));
    }

    @Operation(summary = "Удалить комментарий по ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Комментарий удалён"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            })
    @DeleteMapping("/{commentId}")
    void deleteCommentById (@PathVariable("commentId") Integer commentId){
        commentService.deleteCommentById(commentId);
    }
}