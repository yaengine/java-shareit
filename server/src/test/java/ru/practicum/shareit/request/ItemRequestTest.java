package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestTest {

    @Test
    void testItemRequestBuilder() {
        User requestor = User.builder()
                .id(1L)
                .name("userName")
                .email("userName@ya.com")
                .build();

        LocalDateTime createdTime = LocalDateTime.now();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("test")
                .requestor(requestor)
                .created(createdTime)
                .build();

        assertThat(itemRequest.getId()).isEqualTo(1L);
        assertThat(itemRequest.getDescription()).isEqualTo("test");
        assertThat(itemRequest.getRequestor()).isEqualTo(requestor);
        assertThat(itemRequest.getCreated()).isEqualTo(createdTime);
    }

    @Test
    void testNoArgsConstructor() throws Exception {

        ItemRequest itemRequest = ItemRequest.builder().build();

        assertThat(itemRequest.getId()).isNull();
        assertThat(itemRequest.getDescription()).isNull();
        assertThat(itemRequest.getRequestor()).isNull();
        assertThat(itemRequest.getCreated()).isNull();
    }

    @Test
    void testAllArgsConstructor() {

        User requestor = User.builder()
                .id(2L)
                .name("AnotherUser")
                .email("another@example.com")
                .build();

        LocalDateTime createdTime = LocalDateTime.now();


        ItemRequest itemRequest = new ItemRequest(
                2L,
                "test",
                requestor,
                createdTime
        );

        assertThat(itemRequest.getId()).isEqualTo(2L);
        assertThat(itemRequest.getDescription()).isEqualTo("test");
        assertThat(itemRequest.getRequestor()).isEqualTo(requestor);
        assertThat(itemRequest.getCreated()).isEqualTo(createdTime);
    }

    @Test
    void testSettersAndGetters() {

        ItemRequest itemRequest = ItemRequest.builder().build();
        User requestor = User.builder()
                .id(3L)
                .name("userName 3")
                .email("user3@example.com")
                .build();

        LocalDateTime createdTime = LocalDateTime.now();

        itemRequest.setId(3L);
        itemRequest.setDescription("test");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(createdTime);

        assertThat(itemRequest.getId()).isEqualTo(3L);
        assertThat(itemRequest.getDescription()).isEqualTo("test");
        assertThat(itemRequest.getRequestor()).isEqualTo(requestor);
        assertThat(itemRequest.getCreated()).isEqualTo(createdTime);
    }

    @Test
    void testEqualsAndHashCode() {

        User requestor1 = User.builder().id(1L).build();
        User requestor2 = User.builder().id(2L).build();

        ItemRequest request1 = ItemRequest.builder()
                .id(1L)
                .description("Request 1")
                .requestor(requestor1)
                .build();

        ItemRequest request2 = ItemRequest.builder()
                .id(1L)
                .description("Request 1")
                .requestor(requestor1)
                .build();

        ItemRequest request3 = ItemRequest.builder()
                .id(2L)
                .description("Request 2")
                .requestor(requestor2)
                .build();

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1).isNotEqualTo(request3);
    }

    @Test
    void testToString() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(4L)
                .description("test")
                .build();

        String toString = itemRequest.toString();
        assertThat(toString).contains("ItemRequest");
        assertThat(toString).contains("id=4");
        assertThat(toString).contains("description=test");
    }
}
