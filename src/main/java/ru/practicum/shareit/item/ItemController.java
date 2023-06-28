package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
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
    public List<Item> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос GET /items");
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable(required = false) @NotNull Long itemId) {
        log.info("поулчен запрос GET /items/id");
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<Item> getItemById(@RequestParam String text) {
        log.info("поулчен запрос GET /items/search");
        return itemService.getItemsByTextSearch(text);
    }

    @PostMapping()
    public Item create(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("поулчен запрос POST /items");
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item update(@PathVariable @NotNull Long itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("поулчен запрос PATCH /items");
        return itemService.update(itemId, itemDto, userId);
    }
}