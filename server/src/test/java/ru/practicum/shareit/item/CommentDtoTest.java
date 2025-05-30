package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class CommentDtoTest {

    private final LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 12, 0);

    @Test
    void builderShouldCreateValidCommentDto() {
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("user")
                .created(testTime)
                .build();

        assertThat(comment.getId()).isEqualTo(1L);
        assertThat(comment.getText()).isEqualTo("text");
        assertThat(comment.getAuthorName()).isEqualTo("user");
        assertThat(comment.getCreated()).isEqualTo(testTime);
    }

    @Test
    void settersAndGettersShouldWorkCorrectly() {
        CommentDto comment = CommentDto.builder().build();
        comment.setId(2L);
        comment.setText("Updated comment");
        comment.setAuthorName("user");
        comment.setCreated(testTime.plusDays(1));

        assertThat(comment.getId()).isEqualTo(2L);
        assertThat(comment.getText()).isEqualTo("Updated comment");
        assertThat(comment.getAuthorName()).isEqualTo("user");
        assertThat(comment.getCreated()).isAfter(testTime);
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        CommentDto comment1 = CommentDto.builder()
                .id(1L)
                .authorName("Author")
                .build();

        CommentDto comment2 = CommentDto.builder()
                .id(1L)
                .authorName("Author")
                .build();

        CommentDto comment3 = CommentDto.builder()
                .id(2L)
                .build();

        assertThat(comment1).isEqualTo(comment2);
        assertThat(comment1.hashCode()).isEqualTo(comment2.hashCode());
        assertThat(comment1).isNotEqualTo(comment3);
    }

    @Test
    void toStringShouldContainImportantInfo() {
        CommentDto comment = CommentDto.builder()
                .id(3L)
                .text("Test comment")
                .build();

        String toString = comment.toString();

        assertThat(toString).contains("CommentDto");
        assertThat(toString).contains("id=3");
        assertThat(toString).contains("text=Test comment");
    }

    @Test
    void shouldHandleNullFields() {
        CommentDto comment = CommentDto.builder()
                .id(4L)
                .text(null)
                .authorName(null)
                .created(null)
                .build();

        assertThat(comment.getText()).isNull();
        assertThat(comment.getAuthorName()).isNull();
        assertThat(comment.getCreated()).isNull();
    }

    @Test
    void shouldHandleEmptyAuthorName() {
        CommentDto comment = CommentDto.builder()
                .id(5L)
                .authorName("")
                .build();

        assertThat(comment.getAuthorName()).isEmpty();
    }
}
