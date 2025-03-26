package bip.bip_project.service.review;

import bip.bip_project.model.review.ReviewDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface IReviewService {
    List<ReviewDto> getReviews(Map<String, LocalDate> dates);
    List<ReviewDto> getReviewsByTags(List<String> tags);

    ReviewDto getReviewById(Integer reviewId);

    List<ReviewDto> getReviewsByDeviceType(String deviceType);

    List<ReviewDto> getReviewsByDeviceModel(String deviceModel);

    List<ReviewDto> getReviewsByManufacturer(String manufacturer);

    List<ReviewDto> getReviewsByAuthorEmail(String email);
    List<ReviewDto> getReviewByAuthorId(Integer authorId);

    ReviewDto updateReview(ReviewDto reviewDto);

    void deleteReviewById(Integer reviewId);

}
