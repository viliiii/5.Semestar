package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.Serial;

public abstract class LocalizableAction extends AbstractAction {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String key;
    private final ILocalizationProvider provider;

    public LocalizableAction(String keyy, ILocalizationProvider lp){
        this.key = keyy;
        this.provider = lp;
        putValue(NAME, lp.getString(key));
        provider.addLocalizationListener(new ILocalizationListener() {
            @Override
            public void localizationChanged() {
                putValue(NAME, provider.getString(key));
                putValue(SHORT_DESCRIPTION, provider.getString(key + "Des"));
            }
        });
    }

}
