package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.itemsub.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void 상품주문() throws Exception {
        //given
        Member member = createMember("sanghun", new Address("city", "street", "zipcode"));

        Item item = createBook("cubox", 3000, 1000);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        entityManager.flush();

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, order.getStatus());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(item.getPrice() * 2, order.getTotalPrice());
    }

    @Test
    void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("sanghun", new Address("city", "street", "zipcode"));
        Item item = createBook("book", 2000, 30);

        int orderItemCount = 34;

        //when
        NotEnoughStockException thrown = assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderItemCount);
        });

        //then
        assertEquals("재고 부족", thrown.getMessage());
    }

    @Test
    void 주문취소() throws Exception {
        //given
        Member member = createMember("sanghun", new Address("city", "street", "zipcode"));
        Item item = createBook("cubox", 5000, 10);

        int orderItemCount = 3;

        Long orderId = orderService.order(member.getId(), item.getId(), orderItemCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, order.getStatus());
        assertEquals(10, item.getStockQuantity());
    }

    private Item createBook(String itemName, int itemPrice, int itemStockQUantity) {
        Item item = new Book();
        item.setName(itemName);
        item.setPrice(itemPrice);
        item.setStockQuantity(itemStockQUantity);
        itemService.saveItem(item);
        return item;
    }

    private Member createMember(String memberName, Address memberAddress) {
        Member member = new Member();
        member.setName(memberName);
        member.setAddress(memberAddress);
        memberService.save(member);
        return member;
    }
}