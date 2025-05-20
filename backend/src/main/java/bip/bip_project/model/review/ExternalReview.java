package bip.bip_project.model.review;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "external_reviews")
public class ExternalReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String pros;
    private String cons;
    private String author;
    private String date;
    private String source;
    private String url;

    private OffsetDateTime retrievedAt;

    @ManyToOne
    @JoinColumn(name = "review_id")
    @JsonBackReference("review-externalReviews")
    private Review review;

    public ExternalReview() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPros() {
        return pros;
    }

    public void setPros(String pros) {
        this.pros = pros;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OffsetDateTime getRetrievedAt() {
        return retrievedAt;
    }

    public void setRetrievedAt(OffsetDateTime retrievedAt) {
        this.retrievedAt = retrievedAt;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
