package bip.bip_project.model.user;

import bip.bip_project.model.device.Device;
import bip.bip_project.model.review.Review;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(nullable = true)
    String password;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    Boolean createdViaGoogle = false;

    @Column(nullable = false)
    Boolean emailIsConfirmed = false;

    @Column(name = "password_expired")
    private boolean passwordExpired = false;

    @Column(name = "account_is_locked")
    private boolean accountLocked = false; // смотреть JwtRequestFilter.java

    @Column(name = "failed_attempts")
    private int failedAttempts = 0;


    @Column(nullable = false)
    String role;  // Заменено с List<String> на String

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-reviews")
    List<Review> reviews = new ArrayList<>();

    public User() {
    }

    public User(Integer id, String username, String password, String email, Boolean createdViaGoogle, Boolean emailIsConfirmed, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdViaGoogle = createdViaGoogle;
        this.emailIsConfirmed = emailIsConfirmed;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailIsConfirmed() {
        return emailIsConfirmed;
    }

    public void setEmailIsConfirmed(Boolean emailIsConfirmed) {
        this.emailIsConfirmed = emailIsConfirmed;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getCreatedViaGoogle() {
        return createdViaGoogle;
    }

    public void setCreatedViaGoogle(Boolean createdViaGoogle) {
        this.createdViaGoogle = createdViaGoogle;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}
