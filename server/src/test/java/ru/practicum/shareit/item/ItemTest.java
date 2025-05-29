package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    private final User owner = User.builder()
            .id(1L)
            .name("Owner")
            .email("owner@example.com")
            .build();

    private final ItemRequest request = ItemRequest.builder()
            .id(1L)
            .description("Request description")
            .build();

    private final Booking booking1 = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().plusDays(1))
            .build();

    private final Booking booking2 = Booking.builder()
            .id(2L)
            .start(LocalDateTime.now().plusDays(2))
            .build();

    @Test
    void builderShouldCreateValidItem() {
        Item item = Item.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(owner)
                .request(request)
                .bookings(List.of(booking1, booking2))
                .build();

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("Item name");
        assertThat(item.getDescription()).isEqualTo("Item description");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getOwner()).isEqualTo(owner);
        assertThat(item.getRequest()).isEqualTo(request);
        assertThat(item.getBookings()).hasSize(2);
    }

    @Test
    void noArgsConstructorShouldCreateEmptyItem() {
        Item item = Item.builder().build();

        assertThat(item.getId()).isNull();
        assertThat(item.getName()).isNull();
        assertThat(item.getAvailable()).isNull();
        assertThat(item.getOwner()).isNull();
        assertThat(item.getBookings()).isNull();
    }

    @Test
    void allArgsConstructorShouldSetAllFields() {
        Item item = new Item(
                2L,
                "Test item",
                "Test description",
                false,
                owner,
                request,
                List.of(booking1)
        );

        assertThat(item.getId()).isEqualTo(2L);
        assertThat(item.getAvailable()).isFalse();
        assertThat(item.getBookings()).hasSize(1);
    }

    @Test
    void settersShouldUpdateFields() {
        Item item = Item.builder().build();
        item.setId(3L);
        item.setName("New name");
        item.setDescription("New description");
        item.setAvailable(false);
        item.setOwner(owner);
        item.setRequest(request);
        item.setBookings(List.of(booking2));

        assertThat(item.getId()).isEqualTo(3L);
        assertThat(item.getName()).isEqualTo("New name");
        assertThat(item.getAvailable()).isFalse();
        assertThat(item.getOwner()).isEqualTo(owner);
        assertThat(item.getBookings()).hasSize(1);
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        Item item1 = Item.builder()
                .id(1L)
                .owner(owner)
                .build();

        Item item2 = Item.builder()
                .id(1L)
                .owner(owner)
                .build();

        Item item3 = Item.builder()
                .id(2L)
                .build();

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        assertThat(item1).isNotEqualTo(item3);
    }

    @Test
    void toStringShouldContainImportantInfo() {
        Item item = Item.builder()
                .id(4L)
                .name("Test")
                .available(true)
                .build();

        String toString = item.toString();

        assertThat(toString).contains("Item");
        assertThat(toString).contains("id=4");
        assertThat(toString).contains("name=Test");
        assertThat(toString).contains("available=true");
    }
}
