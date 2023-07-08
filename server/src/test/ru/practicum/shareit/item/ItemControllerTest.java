package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemService itemService;


    @Test
    void getItems() {
        long userId = 0L;
        int from = 0;
        int size = 10;
        ItemDto itemDto = new ItemDto();

        when(itemService.getItemsByUserId(userId, from, size)).thenReturn(List.of(itemDto));

        List<ItemDto> result = itemController.getItems(userId, from, size);

        assertEquals(itemDto, result.get(0));
        verify(itemService).getItemsByUserId(userId, from, size);
    }

    @Test
    void getItemById() {
        long userId = 0L;
        long itemId = 0L;

        ItemDto itemDto = new ItemDto();

        when(itemService.getItemDtoByItemId(itemId, userId)).thenReturn(itemDto);

        ItemDto result = itemController.getItemById(itemId, userId);

        assertEquals(itemDto, result);
        verify(itemService).getItemDtoByItemId(itemId, userId);
    }

    @Test
    void addComment() {
        long userId = 0L;
        long itemId = 0L;
        CommentDto commentDto = new CommentDto();

        when(itemService.addComment(itemId, userId, commentDto)).thenReturn(commentDto);

        CommentDto result = itemController.addComment(itemId, userId, commentDto);

        assertEquals(commentDto, result);

        verify(itemService).addComment(itemId, userId, commentDto);
    }

    @Test
    void getItemsByTextSearch() {
        String text = "text";
        int from = 0;
        int size = 10;

        Item item = new Item();

        when(itemService.getItemsByTextSearch(text, from, size)).thenReturn(List.of(item));

        List<Item> result = itemController.getItemsByTextSearch(text, from, size);

        assertEquals(item, result.get(0));

        verify(itemService).getItemsByTextSearch(text, from, size);
    }

    @Test
    void create() {
        long userId = 0L;
        ItemDto itemDto = new ItemDto();

        when(itemService.create(itemDto, userId)).thenReturn(itemDto);

        ItemDto result = itemController.create(itemDto, userId);

        assertEquals(itemDto, result);

        verify(itemService).create(itemDto, userId);

    }

    @Test
    void update() {
        long userId = 0L;
        long itemId = 0L;

        ItemDto itemDto = new ItemDto();
        Item item = new Item();

        when(itemService.update(itemId, itemDto, userId)).thenReturn(item);

        Item result = itemController.update(itemId, itemDto, userId);

        assertEquals(item, result);

        verify(itemService).update(itemId, itemDto, userId);

    }
}