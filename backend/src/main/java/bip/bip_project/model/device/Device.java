package bip.bip_project.model.device;

import bip.bip_project.model.review.Review;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String deviceType;

    private String model;

    private String manufacturer;

    private LocalDate dateOfCreation;

    private String imageUrl;

    @OneToOne
    @JoinColumn(name="review_id", referencedColumnName = "id")
    @JsonBackReference("review-device")
    private Review review;

    public Device(){}

    public Device(Integer id, String deviceType, String model, String manufacturer, LocalDate dateOfCreation, String imageUrl, Review review) {
        this.id = id;
        this.deviceType = deviceType;
        this.model = model;
        this.manufacturer = manufacturer;
        this.dateOfCreation = dateOfCreation;
        this.imageUrl = imageUrl;
        this.review = review;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}

