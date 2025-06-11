package bip.bip_project.repository.review;

import bip.bip_project.model.review.ExternalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExternalReviewRepository extends JpaRepository<ExternalReview, Integer> {

    // Получить все внешние обзоры, связанные с конкретным Review по его ID
    List<ExternalReview> findByReviewId(Integer reviewId);
}
