package bip.bip_project.controller.review;

import bip.bip_project.model.device.FilterType;
import bip.bip_project.model.review.Review;
import bip.bip_project.model.review.ReviewRequestDto;
import bip.bip_project.model.review.ReviewResponseDto;
import bip.bip_project.service.review.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
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

    @Operation(summary = "Получить обзоры по дате", description = "Возвращает список обзоров, созданных в заданном диапазоне дат.")
    @PostMapping("/by-date")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByDate(
            @RequestBody Map<String, LocalDate> dates) {
        return ResponseEntity.ok(reviewService.getReviews(dates));
    }

    @Operation(summary = "Получить все обзоры", description = "Возвращает список всех обзоров")
    @GetMapping("/all")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @Operation(summary = "Получить обзор по ID", description = "Возвращает обзор по его уникальному идентификатору.")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(
            @Parameter(description = "ID обзора") @PathVariable Integer id) {
        return ResponseEntity.ok(reviewService.getReviewResponseDtoById(id));
    }

    @Operation(summary = "Получить обзоры по фильтру", description = "Возвращает список обзоров по типу фильтра (DEVICE_TYPE, DEVICE_MODEL, MANUFACTURER) и его значению.")
    @GetMapping("/filter")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByFilter(
            @Parameter(description = "Тип фильтра") @RequestParam FilterType filterType,
            @Parameter(description = "Значение фильтра") @RequestParam String value) {
        return ResponseEntity.ok(reviewService.getReviewsByFilter(filterType, value));
    }

    @Operation(summary = "Создать новый обзор", description = "Создает новый обзор от имени авторизованного пользователя.")
    @PostMapping("/")
    public ResponseEntity<ReviewResponseDto> createReview(
            Principal principal,
            @RequestBody ReviewRequestDto reviewRequestDto) {
        String email = principal.getName();
        return ResponseEntity.ok(reviewService.createReview(email, reviewRequestDto));
    }

    @Operation(summary = "Обновить обзор", description = "Обновляет существующий обзор, если он принадлежит текущему пользователю.")
    @PutMapping("/")
    public ResponseEntity<ReviewResponseDto> updateReview(
            Principal principal,
            @RequestBody ReviewRequestDto reviewRequestDto) {
        String email = principal.getName();
        return ResponseEntity.ok(reviewService.updateReview(email, reviewRequestDto));
    }

    @Operation(summary = "Удалить обзор", description = "Удаляет обзор по ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            Principal principal,
            @Parameter(description = "ID обзора") @PathVariable Integer id) {
        String email = principal.getName();
        reviewService.deleteReviewById(email, id);
        return ResponseEntity.noContent().build();
    }
}
