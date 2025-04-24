package bip.bip_project.service.review;

import bip.bip_project.model.device.FilterType;
import bip.bip_project.model.review.Review;
import bip.bip_project.model.review.ReviewRequestDto;
import bip.bip_project.model.review.ReviewResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface IReviewService {
    List<Review> getReviews(Map<String, LocalDate> dates);

    boolean existsById(Integer reviewId);
    Review getReviewById(Integer reviewId);
    ReviewResponseDto getReviewResponseDtoById(Integer reviewId);

    List<ReviewResponseDto> getReviewsByFilter(FilterType filterType, String value);

    ReviewResponseDto createReview(String email, ReviewRequestDto reviewRequestDto);
    ReviewResponseDto updateReview(String email, ReviewRequestDto reviewRequestDto);

    void deleteReviewById(String email, Integer reviewId);

}
