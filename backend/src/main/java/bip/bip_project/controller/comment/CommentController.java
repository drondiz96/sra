package bip.bip_project.controller.comment;

import bip.bip_project.model.comment.CommentRequestDto;
import bip.bip_project.model.comment.CommentResponseDto;
import bip.bip_project.service.comment.ICommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Комментарии", description = "Операции с комментариями к обзорам")
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Получить список комментариев к обзору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен"),
                    @ApiResponse(responseCode = "404", description = "Обзор не найден")
            })
    @GetMapping("/by-review/{reviewId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByReviewId(
            @Parameter(description = "ID обзора") @PathVariable Integer reviewId) {
        return ResponseEntity.ok(commentService.getCommentsByReviewId(reviewId));
    }

    @Operation(summary = "Получить комментарий по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий найден"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            })
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(
            @Parameter(description = "ID комментария") @PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getCommentResponseDtoById(id));
    }

    @Operation(summary = "Создать комментарий",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий создан"),
                    @ApiResponse(responseCode = "404", description = "Обзор не найден")
            })
    @PostMapping("/{reviewId}")
    public ResponseEntity<CommentResponseDto> createComment(
            Principal principal,
            @Parameter(description = "ID обзора") @PathVariable Integer reviewId,
            @RequestBody CommentRequestDto commentRequestDto) {
        String email = principal.getName();
        return ResponseEntity.ok(commentService.createComment(email, reviewId, commentRequestDto));
    }

    @Operation(summary = "Обновить комментарий",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий обновлен"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на обновление"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            })
    @PutMapping("/")
    public ResponseEntity<CommentResponseDto> updateComment(
            Principal principal,
            @RequestBody CommentRequestDto commentRequestDto) {
        String email = principal.getName();
        return ResponseEntity.ok(commentService.updateCommentById(email, commentRequestDto));
    }

    @Operation(summary = "Удалить комментарий",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Комментарий удален"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            Principal principal,
            @Parameter(description = "ID комментария") @PathVariable Integer id) {
        String email = principal.getName();
        commentService.deleteCommentById(email, id);
        return ResponseEntity.noContent().build();
    }
}
