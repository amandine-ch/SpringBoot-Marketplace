package net.Marketplace.part2.utils;

/**
 * Utility class for validation operations including number conversions, empty value checks, and Luhn algorithm validation for card numbers.
 */
public class ValidationUtils {
    /**
     * Checks if the string can be converted to an integer.
     *
     * @param s The string to check.
     * @return true if the conversion to an integer is successful, otherwise false.
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the string can be converted to a float.
     *
     * @param s The string to check.
     * @return true if the conversion to a float is successful, otherwise false.
     */
    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the parameter values are empty or null.
     *
     * @param nomCarte                Cardholder's name.
     * @param numeroCarte             Card number.
     * @param dateExpiration          Card expiration date.
     * @param codeCarte               Card code.
     * @param personnalInformationObject Object containing personal information.
     * @return true if any of the parameters is empty or null, otherwise false.
     */
    private static boolean checkEmpty(String nomCarte, String numeroCarte, String dateExpiration, String codeCarte, Object personnalInformationObject) {
        if((nomCarte == null || nomCarte.isEmpty()) ||
                (numeroCarte == null || numeroCarte.isEmpty()) ||
                (dateExpiration == null || dateExpiration.isEmpty()) ||
                (codeCarte == null || codeCarte.isEmpty()) ||
                (personnalInformationObject == null)) {
            return true;
        }
        return false;
    }

    /**
     * Checks the validity of the card number using the Luhn algorithm.
     *
     * @param numeroCarte The card number to be checked.
     * @return true if the card is valid according to the Luhn algorithm, otherwise false.
     */
    public static boolean checkLuhn(String numeroCarte) {
        String numeroCarteSansEspace = numeroCarte.replaceAll("\\s+", "");
        int somme = 0;
        boolean pair = false;
        for (int i = numeroCarteSansEspace.length() - 1; i >= 0; i--) {
            int chiffre = Integer.parseInt(numeroCarteSansEspace.substring(i, i + 1));
            if (pair) {
                chiffre *= 2;
                if (chiffre > 9) {
                    chiffre -= 9;
                }
            }
            somme += chiffre;
            pair = !pair;
        }
        return (somme % 10 == 0);
    }

    /**
     * Checks both if the values are not empty and if the card is valid according to the Luhn algorithm.
     *
     * @param nomCarte                Cardholder's name.
     * @param numeroCarte             Card number.
     * @param dateExpiration          Card expiration date.
     * @param codeCarte               Card code.
     * @param personnalInformationObject Object containing personal information.
     * @return true if the values are not empty and the card is valid according to the Luhn algorithm, otherwise false.
     */
    public static boolean checkValues(String nomCarte, String numeroCarte, String dateExpiration, String codeCarte, Object personnalInformationObject) {
        return !checkEmpty(nomCarte, numeroCarte, dateExpiration, codeCarte, personnalInformationObject) && checkLuhn(numeroCarte);
    }
}
