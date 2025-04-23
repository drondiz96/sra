package bip.bip_project.controller.review;

import bip.bip_project.model.review.ReviewDto;
import bip.bip_project.service.review.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "Обзоры", description = "Операции с обзорами устройств")
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Получить все обзоры по диапазону дат",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Обзоры найдены")
            })
    @PostMapping("/getNews/")
    ResponseEntity<List<ReviewDto>> GetAllReviews(@RequestBody Map<String, LocalDate> dates) {
        return ResponseEntity.ok(reviewService.getReviews(dates));
    }

    @Operation(summary = "Получить обзоры по тегам",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Обзоры найдены")
            })
    @PostMapping("/getReviewsByTags")
    ResponseEntity<List<ReviewDto>> getNewsByTags(@RequestBody ArrayList<String> tags) {
        return ResponseEntity.ok(reviewService.getReviewsByTags(tags));
    }

    @Operation(summary = "Получить обзор по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Обзор найден"),
                    @ApiResponse(responseCode = "404", description = "Обзор не найден")
            })
    @GetMapping("/{reviewId}")
    ResponseEntity<ReviewDto> getNewsById(@PathVariable("reviewId") Integer reviewId){
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }

    @Operation(summary = "Получить обзоры по автору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Обзоры найдены")
            })
    @GetMapping("/byAuthor/{authorId}")
    ResponseEntity<List<ReviewDto>> getReviewsByAuthorId(@PathVariable("authorId") Integer authorId){
        return ResponseEntity.ok(reviewService.getReviewByAuthorId(authorId));
    }

    @Operation(summary = "Обновить обзор",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Обзор обновлён")
            })
    @PutMapping("/")
    ResponseEntity<ReviewDto> updateReview(@RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok(reviewService.updateReview(reviewDto));
    }

    @Operation(summary = "Удалить обзор по ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Обзор удалён"),
                    @ApiResponse(responseCode = "404", description = "Обзор не найден")
            })
    @DeleteMapping("/{reviewId}")
    ResponseEntity<Object> deleteReviewById(@PathVariable("reviewId") Integer reviewId){
        reviewService.deleteReviewById(reviewId);
        return ResponseEntity.noContent().build();
    }
}