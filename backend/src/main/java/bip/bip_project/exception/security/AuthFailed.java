package bip.bip_project.exception.security;

public class AuthFailed extends RuntimeException{
    public AuthFailed(String message){
        super(message);
    }
}