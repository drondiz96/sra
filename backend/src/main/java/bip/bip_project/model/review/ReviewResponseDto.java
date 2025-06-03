package bip.bip_project.model.review;

import bip.bip_project.model.device.Device;
import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserResponseDto;

import java.util.Date;
import java.util.List;

public class ReviewResponseDto {
    public Integer id;

    public String title;

    public String content;

    private Date dateOfCreation;

    private Date dateOfChanged;

    private Device device;

    private UserResponseDto author;

    private List<ExternalReview> externalReviews;

    public ReviewResponseDto(){}

    public ReviewResponseDto(Integer id, String title, String content, Date dateOfCreation, Date dateOfChanged, Device device, UserResponseDto author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateOfCreation = dateOfCreation;
        this.dateOfChanged = dateOfChanged;
        this.device = device;
        this.author = author;
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

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Date getDateOfChanged() {
        return dateOfChanged;
    }

    public void setDateOfChanged(Date dateOfChanged) {
        this.dateOfChanged = dateOfChanged;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public UserResponseDto getAuthor() {
        return author;
    }

    public void setAuthor(UserResponseDto author) {
        this.author = author;
    }

    public List<ExternalReview> getExternalReviews() {
        return externalReviews;
    }

    public void setExternalReviews(List<ExternalReview> externalReviews) {
        this.externalReviews = externalReviews;
    }
}