/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.siwoc.navigation.utils.vcfcreator.ui;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author niekk
 */
public class Localizer {

    private static Locale _locale = Locale.getDefault();
    private static String _resourceBundle = "resources/Translations";

    public static void setLocale (Locale locale) {
        _locale = locale;
    }
    
    public static String getLocalizedText(String key) {
        return ResourceBundle.getBundle(_resourceBundle, _locale).getString(key);
    }
    
}
