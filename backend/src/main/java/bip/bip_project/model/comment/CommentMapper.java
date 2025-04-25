package bip.bip_project.model.comment;

import bip.bip_project.model.review.Review;
import bip.bip_project.model.user.UserMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    Comment toEntity(CommentRequestDto dto);

    @Mapping(source = "author", target = "author")
    CommentResponseDto toDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateCommentFromDto(CommentRequestDto dto, @MappingTarget Comment comment);
}
