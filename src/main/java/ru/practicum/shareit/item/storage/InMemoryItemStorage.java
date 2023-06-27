package ru.practicum.shareit.item.storage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Data
@Component("inMemoryItemStorage")
public class InMemoryItemStorage implements ItemStorage {
    HashMap<Long, Item> itemRepository = new HashMap<>();
    long generatorId = 0;

    public long generateId() {
        return ++generatorId;
    }

    @Override
    public void save(Item item) {
        item.setId(generateId());
        itemRepository.put(item.getId(), item);
    }

    @Override
    public void update(Long id, Item item, Long userId) {

        if (!itemRepository.containsKey(id)) {
            throw new NotFoundEntityException();
        }

        if (item.getName() != null) {
            itemRepository.get(id).setName(item.getName());
        }
        if (item.getAvailable() != null) {
            itemRepository.get(id).setAvailable(item.getAvailable());
        }
        if (item.getDescription() != null) {
            itemRepository.get(id).setDescription(item.getDescription());
        }
    }

    @Override
    public void delete(Long id) {
        if (!itemRepository.containsKey(id)) {
            throw new NotFoundEntityException();
        }
        itemRepository.remove(id);
    }


    @Override
    public Item getItem(Long id) {
        if (!itemRepository.containsKey(id)) {
            throw new NotFoundEntityException();
        }
        return itemRepository.get(id);
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        List<Item> list = new ArrayList<>();
        for (Item item : itemRepository.values()) {
            if (item.getOwner().equals(userId)) {
                list.add(item);
            }
        }
        return list;

    }

    public List<Item> getItems() {
        return new ArrayList<>(itemRepository.values());

    }


}
