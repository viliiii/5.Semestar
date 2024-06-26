package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.io.Serial;

public class LJMenu extends JMenu {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String key;
    private final ILocalizationProvider provider;

    public LJMenu(String keyy, ILocalizationProvider lp){
        this.key = keyy;
        this.provider = lp;
        updateLabel();
        provider.addLocalizationListener(new ILocalizationListener() {
            @Override
            public void localizationChanged() {
                updateLabel();
            }
        });
    }

    private void updateLabel(){
        setText(provider.getString(key));
    }
}
