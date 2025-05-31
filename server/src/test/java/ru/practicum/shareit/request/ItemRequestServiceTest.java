package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Mock
    ItemMapper itemMapper;

    @Mock
    ItemRequestMapper itemRequestMapper;

    User user;
    ItemRequest request;
    Item item;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("userName1r")
                .email("userName1r@ya.ru")
                .build();
        request = ItemRequest.builder()
                .id(1L)
                .description("requestDescriprion")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();
        item = Item.builder()
                .id(1L)
                .owner(user)
                .name("itemName")
                .description("itemDescription")
                .request(request)
                .available(true)
                .build();
    }

    @Test
    void createBooking() {
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(request);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class))).thenReturn(ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build());

        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(request);

        ItemRequestDto requestDto = itemRequestService.createBooking(user.getId(), itemRequestDto);

        assertEquals(requestDto.getId(), request.getId(), "Запрос не создался");
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void findUserRequests() {
        when(itemRequestRepository.findByRequestorId(user.getId())).thenReturn(List.of(request));
        when(itemRepository.findByRequestId(request.getId())).thenReturn(List.of(item));

        List<ItemRequestDto> itemRequestDtoList = itemRequestService.findUserRequests(user.getId());

        assertFalse(itemRequestDtoList.isEmpty(), "Запросы не нашлись");
    }

    @Test
    void findAllRequests() {
        when(itemRepository.findByRequestId(request.getId())).thenReturn(List.of(item));
        when(itemRequestRepository.findAll()).thenReturn(List.of(request));
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                null,
                null,
                null));
        when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class), any())).thenReturn(ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .items(List.of(new ItemDto(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        item.getRequest() != null ? item.getRequest().getId() : null,
                        null,
                        null,
                        null)))
                .created(request.getCreated())
                .build());

        List<ItemRequestDto> requests = itemRequestService.findAllRequests();

        assertFalse(requests.isEmpty(), "Запросы не нашлись");
    }

    @Test
    void findRequestById() {
        when(itemRepository.findByRequestId(request.getId())).thenReturn(List.of(item));
        when(itemRequestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                null,
                null,
                null));
        when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class), any())).thenReturn(ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .items(List.of(new ItemDto(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        item.getRequest() != null ? item.getRequest().getId() : null,
                        null,
                        null,
                        null)))
                .created(request.getCreated())
                .build());

        ItemRequestDto findRequest = itemRequestService.findRequestById(request.getId());

        assertEquals(findRequest.getId(), request.getId(), "Запрос не нашелся");
    }
}
