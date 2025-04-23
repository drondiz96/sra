package bip.bip_project.model.review;

public class RatingDto {
    public Integer id;

    public Integer reviewId;

    public Integer userId;

    public Integer score;

    public RatingDto(){}

    public RatingDto(Integer id, Integer reviewId, Integer userId, Integer score) {
        this.id = id;
        this.reviewId = reviewId;
        this.userId = userId;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
