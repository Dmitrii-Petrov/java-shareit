package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class ItemMapper {

//    public static ItemDto itemToDto(Item item) {
//        return new ItemDto(
//                item.getName(),
//                item.getDescription(),
//                item.getAvailable(),
//                item.getRequest()
//        );
//    }

    public static Item dtoToItem(Long id, ItemDto itemDto, Long userId) {
        return new Item(
                id,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                itemDto.getRequest()
        );
    }

    public static Item mapToNewItem(ItemDto itemDto, Long id) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(id);
        item.setRequest(itemDto.getRequest());
        return item;
    }
}
