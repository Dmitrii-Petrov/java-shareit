package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;

public interface ItemRequestStorage {
    void save(ItemRequest request);

    void update(ItemRequest request);

    void delete(Long id);

    ItemRequest getItemRequest(Long id);

    ArrayList<ItemRequest> getItemRequests();
}
