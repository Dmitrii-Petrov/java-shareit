package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {

    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                  @RequestParam(required = false) @Min(1) Integer size) {
        log.info("поулчен запрос GET /items");
        return itemService.getItemsByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable(required = false) @NotNull Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос GET /items/id");
        return itemService.getItemDtoByItemId(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable(required = false) @NotNull Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid CommentDto commentDto) {
        log.info("поулчен запрос POST /items/id/comment");
        return itemService.addComment(itemId, userId, commentDto);
    }

    @GetMapping("/search")
    public List<Item> getItemsByTextSearch(@RequestParam String text,
                                           @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(required = false) @Min(1) Integer size) {
        log.info("поулчен запрос GET /items/search");
        return itemService.getItemsByTextSearch(text, from, size);
    }

    @PostMapping()
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос POST /items");
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item update(@PathVariable @NotNull Long itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("поулчен запрос PATCH /items");
        return itemService.update(itemId, itemDto, userId);
    }
}