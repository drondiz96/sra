package bip.bip_project.model.comment;

public class CommentRequestDto {
    private Integer id;
    private String content;
    private Integer reviewId;

    public CommentRequestDto() {
    }

    public CommentRequestDto(Integer id, String content, Integer reviewId) {
        this.id = id;
        this.content = content;
        this.reviewId = reviewId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }
}
