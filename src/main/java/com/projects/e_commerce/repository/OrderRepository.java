package com.projects.e_commerce.repository;

import com.projects.e_commerce.order.entity.Order;
import com.projects.e_commerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}

