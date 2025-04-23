package bip.bip_project.model.comment;
import java.time.LocalDate;
import java.util.Date;

public class CommentDto {
    public Integer id;

    public String content;

    public Integer reviewId;

    public Integer authorId;

    public LocalDate dateOfCreation;

    public CommentDto() {}

    public CommentDto(Integer id, String content, Integer reviewId, Integer authorId, LocalDate dateOfCreation) {
        this.id = id;
        this.content = content;
        this.reviewId = reviewId;
        this.authorId = authorId;
        this.dateOfCreation = dateOfCreation;
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

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
