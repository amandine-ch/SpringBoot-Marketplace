package net.Marketplace.part2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.Marketplace.part2.entity.Articles;
import net.Marketplace.part2.repository.ArticlesRepository;
import net.Marketplace.part2.utils.Cart;
import net.Marketplace.part2.utils.CartItem;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@SessionAttributes("cart")
public class CartController {
    private final ArticlesRepository articlesRepository;
    /**
     * Constructor for CartController.
     *
     * @param articlesRepository Repository for managing articles data.
     */
    public CartController(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    /**
     * Adds an item to the cart.
     *
     * @param request            The HTTP request.
     * @param productId          The ID of the product to add.
     * @param quantity           The quantity of the product to add.
     * @param redirectAttributes Redirect attributes for passing messages.
     * @return The redirection path.
     */
    @PostMapping("/addToCart")
    public String addToCart(HttpServletRequest request,
                            @RequestParam("productId") UUID productId,
                            @RequestParam(value="quantity", defaultValue ="1") int quantity,
                            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        Articles product = articlesRepository.getById(productId);
        if (product != null && quantity > 0 && quantity <= product.getStock()) {
            // Ajouter l'article avec sa quantité au panier
            // Add item to cart
            cart.addItem(product, quantity);

            session.setAttribute("cart", cart);

            // Utilisation de RedirectAttributes pour passer des messages à la page
            redirectAttributes.addAttribute("productID", productId);
            session.setAttribute("message", "L'article a été ajouté au panier!");
        } else {
            // Rediriger avec un message d'erreur
            redirectAttributes.addAttribute("productID", productId);
            session.setAttribute("message", "Erreur : quantité non valide ou stock indisponible");
        }

        return "redirect:/product/{productID}#cartSection";
    }
    /**
     * Retrieves the cart as a model attribute.
     *
     * @return The cart as a model attribute.
     */
    @ModelAttribute("cart")
    public Cart getCart() {
        return new Cart();
    }
    /**
     * Displays the cart contents.
     *
     * @param model   The model to be populated with data for rendering the view.
     * @param session The HTTP session.
     * @return The name of the cart view.
     */
    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("cartTotal", cart.getTotal());
        model.addAttribute("cart", cart);
        return "cart";
    }
    /**
     * Deletes an item from the cart.
     *
     * @param articleId The ID of the article to remove from the cart.
     * @param session   The HTTP session.
     * @return The redirection path.
     */
    @PostMapping("/deleteFromCart")
    public String deleteFromCart(@RequestParam("articleId") UUID articleId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.removeItem(articleId);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }
    /**
     * Handles the checkout process.
     *
     * @param request The HTTP request.
     * @param session The HTTP session.
     * @return The redirection path.
     */
    @GetMapping("/checkout")
    public String checkout(HttpServletRequest request, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            // L'utilisateur est connecté et a le rôle d'utilisateur, redirigez-le vers le formulaire de commande
            return "redirect:/order/";
        } else {
            session.setAttribute("fromCart", true);
            // L'utilisateur n'est pas connecté, redirigez-le vers la page de connexion
            return "redirect:/login";
        }
    }
}
