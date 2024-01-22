package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    private final List<ILocalizationListener> listeners;

    public AbstractLocalizationProvider() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    public void fire(){
        for(var l: listeners) l.localizationChanged();
    }
}
