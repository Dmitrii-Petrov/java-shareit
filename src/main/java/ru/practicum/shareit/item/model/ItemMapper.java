package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class ItemMapper {

    public static ItemDto itemToDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static Item dtoToItem(Long id, ItemDto itemDto, Long userId) {
        return new Item(
                id,
                itemDto.getName() != null ? itemDto.getName() : null,
                itemDto.getDescription() != null ? itemDto.getDescription() : null,
                itemDto.getAvailable() != null ? itemDto.getAvailable() : null,
                userId,
                itemDto.getRequest() != null ? itemDto.getRequest() : null
        );
    }
}
