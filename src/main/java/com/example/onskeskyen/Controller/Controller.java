package com.example.onskeskyen.Controller;

import com.example.onskeskyen.Models.User;
import com.example.onskeskyen.Models.Wish;
import com.example.onskeskyen.Models.Wishlist;
import com.example.onskeskyen.Repos.UserRepository;
import com.example.onskeskyen.Repos.WishRepository;
import com.example.onskeskyen.Repos.WishlistRepository;
import com.example.onskeskyen.Services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========== AUTH ==========

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ========== MY WISHLISTS (overview) ==========

    @GetMapping("/wishes")
    public String myWishlists(Model model) {

        User user = userService.getCurrentUser();

        List<Wishlist> wishlists = wishlistRepository.findByUser(user);

        model.addAttribute("wishlists", wishlists);
        model.addAttribute("reservedWishes", wishRepository.findByReservedBy(user));

        return "wishes";
    }

    /** Create a new wishlist */
    @PostMapping("/wishlists/create")
    public String createWishlist(@RequestParam String name) {

        User user = userService.getCurrentUser();

        Wishlist wishlist = new Wishlist();
        wishlist.setName(name);
        wishlist.setUser(user);

        wishlistRepository.save(wishlist);

        return "redirect:/wishes";
    }

    /** Delete a wishlist */
    @PostMapping("/wishlists/delete")
    public String deleteWishlist(@RequestParam Long id) {

        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow();
        User user = userService.getCurrentUser();

        if (wishlist.getUser().getId().equals(user.getId())) {
            wishlistRepository.delete(wishlist);
        }

        return "redirect:/wishes";
    }

    // ========== WISHES INSIDE A WISHLIST (owner view) ==========

    /** View wishes in one of my wishlists */
    @GetMapping("/wishlists/{wishlistId}")
    public String viewMyWishlist(@PathVariable Long wishlistId, Model model) {

        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow();
        User user = userService.getCurrentUser();

        // Only the owner can use this view
        if (!wishlist.getUser().getId().equals(user.getId())) {
            return "redirect:/wishes";
        }

        model.addAttribute("wishlist", wishlist);
        model.addAttribute("wishes", wishRepository.findByWishlist(wishlist));

        return "myWishlist";
    }

    /** Add a wish to a wishlist */
    @PostMapping("/wishlists/{wishlistId}/add")
    public String addWish(@PathVariable Long wishlistId,
                          @RequestParam String text) {

        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow();
        User user = userService.getCurrentUser();

        if (!wishlist.getUser().getId().equals(user.getId())) {
            return "redirect:/wishes";
        }

        Wish wish = new Wish();
        wish.setText(text);
        wish.setUser(user);
        wish.setWishlist(wishlist);

        wishRepository.save(wish);

        return "redirect:/wishlists/" + wishlistId;
    }

    /** Delete a wish */
    @PostMapping("/wishes/delete")
    public String deleteWish(@RequestParam Long id) {

        Wish wish = wishRepository.findById(id).orElseThrow();
        User user = userService.getCurrentUser();

        if (wish.getUser().getId().equals(user.getId())) {
            Long wishlistId = wish.getWishlist().getId();
            wishRepository.delete(wish);
            return "redirect:/wishlists/" + wishlistId;
        }

        return "redirect:/wishes";
    }

    /** Edit wish page */
    @GetMapping("/wishes/edit")
    public String editPage(@RequestParam Long id, Model model) {

        Wish wish = wishRepository.findById(id).orElseThrow();
        model.addAttribute("wish", wish);

        return "editWish";
    }

    /** Update a wish */
    @PostMapping("/wishes/edit")
    public String updateWish(@RequestParam Long id,
                             @RequestParam String text) {

        Wish wish = wishRepository.findById(id).orElseThrow();
        User user = userService.getCurrentUser();

        if (wish.getUser().getId().equals(user.getId())) {
            wish.setText(text);
            wishRepository.save(wish);
            return "redirect:/wishlists/" + wish.getWishlist().getId();
        }

        return "redirect:/wishes";
    }

    // ========== SHAREABLE LINK (public, no login required) ==========

    /** Anyone with the link can view a wishlist */
    @GetMapping("/wishlist/{wishlistId}")
    public String sharedWishlist(@PathVariable Long wishlistId, Model model) {

        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow();

        model.addAttribute("wishlist", wishlist);
        model.addAttribute("wishes", wishRepository.findByWishlist(wishlist));
        model.addAttribute("profileUser", wishlist.getUser());

        // Check if a user is logged in (for reserve functionality)
        User currentUser = null;
        try {
            currentUser = userService.getCurrentUser();
        } catch (Exception ignored) {
            // Not logged in
        }
        model.addAttribute("currentUser", currentUser);

        // Don't show reservation status if the viewer is the owner
        boolean isOwner = currentUser != null
                && currentUser.getId().equals(wishlist.getUser().getId());
        model.addAttribute("isOwner", isOwner);

        return "sharedWishlist";
    }

    // ========== RESERVE / UNRESERVE ==========

    /** Reserve a wish */
    @PostMapping("/wishes/reserve")
    public String reserveWish(@RequestParam Long id,
                              @RequestParam Long wishlistId) {

        Wish wish = wishRepository.findById(id).orElseThrow();
        User currentUser = userService.getCurrentUser();

        if (!wish.getUser().getId().equals(currentUser.getId()) && !wish.isReserved()) {
            wish.setReservedBy(currentUser);
            wishRepository.save(wish);
        }

        return "redirect:/wishlist/" + wishlistId;
    }

    /** Unreserve a wish */
    @PostMapping("/wishes/unreserve")
    public String unreserveWish(@RequestParam Long id,
                                @RequestParam Long wishlistId) {

        Wish wish = wishRepository.findById(id).orElseThrow();
        User currentUser = userService.getCurrentUser();

        if (wish.isReserved() && wish.getReservedBy().getId().equals(currentUser.getId())) {
            wish.setReservedBy(null);
            wishRepository.save(wish);
        }

        return "redirect:/wishlist/" + wishlistId;
    }

    // ========== SOCIAL: BROWSE USERS ==========

    @GetMapping("/users")
    public String usersPage(@RequestParam(required = false) String search, Model model) {

        User currentUser = userService.getCurrentUser();
        List<User> users;

        if (search != null && !search.isBlank()) {
            users = userRepository.findByNameContainingIgnoreCase(search);
        } else {
            users = userRepository.findAll();
        }

        users.removeIf(u -> u.getId().equals(currentUser.getId()));

        model.addAttribute("users", users);
        model.addAttribute("search", search);

        return "users";
    }

    /** View another user's wishlists */
    @GetMapping("/users/{userId}/wishlists")
    public String viewUserWishlists(@PathVariable Long userId, Model model) {

        User currentUser = userService.getCurrentUser();
        User profileUser = userRepository.findById(userId).orElseThrow();

        if (profileUser.getId().equals(currentUser.getId())) {
            return "redirect:/wishes";
        }

        List<Wishlist> wishlists = wishlistRepository.findByUser(profileUser);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("wishlists", wishlists);

        return "userWishlists";
    }
}