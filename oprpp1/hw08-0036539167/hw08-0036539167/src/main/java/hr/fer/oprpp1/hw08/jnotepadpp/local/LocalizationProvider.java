package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider{

    private static final LocalizationProvider instance = new LocalizationProvider();
    private String language;
    private ResourceBundle bundle;

    private LocalizationProvider() {
        language = "en";
        bundle = ResourceBundle.getBundle("prijevodi", Locale.forLanguageTag(language));

    }

    public static LocalizationProvider getInstance() {
        return instance;
    }


    public void setLanguage(String language) {
        this.language = language;
        bundle = ResourceBundle.getBundle("prijevodi", Locale.forLanguageTag(language));
        fire();
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getLanguage() {
        return language;
    }


}
