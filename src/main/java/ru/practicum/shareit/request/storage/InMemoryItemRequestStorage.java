package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryItemRequestStorage implements ItemRequestStorage {
    HashMap<Long, ItemRequest> requestRepository = new HashMap<>();
    long generatorId = 0;

    public long generateId() {
        return ++generatorId;
    }

    @Override
    public void save(ItemRequest request) {
        request.setId(generateId());
        requestRepository.put(request.getId(), request);
    }

    @Override
    public void update(ItemRequest request) {
        if (!requestRepository.containsKey(request.getId())) {
            throw new NotFoundEntityException();
        }
        requestRepository.put(request.getId(), request);
    }

    @Override
    public void delete(Long id) {
        if (!requestRepository.containsKey(id)) {
            throw new NotFoundEntityException();
        }
        requestRepository.remove(id);
    }


    @Override
    public ItemRequest getItemRequest(Long id) {
        if (!requestRepository.containsKey(id)) {
            throw new NotFoundEntityException();
        }
        return requestRepository.get(id);
    }

    @Override
    public List<ItemRequest> getItemRequests() {
        return new ArrayList<>(requestRepository.values());
    }


}
