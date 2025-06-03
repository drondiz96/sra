package bip.bip_project.model.user;

public class UserResponseDto {
    Integer id;
    String username;
    String email;

    public UserResponseDto() {
    }

    public UserResponseDto(Integer id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
