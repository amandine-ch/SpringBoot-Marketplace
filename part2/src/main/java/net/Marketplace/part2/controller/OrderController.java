package net.Marketplace.part2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller class handling order-related operations.
 */
@Controller
public class OrderController {
    /**
     * Displays the order form.
     *
     * @return redirect to order-form page.
     */
    @GetMapping("/order/")
    public String showOrderForm() {
        // Logic for displaying the order form
        return "order-form";
    }

    /**
     * Checks if the given fields are empty.
     *
     * @param nomComplet The full name.
     * @param adresse    The address.
     * @param telephone  The telephone number.
     * @return true if any of the fields is empty, otherwise false.
     */
    public boolean checkEmpty(String nomComplet, String adresse, String telephone) {
        if((nomComplet == null || nomComplet.isEmpty()) ||
                (adresse == null || adresse.isEmpty()) ||
                (telephone == null || telephone.isEmpty())) {
            return true;
        }
        return false;
    }
    /**
     * Validates the order form for personal information.
     *
     * @param nomComplet The full name entered in the form.
     * @param adresse    The address entered in the form.
     * @param telephone  The telephone number entered in the form.
     * @param session    The HTTP session.
     * @return Redirect to /order/ page.
     */
    @PostMapping("/order/infoPersonnal")
    public String validateOrderForm(
            @RequestParam(name = "nomComplet") String nomComplet,
            @RequestParam(name = "adresse") String adresse,
            @RequestParam(name = "telephone") String telephone,
            HttpSession session){
        // Checks if the fields are not empty
        boolean correctValues = !checkEmpty(nomComplet, adresse, telephone);

        if(correctValues) {
            // Stores personal information in session
            Map<String, String> personnalInformation = new HashMap<String, String>()
            {{
                put("nomComplet", nomComplet);
                put("adresse", adresse);
                put("telephone", telephone);
            }};
            session.setAttribute("personnalInformation", personnalInformation);
            return "redirect:/order/payment/";
        }
        else {
            return "redirect:/order/";
        }
    }
}
