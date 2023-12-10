package net.Marketplace.part2.utils;

import net.Marketplace.part2.entity.Articles;

/**
 * Represents an item in the shopping cart.
 */
public class CartItem {
    private Articles article; // The article in the cart
    private int quantity;     // The quantity of the article

    /**
     * Constructor to create a CartItem with an article and quantity.
     *
     * @param article   The article to add to the cart.
     * @param quantity  The quantity of the article to add.
     */
    public CartItem(Articles article, int quantity) {
        this.article = article;
        this.quantity = quantity;
    }

    // Getters and setters

    /**
     * Retrieves the article in the cart.
     *
     * @return The article in the cart.
     */
    public Articles getArticle() {
        return article;
    }
    /**
     * Sets the article in the cart.
     *
     * @param article The article to set in the cart.
     */
    public void setArticle(Articles article) {
        this.article = article;
    }
    /**
     * Retrieves the quantity of the article in the cart.
     *
     * @return The quantity of the article.
     */
    public int getQuantity() {
        return quantity;
    }
    /**
     * Sets the quantity of the article in the cart.
     *
     * @param quantity The quantity of the article to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    /**
     * Calculates the total price of the item in the cart.
     *
     * @return The total price of the item based on its quantity and price.
     */
    public float getItemTotal() {
        return article.getPrice() * quantity;
    }
}
