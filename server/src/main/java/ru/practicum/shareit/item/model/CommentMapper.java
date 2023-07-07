package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CommentMapper {

    public static CommentDto commentToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                comment.getCreated(),
                comment.getAuthor().getName()
        );
    }

    public static Comment mapToNewComment(CommentDto commentDto, User user, Item itemId) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        if (commentDto.getCreated() != null) {
            comment.setCreated(commentDto.getCreated());
        } else {
            comment.setCreated(LocalDateTime.now());
        }
        comment.setItem(itemId);
        comment.setAuthor(user);
        return comment;
    }
}
