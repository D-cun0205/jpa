package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.itemsub.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class Initdb {

    private final InitService initService;

    @PostConstruct
    public void postConstruct() {
        initService.initdb1();
        initService.initdb2();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {

        private final EntityManager em;

        public void initdb1() {
            Member member = createMember(
                    "user2",
                    new Address("cityA", "streetA", "zipcodeA"));

            Book book1 = createBook("JPA1 Book", 5000, 100);

            Book book2 = createBook("JPA2 Book", 10000, 200);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 5);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 3);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void initdb2() {
            Member member = createMember(
                    "user2",
                    new Address("cityB", "streetB", "zipcodeB"));

            Book book1 = createBook("Spring1 Book", 20000, 100);

            Book book2 = createBook("Spring2 Book", 30000, 200);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 5);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 3);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, Address address) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(address);
            em.persist(member);
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            em.persist(book1);
            return book1;
        }
    }

}
