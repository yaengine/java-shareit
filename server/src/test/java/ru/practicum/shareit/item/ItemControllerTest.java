package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@WebMvcTest(controllers = ItemController.class)
@ContextConfiguration(classes = ShareItServer.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .build();

    private final CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .text("Comment")
            .build();

    @Test
    void createItem_shouldReturnCreatedItem() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(UserDto.builder().build());
        when(itemService.createItem(any(), any())).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(UserDto.builder().build());
        when(itemService.updateItemById(any(), anyLong(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    void getItemById_shouldReturnItem() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(UserDto.builder().build());
        when(itemService.findItemById(anyLong())).thenReturn(itemDto);

        mockMvc.perform(get("/items/1")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    void getAllByUserId_shouldReturnItems() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(UserDto.builder().build());
        when(itemService.findAllByUserId(anyLong())).thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()));
    }

    @Test
    void searchItems_shouldReturnItems() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(UserDto.builder().build());
        when(itemService.searchItemsByText(anyString())).thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items/search")
                        .header(X_SHARER_USER_ID, 1L)
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()));
    }

    @Test
    void createComment_shouldReturnComment() throws Exception {
        when(itemService.createComment(anyLong(), any(), anyLong())).thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()));
    }

    @Test
    void createItem_withoutUserId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }
}
