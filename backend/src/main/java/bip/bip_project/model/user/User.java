package bip.bip_project.model.user;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(nullable = false)
    String password;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    Boolean emailIsConfirmed = false;

    @Column(nullable = false)
    String role;  // Заменено с List<String> на String

    public User() {
    }

    public User(Integer id, String username, String password, String email, Boolean emailIsConfirmed, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
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
}
