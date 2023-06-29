package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.shareit.item.model.ItemMapper.mapToNewItem;

@Service("itemService")
@RequiredArgsConstructor
public class ItemService {

    ItemRepository itemRepository;
    UserService userService;


    @Autowired
    public ItemService(UserService userService, ItemRepository itemRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    public List<Item> getItemsByUserId(Long userId) {
        return itemRepository.findByOwner(userId);
    }

    public Optional<Item> getItemById(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundEntityException();
        }
        return itemRepository.findById(itemId);
    }

    public Item create(ItemDto itemDto, Long userId) {
        if (!userService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        Item item = mapToNewItem(itemDto, userId);
        itemRepository.save(item);
        return item;
    }

    public Item update(Long id, ItemDto itemDto, Long userId) {
        if ((!userService.findUserById(userId)) || (!itemRepository.existsById(id)) || (!Objects.equals(itemRepository.findById(id).get().getOwner(), userId))) {
            throw new NotFoundEntityException();
        }
        Item item = itemRepository.findById(id).get();
        return itemRepository.save(new Item(
                id,
                itemDto.getName() != null ? itemDto.getName() : item.getName(),
                itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription(),
                itemDto.getAvailable() != null ? itemDto.getAvailable() : item.getAvailable(),
                userId,
                itemDto.getRequest() != null ? itemDto.getRequest() : item.getRequest()
        ));
    }

    public void delete(Long id) {
        itemRepository.delete(itemRepository.findById(id).get());
    }

    public List<Item> getItemsByTextSearch(String text) {
        List<Item> list = new ArrayList<>();
        if (text.isBlank()) {
            return list;
        }
        for (Item item : itemRepository.findAll()) {
            if ((item.getAvailable()) && ((item.getDescription().toLowerCase().contains(text.toLowerCase())) || (item.getName().toLowerCase().contains(text.toLowerCase())))) {
                list.add(item);
            }
        }
        return list;
    }
}
