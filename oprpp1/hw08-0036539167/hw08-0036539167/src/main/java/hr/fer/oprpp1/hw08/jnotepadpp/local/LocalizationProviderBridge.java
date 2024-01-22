package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Objects;

public class LocalizationProviderBridge extends AbstractLocalizationProvider{

    private ILocalizationProvider parent;
    private ILocalizationListener listener;
    private boolean connected;
    private String currentLanguage;

    public LocalizationProviderBridge(ILocalizationProvider parent){

        currentLanguage = parent.getLanguage();
        listener = new ILocalizationListener() {
            @Override
            public void localizationChanged() {
                if(!parent.getLanguage().equals(currentLanguage)){
                    currentLanguage = parent.getLanguage();
                    fire();
                }
            }
        };

        //parent.addLocalizationListener(listener); mozda treba
        this.parent = parent;
        connected = false;
        currentLanguage = parent.getLanguage();
    }

    public void connect(){

        if(!connected){
            parent.addLocalizationListener(listener);
            connected = true;
            if(!Objects.equals(parent.getLanguage(), currentLanguage)){
                currentLanguage = parent.getLanguage();
                fire();
            }
        }
    }

    public void disconnect(){
        if(connected){
            parent.removeLocalizationListener(listener);
            connected = false;
        }
    }


    @Override
    public String getString(String key) {
        return parent.getString(key);
    }

    @Override
    public String getLanguage() {
        return currentLanguage;
    }
}
