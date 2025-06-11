package bip.bip_project.service.review;

import bip.bip_project.model.review.ExternalReview;
import bip.bip_project.model.review.Review;

import java.util.List;

public interface IExternalReviewService {
    List<ExternalReview> fetchAndSaveExternalReviews(Review review);
}
