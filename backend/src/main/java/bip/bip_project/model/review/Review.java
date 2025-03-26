package bip.bip_project.model.review;

import bip.bip_project.model.comment.Comment;
import bip.bip_project.model.device.Device;
import bip.bip_project.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;

    @Column(name = "date_of_changed")
    private LocalDate dateOfChanged;

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("review-device")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonBackReference("user-reviews")
    private User author;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    public Review(){}

    public Review(Integer id, String title, String content, LocalDate dateOfCreation, LocalDate dateOfChanged, Device device, User author, List<String> tags, List<Comment> comments, List<Rating> ratings) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateOfCreation = dateOfCreation;
        this.dateOfChanged = dateOfChanged;
        this.device = device;
        this.author = author;
        this.tags = tags;
        this.comments = comments;
        this.ratings = ratings;
    }

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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}

