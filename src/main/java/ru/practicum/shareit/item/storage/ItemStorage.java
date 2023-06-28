package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    void save(Item item);

    void update(Long id, Item item, Long userId);

    void delete(Long id);

    Item getItem(Long id);

    List<Item> getItemsByUserId(Long userId);

    List<Item> getItems();

}
