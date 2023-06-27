package ru.practicum.shareit.item;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemNotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.item.model.ItemMapper.dtoToItem;

@Service("itemService")
@Data
public class ItemService {

    ItemStorage itemStorage;
    UserStorage userStorage;


    @Autowired
    public ItemService(@Qualifier("inMemoryItemStorage") ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;

    }

    public ItemStorage getItemStorage() {
        return itemStorage;
    }

    public List<Item> getItemsByUserId(Long userId) {
        return getItemStorage().getItemsByUserId(userId);
    }

    public Item getItemById(Long itemId) {

        return getItemStorage().getItem(itemId);
    }

    public Item create(ItemDto itemDto, Long userId) {
        if (!userStorage.getUsers().contains(userStorage.getUser(userId))) {
            throw new UserNotFoundException();
        }

        Item item = dtoToItem(null, itemDto, userId);
        getItemStorage().save(item);
        return item;
    }

    public Item update(Long id, ItemDto itemDto, Long userId) {
        if (!Objects.equals(itemStorage.getItem(id).getOwner(), userId)) {
            throw new ItemNotFoundException();
        }
        Item item = dtoToItem(id, itemDto, userId);
        getItemStorage().update(id, item, userId);
        return getItemStorage().getItem(id);
    }

    public void delete(Long id) {
        getItemStorage().delete(id);
    }

    public List<Item> getItemsByTextSearch(String text) {
        List<Item> list = new ArrayList<>();
        if (text.isBlank()) {
            return list;
        }
        for (Item item : itemStorage.getItems()) {
            if ((item.getAvailable()) && ((item.getDescription().toLowerCase().contains(text.toLowerCase())) || (item.getName().toLowerCase().contains(text.toLowerCase())))) {
                list.add(item);
            }
        }
        return list;
    }
}
