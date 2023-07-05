package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class ItemMapper {

    public static ItemDto itemToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId(),
                null,
                null,
                new ArrayList<>(),
                null
        );
    }

    public static Item mapToNewItem(ItemDto itemDto, Long id) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(id);
        item.setRequestId(itemDto.getRequestId());
        return item;
    }
}
