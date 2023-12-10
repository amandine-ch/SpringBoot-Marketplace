package net.Marketplace.part2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.Marketplace.part2.entity.Articles;
import net.Marketplace.part2.entity.Users;
import net.Marketplace.part2.repository.ArticlesRepository;
import net.Marketplace.part2.repository.UsersRepository;
import net.Marketplace.part2.utils.Cart;
import net.Marketplace.part2.utils.CartItem;
import net.Marketplace.part2.utils.EmailUtil;
import net.Marketplace.part2.utils.ValidationUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
/**
 * Controller class handling payment-related operations.
 */
@Controller
public class PaymentController {
    private final UsersRepository usersRepository;
    private final UserDetailsService userDetailsService;
    private final ArticlesRepository articlesRepository;
    /**
     * Constructor for PaymentController.
     *
     * @param usersRepository     Repository for managing user data.
     * @param userDetailsService Service for managing user details.
     * @param articlesRepository  Repository for managing articles data.
     */
    public PaymentController(UsersRepository usersRepository, UserDetailsService userDetailsService, ArticlesRepository articlesRepository) {
        this.usersRepository = usersRepository;
        this.userDetailsService = userDetailsService;
        this.articlesRepository = articlesRepository;
    }

    /**
     * Displays the payment form for an order.
     *
     * @return payment-form page.
     */
    @GetMapping("/order/payment/")
    public String showPaymentForm() {
        // Logic for displaying the order form
        return "payment-form";
    }
    /**
     * Validates the order form for payment.
     *
     * @param request             The HTTP request.
     * @param nomCarte            The name on the card.
     * @param numeroCarte         The card number.
     * @param dateExpiration      The expiration date of the card.
     * @param codeCarte           The card's security code.
     * @param usePointsString     String indicating if loyalty points are used.
     * @return The redirection URL based on the validation result.
     */
    @PostMapping("/order/infoPayment")
    public String validateOrderForm(
            HttpServletRequest request,
            @RequestParam(name = "nomCarte") String nomCarte,
            @RequestParam(name = "numeroCarte") String numeroCarte,
            @RequestParam(name = "dateExpiration") String dateExpiration,
            @RequestParam(name = "codeCarte") String codeCarte,
            @RequestParam(name = "usePoints", required = false) String usePointsString
    ) {

        HttpSession session = request.getSession();
        // On récupère l'attribut permettant de savoir si l'utilisateur a utilisé ses points ou non
        boolean usePoints = false;
        if(usePointsString != null) {
            usePoints = usePointsString.equals("on");
        }

        // On récupère les informations entrées par l'utilisateur sur la page infopersonnal.jsp
        Object personnalInformationObject = session.getAttribute("personnalInformation");

        // On vérifie si l'ensemble des informations récupérées sont correctes ou non
        boolean correctValues = ValidationUtils.checkValues(nomCarte, numeroCarte, dateExpiration, codeCarte, personnalInformationObject);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(correctValues) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Users user = (Users) userDetails;
            Map<String, String> personnalInformation = (Map<String, String>) personnalInformationObject;
            Cart cart = (Cart) session.getAttribute("cart");

            // Pour chaque article, on modifie son stock et on calcule le prix total
            float total = 0;
            for(CartItem item : cart.getCartItems()) {
                Articles modifiedArticle = item.getArticle();
                modifiedArticle.setStock(modifiedArticle.getStock() - item.getQuantity());
                articlesRepository.save(modifiedArticle);
                total += item.getArticle().getPrice() * item.getQuantity();
            }

            int loyaltyPoints = 0;
            if (usePoints) {
                loyaltyPoints = user.getLoyaltyPoints();
                total -= (float) user.getLoyaltyPoints() / 100;
                usersRepository.updateLoyalityPoints((int) total, user.getId());
            } else {
                usersRepository.updateLoyalityPoints(user.getLoyaltyPoints() + (int) total, user.getId());
            }

            // Envoie de l'email récapitulatif
            EmailUtil.sendRecapMail(user, cart, personnalInformation, total, loyaltyPoints);

            // Supprimer toutes les variables de session qui ne sont plus utiles
            session.removeAttribute("cart");
            session.removeAttribute("personnalInformation");
            session.removeAttribute("error");

            return "redirect:/order/confirmationPayment";
        }
        else {
            return "redirect:/order/payment/";
        }
    }
}
