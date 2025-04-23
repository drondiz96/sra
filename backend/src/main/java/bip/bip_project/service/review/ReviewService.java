package bip.bip_project.service.review;

import bip.bip_project.model.review.ReviewDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService implements IReviewService {
    @Override
    public List<ReviewDto> getReviews(Map<String, LocalDate> dates) {
        return List.of();
    }

    @Override
    public List<ReviewDto> getReviewsByTags(List<String> tags) {
        return List.of();
    }

    @Override
    public ReviewDto getReviewById(Integer reviewId) {
        return null;
    }

    @Override
    public List<ReviewDto> getReviewsByDeviceType(String deviceType) {
        return List.of();
    }

    @Override
    public List<ReviewDto> getReviewsByDeviceModel(String deviceModel) {
        return List.of();
    }

    @Override
    public List<ReviewDto> getReviewsByManufacturer(String manufacturer) {
        return List.of();
    }

    @Override
    public List<ReviewDto> getReviewsByAuthorEmail(String email) {
        return List.of();
    }

    @Override
    public List<ReviewDto> getReviewByAuthorId(Integer authorId) {
        return List.of();
    }

    @Override
    public ReviewDto updateReview(ReviewDto reviewDto) {
        return null;
    }

    @Override
    public void deleteReviewById(Integer reviewId) {

    }
}
