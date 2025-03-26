package bip.bip_project.repository.review;

import bip.bip_project.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReviewRepository extends JpaRepository<Review, Integer> {
}
