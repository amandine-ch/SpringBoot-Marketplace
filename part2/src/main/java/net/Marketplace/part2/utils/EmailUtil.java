package net.Marketplace.part2.utils;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import net.Marketplace.part2.entity.Articles;
import net.Marketplace.part2.entity.Users;

/**
 * Utility class for sending emails.
 */
public class EmailUtil {

    /**
     * Utility method to send simple HTML email
     * @param session   - the session to use
     * @param toEmail   - the email address to send to
     * @param subject   - the subject of the email
     * @param body      - the body of the email
     */
    public static void sendEmail(Session session, String toEmail, String subject, String body){
        try
        {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("marketplace.root@gmail.com", "Marketplace"));

            msg.setReplyTo(InternetAddress.parse(toEmail, false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Private methods for constructing the email body

    /**
     * Constructs a string summarizing the cart items.
     *
     * @param cart           The shopping cart.
     * @param total          The total purchase amount.
     * @param loyaltyPoints  The loyalty points used.
     * @return A string representing a cart summary.
     */
    private static String cartRecapString(Cart cart, float total, int loyaltyPoints) {
        String cartRecap = "";
        cartRecap += "Récapitulatif du panier :\n";
        cartRecap += "Article | Quantité | Prix unitaire\n";
        double prixTotal = 0;

        for(CartItem item : cart.getCartItems()) {
            Articles currentArticle = item.getArticle();
            int articleQuantite = item.getQuantity();

            cartRecap += currentArticle.getName() + " | " + articleQuantite + " | " + currentArticle.getPrice() + "\n";

            prixTotal += currentArticle.getPrice() * articleQuantite;
        }

        String formatedTotal = String.format("%.2f", total);

        cartRecap += "\nPrix total : " + formatedTotal + "€";
        cartRecap += "\nPoints de fidélité utilisés : " + loyaltyPoints;
        cartRecap += "\nNouveaux total : " + total + "€";

        return cartRecap;
    }

    /**
     * Constructs a string representing the shipping information.
     *
     * @param personnalInformation The shipping information.
     * @return A string representing the shipping information.
     */
    private static String shippingInformation(Map<String, String> personnalInformation) {
        String shippingInformationString = "";
        shippingInformationString += "Informations de livraison :\n";
        shippingInformationString += "Nom complet : " + personnalInformation.get("nomComplet") + "\n";
        shippingInformationString += "Adresse de livraison : " + personnalInformation.get("adresse") + "\n";
        shippingInformationString += "Numéro de téléphone : " + personnalInformation.get("telephone") + "\n";

        return shippingInformationString;
    }
    /**
     * Constructs the email body to be sent.
     *
     * @param user              The user involved.
     * @param cart              The shopping cart.
     * @param personnalInformation The shipping information.
     * @param total             The total purchase amount.
     * @param loyaltyPoints     The loyalty points used.
     * @return The email body.
     */
    private static String bodyEmail(Users user, Cart cart, Map<String, String> personnalInformation, float total, int loyaltyPoints) {
        String body = "Bonjour " + user.getName() + ",\n\n" +
                "Nous vous remercions pour votre achat. Voici un récapitulatif de celui-ci :\n" +
                "\n" +
                shippingInformation(personnalInformation) +
                "\n" +
                cartRecapString(cart, total, loyaltyPoints) + "\n\n" +
                "Cordialement,\n" +
                "L'équipe de WA'ER";

        return body;
    }
    /**
     * Sends a recap email after a purchase.
     *
     * @param user              The user involved.
     * @param cart              The shopping cart.
     * @param personnalInformation The shipping information.
     * @param total             The total purchase amount.
     * @param loyaltyPoints     The loyalty points used.
     */
    public static void sendRecapMail(Users user, Cart cart, Map<String, String> personnalInformation, float total, int loyaltyPoints) {
        final String from = "marketplace.root@gmail.com";
        final String pw = "aibygnesrjnpgnbj";
        final String host = "smtp.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pw);
            }
        };

        Session session = Session.getDefaultInstance(properties, auth);
        EmailUtil.sendEmail(session, user.getEmail(), "Récapitulatif de paiement", bodyEmail(user, cart, personnalInformation, total, loyaltyPoints));
    }
}
