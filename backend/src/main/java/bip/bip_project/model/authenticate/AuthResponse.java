package bip.bip_project.model.authenticate;

public class AuthResponse {
    String response;

    public AuthResponse(){}
    public AuthResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
