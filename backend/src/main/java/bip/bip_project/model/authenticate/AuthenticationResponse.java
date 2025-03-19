package bip.bip_project.model.authenticate;

public class AuthenticationResponse {
    String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}