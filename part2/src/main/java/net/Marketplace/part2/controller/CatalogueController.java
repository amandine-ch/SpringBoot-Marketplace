package net.Marketplace.part2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.Marketplace.part2.entity.Articles;
import net.Marketplace.part2.repository.ArticlesRepository;
import net.Marketplace.part2.utils.Cart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller class managing catalogue-related operations.
 */
@Controller
public class CatalogueController {

    private final ArticlesRepository articlesRepository;
    /**
     * Constructor for CatalogueController.
     *
     * @param articlesRepository Repository for managing articles data.
     */
    public CatalogueController(ArticlesRepository articlesRepository){
        this.articlesRepository = articlesRepository;
    }
    /**
     * Displays the catalogue page with a list of articles.
     *
     * @param model The model to be populated with data for rendering the view.
     * @return redirect to catalogue.
     */
    @GetMapping("/catalogue")
    public String catalogue(Model model) {
        List<Articles> articlesList = articlesRepository.findAll();
        model.addAttribute("articles", articlesList);

        return "catalogue";
    }
    /**
     * Displays the product page for a specific product.
     *
     * @param model     The model to be populated with data for rendering the view.
     * @param productID The ID of the product to display.
     * @param request   The HTTP request.
     * @return Redirect to product.
     */
    @GetMapping("/product/{productID}")
    public String product(Model model, @PathVariable String productID, HttpServletRequest request) {
        Articles product = articlesRepository.getById(UUID.fromString(productID));
        model.addAttribute("product", product);

        HttpSession session = request.getSession();
        String message = (String) session.getAttribute("message");
        session.removeAttribute("message"); // Remove the message from the session after retrieving it
        // Add the message to the model to display it in the view
        model.addAttribute("message", message != null ? message : "");

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        return "product";
    }


}
