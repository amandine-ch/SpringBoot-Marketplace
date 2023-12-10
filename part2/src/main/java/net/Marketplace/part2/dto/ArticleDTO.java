package net.Marketplace.part2.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
/**
 * Data Transfer Object (DTO) representing product information.
 */
@Data
public class ArticleDTO {
    private UUID id;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Price should not be empty")
    private float price;
    @NotNull(message = "Stock should not be empty")
    private int stock;
    @NotEmpty(message = "Image should not be empty")
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
