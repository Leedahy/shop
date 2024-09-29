package com.shop.repository;

import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
        List<Item> findByItemNM(String itemNM);

        List<Item> findByItemNMOrItemDetail(String itemNM, String itemDetail);

        List<Item> findByPriceLessThanOrderByPriceDesc(int price);

        @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc ")
        List<Item> findByDetail(@Param("itemDetail") String itemDetail);           //JPQL

        @Query(value = "select * from Item i where i.item_detail like %:itemDetail% order by i.price desc ", nativeQuery = true)
        List<Item> findByDetailNative(@Param("itemDetail") String itemDetail);           //Native


}
