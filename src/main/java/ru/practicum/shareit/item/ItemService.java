package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundEntityException;
import ru.practicum.shareit.exceptions.WrongEntityException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.item.model.CommentMapper.commentToDto;
import static ru.practicum.shareit.item.model.CommentMapper.mapToNewComment;
import static ru.practicum.shareit.item.model.ItemMapper.itemToDto;
import static ru.practicum.shareit.item.model.ItemMapper.mapToNewItem;

@Service("itemService")
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByUserId(Long userId) {
        List<Item> list = itemRepository.findByOwnerOrderById(userId);
        List<ItemDto> result = new ArrayList<>();
        for (Item item : list) {
            ItemDto itemDto = itemToDto(item);
            if (!bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(), Status.APPROVED).isEmpty()) {
                itemDto.setNextBooking(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(), Status.APPROVED).get(0));
            }
            if (!bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(), Status.APPROVED).isEmpty()) {
                itemDto.setLastBooking(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(), Status.APPROVED).get(0));
            }
            result.add(itemDto);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Boolean findUserById(Long userId) {
        return userService.findUserById(userId);
    }

    @Transactional(readOnly = true)
    public Item getItemById(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundEntityException();
        }
        return itemRepository.findById(itemId).get();
    }

    @Transactional(readOnly = true)
    public ItemDto getItemDtoByItemId(Long itemId, Long userId) {
        Item item = getItemById(itemId);
        ItemDto itemDto = itemToDto(item);
        if (item.getOwner().equals(userId)) {
            if (!bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId, LocalDateTime.now(), Status.APPROVED).isEmpty()) {
                itemDto.setNextBooking(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId, LocalDateTime.now(), Status.APPROVED).get(0));
            }
            if (!bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(itemId, LocalDateTime.now(), Status.APPROVED).isEmpty()) {
                itemDto.setLastBooking(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(itemId, LocalDateTime.now(), Status.APPROVED).get(0));
            }
        }
        if (commentRepository.findByItem_IdOrderByCreatedDesc(itemId) != null) {
            List<Comment> list = (commentRepository.findByItem_IdOrderByCreatedDesc(itemId));
            List<CommentDto> commentDtoList = new ArrayList<>();
            for (Comment comment : list) {
                commentDtoList.add(commentToDto(comment));
            }
            itemDto.setComments(commentDtoList);
        }
        return itemDto;
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

    @Transactional(readOnly = true)
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

    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundEntityException();
        }
        if (!userService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        List<Booking> list = bookingRepository.findByBookerIdAndStatusAndEndBeforeOrderByIdDesc(userId, Status.APPROVED, LocalDateTime.now());
        boolean bool = false;
        for (Booking booking : list) {
            if (booking.getItem().getId().equals(itemId)) {
                bool = true;
                break;
            }
        }
        if (bool) {
            Comment comment = mapToNewComment(commentDto, userService.getUsersById(userId).get(), itemRepository.findById(itemId).get());
            return commentToDto(commentRepository.save(comment));
        } else throw new WrongEntityException();

    }
}
