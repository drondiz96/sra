package bip.bip_project.model.comment;

import bip.bip_project.model.user.UserResponseDto;

import java.util.Date;

public class CommentResponseDto {
    private Integer id;
    private String content;
    private Date dateOfCreation;
    private Date dateOfChanged;
    private UserResponseDto author;

    public CommentResponseDto() {
    }

    public CommentResponseDto(Integer id, String content, Date dateOfCreation, Date dateOfChanged, UserResponseDto author) {
        this.id = id;
        this.content = content;
        this.dateOfCreation = dateOfCreation;
        this.dateOfChanged = dateOfChanged;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UserResponseDto getAuthor() {
        return author;
    }

    public void setAuthor(UserResponseDto author) {
        this.author = author;
    }
}
