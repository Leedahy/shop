package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.shop.entity.QItem.*;
import static org.junit.jupiter.api.Assertions.*;

//@Transactional
@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager em;

    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = Item.builder()
                    .itemNM("테스트 상품" + i)
                    .price(10000 + i)
                    .stockNum(1001 + i)
                    .itemDetail("아름다운 가방입니다" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            System.out.println(" ============아이템=============" + item);
            Item item1= itemRepository.save(item);
            System.out.println(" ============저장된아이템=============" + item1);


        }
    }
    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = Item.builder()
                    .itemNM("테스트 상품" + i)
                    .price(10000 + i)
                    .stockNum(1001 + i)
                    .itemDetail("아름다운 가방입니다" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            System.out.println(" ============아이템=============" + item);
            Item item1 = itemRepository.save(item);
            System.out.println(" ============저장된아이템=============" + item1);
        }
            for (int i = 6; i <= 10; i++) {
            Item item = Item.builder()
                    .itemNM("테스트 상품" + i)
                    .price(10000 + i)
                    .stockNum(1001 + i)
                    .itemDetail("아름다운 가방입니다" + i)
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            System.out.println(" ============아이템=============" + item);
            Item item1= itemRepository.save(item);
            System.out.println(" ============저장된아이템=============" + item1);


        }
    }

    @Test
    @DisplayName("querydsl 테스트2")
    public void querydslTest2(){
        createItemList2();

        BooleanBuilder builder = new BooleanBuilder();
        String itemDetail = "테스트";
        int price = 10002;
        String itemSellStatus = "SELL";
        QItem item = QItem.item;

        builder.and(item.itemDetail.like("%" + itemDetail + "%"));
        builder.and((item.price.gt(price)));

        if(StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)){
            builder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);

        Page<Item> page = itemRepository.findAll(builder, pageable);
        List<Item> content = page.getContent();
        content.stream().forEach((e) -> {
            System.out.println(e);
        });
    }

        @Test
        @DisplayName("querydsl 테스트")
        public void querydslTest(){
            createItemList();
            JPAQueryFactory query = new JPAQueryFactory(em);
            //QItem qItem = item;

            List<Item> itemList = query.selectFrom(item)
                    .where(item.itemSellStatus.eq(ItemSellStatus.SELL))
                    .where(item.itemDetail.like("%" + "1" + "%"))
                    .orderBy(item.price.desc())
                    .fetch();

            itemList.forEach((item-> System.out.println("dsl결과" +item)));


        }

        @Test
        @DisplayName("Native 테스트")
        public void findByDetailNativeTest(){
            createItemList();
            //like 조건으로 1이 포함된 모든 레코드를 찾아라
            //찐 오라클 문법을 그대로 따름
            itemRepository.findByDetailNative("1")
                    .forEach((item) -> {
                        System.out.println("Native결과" +item);
                    });
        }
        @Test
        @DisplayName("JPQL 테스트")
        public void findByDetailTest(){
            createItemList();
            //like 조건으로 1이 포함된 모든 레코드를 찾아라
            itemRepository.findByDetail("1")
                    .forEach((item) -> {
                        System.out.println("JPQL결과" +item);
                    });
        }

        @Test
        @DisplayName("정렬 테스트")
        public void findByPriceLessThanOrderByPriceDesc() {
            createItemList();
            itemRepository.findByPriceLessThanOrderByPriceDesc(10005)
                    .forEach((item -> System.out.println("정렬결과" +item)));
        }

        @Test
        @DisplayName("Or 테스트")
        public void findByItemNMOrItemDetailTest() {
            createItemList();
            List<Item> itemList = itemRepository.findByItemNMOrItemDetail("테스트 상품2", "아름다운 가방입니다8");
            itemList.forEach((item) -> {
                System.out.println("or 검색결과" + item);
            });
        }

        @Test
        @DisplayName("조회테스트")
        public void findByItemNMTest(){
            this.createItemList();
            List<Item> itemList = itemRepository.findByItemNM("테스트 상품1");
            for (Item item : itemList) {
                System.out.println("찾은 결과" +       item);
            }
        }

        @Test
        @DisplayName("상품 저장 테스트")
        public void createItemTest() {
//        Item item = new Item();
            Item item = Item.builder()
                    .itemNM("테스트 상품")
                    .price(10000)
                    .stockNum(1001)
                    .itemDetail("아름다운 가방입니다")
                    .itemSellStatus(ItemSellStatus.SELL)
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())

                    .build();
            item.setItemNM("테스트 상품");
            System.out.println(" ============아이템=============" + item);
            Item item1= itemRepository.save(item);
            System.out.println(" ============저장된아이템=============" + item1);


        }
    }

