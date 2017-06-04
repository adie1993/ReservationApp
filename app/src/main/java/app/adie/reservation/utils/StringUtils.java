package app.adie.reservation.utils;

import android.util.Patterns;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {
    public static String toRupiahFormat(String nominal) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        double value = Double.valueOf(nominal).doubleValue();
        String[] split = formatter.format(value).replace(",", "#").replace(".", ",").replace("#", ".").split(",");
        return value < 0.0d ? split[0].replace("$", "Rp. -") : split[0].replace("$", "Rp. ");
    }

    public static String toRupiahFormat(int nominal) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        double value = Double.valueOf((double) nominal).doubleValue();
        String[] split = formatter.format(value).replace(",", "#").replace(".", ",").replace("#", ".").split(",");
        return value < 0.0d ? split[0].replace("$", "Rp. -") : split[0].replace("$", "Rp. ");
    }

    public boolean validateText(String text) {
        return !text.isEmpty();
    }

    public static boolean validateText(EditText text) {
        return !str(text).isEmpty();
    }

    public boolean validateEmail(String text) {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    public static boolean validateEmail(EditText text) {
        return Patterns.EMAIL_ADDRESS.matcher(str(text)).matches();
    }

    public static String str(EditText text) {
        return text.getText().toString();
    }
}
