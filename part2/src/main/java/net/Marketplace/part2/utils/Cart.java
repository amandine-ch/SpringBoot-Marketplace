package net.Marketplace.part2.utils;

import net.Marketplace.part2.entity.Articles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a shopping cart that contains cart items.
 */
public class Cart {
    private List<CartItem> cartItems;// List of items in the cart
    /**
     * Constructs a Cart with an empty list of cart items.
     */
    public Cart() {
        this.cartItems = new ArrayList<>();
    }
    /**
     * Retrieves the list of cart items.
     *
     * @return The list of cart items.
     */
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    /**
     * Sets the list of cart items.
     *
     * @param cartItems The list of cart items to set.
     */
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    /**
     * Adds an article with a certain quantity to the cart.
     * If the article is already in the cart, it updates the quantity; otherwise, adds a new item.
     *
     * @param article  The article to add.
     * @param quantity The quantity of the article to add.
     */
    public void addItem(Articles article, int quantity) {
        // Checks if the article is already in the cart, if yes, updates the quantity
        boolean found = false;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getArticle().getId().equals(article.getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        // If the article is not in the cart, adds it
        if (!found) {
            CartItem newItem = new CartItem(article, quantity);
            cartItems.add(newItem);
        }
    }
    /**
     * Removes an item from the cart based on the article ID.
     *
     * @param articleId The ID of the article to remove.
     */
    public void removeItem(UUID articleId) {
        cartItems.removeIf(cartItem -> cartItem.getArticle().getId().equals(articleId));
    }
    /**
     * Calculates the total price of all items in the cart.
     *
     * @return The total price of all items in the cart.
     */
    public float getTotal() {
        float total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getItemTotal();
        }
        return total;
    }

}
