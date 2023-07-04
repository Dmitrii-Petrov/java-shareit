package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByOwnerOrderById(Long owner, Pageable pageable);

    List<Item> findByRequestIdOrderById(Long request);

    Page<Item> findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase(Boolean bool, String text, Boolean bool1, String text1, Pageable pageable);
}

