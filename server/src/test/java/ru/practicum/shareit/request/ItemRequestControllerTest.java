package ru.practicum.shareit.request;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@WebMvcTest(controllers = ItemRequestController.class)
@ContextConfiguration(classes = ShareItServer.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private final ItemRequestDto testRequest = ItemRequestDto.builder()
            .id(1L)
            .description("Need a drill")
            .created(LocalDateTime.now())
            .build();

    @Test
    void createRequest_shouldReturnCreatedRequest() throws Exception {
        when(itemRequestService.createBooking(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(testRequest);

        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRequest.getId()))
                .andExpect(jsonPath("$.description").value(testRequest.getDescription()));
    }

    @Test
    void getUserRequests_shouldReturnUserRequests() throws Exception {
        when(itemRequestService.findUserRequests(anyLong()))
                .thenReturn(List.of(testRequest));

        mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(testRequest.getId()));
    }

    @Test
    void getAllRequests_shouldReturnAllRequests() throws Exception {
        when(itemRequestService.findAllRequests())
                .thenReturn(List.of(testRequest));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(testRequest.getId()));
    }

    @Test
    void getRequestById_shouldReturnRequest() throws Exception {
        when(itemRequestService.findRequestById(anyLong()))
                .thenReturn(testRequest);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRequest.getId()))
                .andExpect(jsonPath("$.description").value(testRequest.getDescription()));
    }

    @Test
    void createRequest_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest());
    }
}
