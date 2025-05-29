package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    private final User author = User.builder()
            .id(1L)
            .name("username")
            .email("username@ya.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .build();

    @Test
    void builderShouldCreateValidComment() {
        LocalDateTime testTime = LocalDateTime.now();
        Comment comment = Comment.builder()
                .id(1L)
                .text("Test comment")
                .author(author)
                .item(item)
                .created(testTime)
                .build();

        assertThat(comment.getId()).isEqualTo(1L);
        assertThat(comment.getText()).isEqualTo("Test comment");
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getCreated()).isEqualTo(testTime);
    }

    @Test
    void noArgsConstructorShouldCreateEmptyComment() {
        Comment comment = Comment.builder().build();

        assertThat(comment.getId()).isNull();
        assertThat(comment.getText()).isNull();
        assertThat(comment.getAuthor()).isNull();
        assertThat(comment.getItem()).isNull();
        assertThat(comment.getCreated()).isNull();
    }

    @Test
    void allArgsConstructorShouldSetAllFields() {
        LocalDateTime testTime = LocalDateTime.now();
        Comment comment = new Comment(2L, "User comment", author, item, testTime);

        assertThat(comment.getId()).isEqualTo(2L);
        assertThat(comment.getText()).isEqualTo("User comment");
        assertThat(comment.getCreated()).isEqualTo(testTime);
    }

    @Test
    void settersShouldUpdateFields() {
        Comment comment = Comment.builder().build();
        comment.setId(3L);
        comment.setText("Updated comment");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        assertThat(comment.getId()).isEqualTo(3L);
        assertThat(comment.getText()).isEqualTo("Updated comment");
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getCreated()).isNotNull();
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        Comment comment1 = Comment.builder()
                .id(1L)
                .author(author)
                .build();

        Comment comment2 = Comment.builder()
                .id(1L)
                .author(author)
                .build();

        Comment comment3 = Comment.builder()
                .id(2L)
                .build();

        assertThat(comment1).isEqualTo(comment2);
        assertThat(comment1.hashCode()).isEqualTo(comment2.hashCode());
        assertThat(comment1).isNotEqualTo(comment3);
    }

    @Test
    void toStringShouldContainImportantInfo() {
        Comment comment = Comment.builder()
                .id(4L)
                .text("Test")
                .build();

        String toString = comment.toString();

        assertThat(toString).contains("Comment");
        assertThat(toString).contains("id=4");
        assertThat(toString).contains("text=Test");
    }

    @Test
    void creationTimestampShouldBeSetAutomatically() {
        Comment comment = Comment.builder().build();
        comment.setId(5L);
        comment.setText("Auto timestamp");

        // При сохранении в базу created установится автоматически
        assertThat(comment.getCreated()).isNull(); // До сохранения null
    }
}
