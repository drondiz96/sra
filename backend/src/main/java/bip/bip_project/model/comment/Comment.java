package bip.bip_project.model.comment;

import bip.bip_project.model.review.Review;
import bip.bip_project.model.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private LocalDate dateOfCreation;
    private LocalDate dateOfChanged;

    public Comment() {
    }

    public Comment(Integer id, String content, Review review, User author, LocalDate dateOfCreation, LocalDate dateOfChanged) {
        this.id = id;
        this.content = content;
        this.review = review;
        this.author = author;
        this.dateOfCreation = dateOfCreation;
        this.dateOfChanged = dateOfChanged;
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

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public LocalDate getDateOfChanged() {
        return dateOfChanged;
    }

    public void setDateOfChanged(LocalDate dateOfChanged) {
        this.dateOfChanged = dateOfChanged;
    }
}

