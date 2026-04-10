import Models.Wish;
import Models.Wishlist;

public class Main {
    public static void main(String[] args) {
        Wishlist wishlist = new Wishlist();

        wishlist.addWish(new Wish("PlayStation 5", "Spilkonsol"));
        wishlist.addWish(new Wish("Ny telefon", "iPhone eller Samsung"));


        wishlist.showAllWishlist();
    }
}