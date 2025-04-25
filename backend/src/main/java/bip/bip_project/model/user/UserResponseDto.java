package bip.bip_project.model.user;

public class UserResponseDto {
    Integer id;
    String username;

    public UserResponseDto() {
    }

    public UserResponseDto(Integer id, String username) {
        this.id = id;
        this.username = username;
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
