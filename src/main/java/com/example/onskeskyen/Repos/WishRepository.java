package com.example.onskeskyen.Repos;

import com.example.onskeskyen.Models.Wish;
import com.example.onskeskyen.Models.Wishlist;
import com.example.onskeskyen.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUser(User user);
    List<Wish> findByWishlist(Wishlist wishlist);
    List<Wish> findByReservedBy(User reservedBy);
}