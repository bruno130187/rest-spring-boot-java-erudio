package br.com.erudio.restspringbootjavaerudio.converters;

public class NumberConverter {

    public static Double convertToDouble(String num) {
        if (num == null) return 0D;
        String number = num.replaceAll(",", ".");
        if (isNumeric(number)) return Double.parseDouble(number);
        return 0D;
    }

    public static boolean isNumeric(String num) {
        if (num == null) return false;
        String number = num.replaceAll(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

}
