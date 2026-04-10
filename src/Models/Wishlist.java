package Models;

import java.util.ArrayList;
import java.util.List;

public class Wishlist {
    private List<Wish> wishes;

    public Wishlist() {
        this.wishes = new ArrayList<>();
    }

    public void addWish(Wish wish) {
        wishes.add(wish);
    }


    public void showAllWishlist() {
        if (wishes.isEmpty()) {
            System.out.println("Ønskelisten er tom.");
        } else {
            System.out.println("Ønskeliste:");
            for (int i = 0; i < wishes.size(); i++) {
                System.out.println((i + 1) + ". " + wishes.get(i));
            }
        }
    }
}