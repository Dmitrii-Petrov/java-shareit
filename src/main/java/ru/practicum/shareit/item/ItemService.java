package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import ru.practicum.shareit.request.storage.RequestRepository;
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

    private final RequestRepository requestRepository;

    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByUserId(Long userId, Integer from, Integer size) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from / size, size, sort);
        Page<Item> itemPage = itemRepository.findByOwnerOrderById(userId, page);

        List<ItemDto> result = new ArrayList<>();

        itemPage.getContent().forEach(item -> {
            ItemDto itemDto = itemToDto(item);
            if (!bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(), Status.APPROVED).isEmpty()) {
                itemDto.setNextBooking(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(), Status.APPROVED).get(0));
            }
            if (!bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(), Status.APPROVED).isEmpty()) {
                itemDto.setLastBooking(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(), Status.APPROVED).get(0));
            }
            result.add(itemDto);
        });

        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();
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

    public ItemDto create(ItemDto itemDto, Long userId) {
        if (!userService.findUserById(userId)) {
            throw new NotFoundEntityException();
        }
        if ((itemDto.getRequestId() != null) && (!requestRepository.existsById(itemDto.getRequestId()))) {
            throw new NotFoundEntityException();
        }
        Item item = mapToNewItem(itemDto, userId);
        ItemDto itemDto1 = itemToDto(itemRepository.save(item));
        itemDto1.setRequestId(itemDto.getRequestId());
        return itemDto1;
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
                itemDto.getRequest() != null ? itemDto.getRequest() : item.getRequestId()
        ));
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsByTextSearch(String text, Integer from, Integer size) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        List<Item> result = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from / size, size, sort);
        Page<Item> itemPage;

        if (text.isBlank()) {
            return result;
        }
        itemPage = itemRepository.findByAvailableAndDescriptionContainingIgnoreCaseOrAvailableAndNameContainingIgnoreCase(true, text, true, text, page);

        result.addAll(itemPage.getContent());

        if (result.size() > from % size) {
            result = result.subList(from % size, result.size());
        } else result.clear();

        return result;
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
