package bip.bip_project.model.authenticate;

public class Verify2faRequest {
    private String email;
    private String code;

    public Verify2faRequest() {
    }

    public Verify2faRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}