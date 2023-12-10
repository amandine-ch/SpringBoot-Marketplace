package net.Marketplace.part2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Controller class handling the confirmation payment page.
 */
@Controller
public class ConfirmationPaymentController {
    /**
     * Displays the confirmation payment page.
     *
     * @return redirect to confirmationPayment page.
     */
    @GetMapping("/order/confirmationPayment")
    public String showConfirmationPage() {
        // Logic for displaying the confirmation payment form
        return "confirmationPayment";
    }
}
