package bip.bip_project.model.comment;

public class CommentRequestDto {
    private Integer id;
    private String content;

    public CommentRequestDto() {
    }

    public CommentRequestDto(Integer id, String content) {
        this.id = id;
        this.content = content;
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
}
