package hr.fer.oprpp1.hw08.jnotepadpp.defaultModels;

import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultSingleDocumentModel implements SingleDocumentModel {

    private Path filePath;

    private JTextArea textComponent;

    private boolean modified;

    private List<SingleDocumentListener> listeners;

    public DefaultSingleDocumentModel(Path filePath, String content) {
        textComponent = new JTextArea(content);
        modified = false;
        listeners = new ArrayList<>();

        textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }

    @Override
    public JTextArea getTextComponent() {
        return textComponent;
    }

    @Override
    public Path getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(Path path) {
        if(path != this.filePath){
            this.filePath = path;
            notifyListenersDocumentFilePathUpdated();
        }
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        if(modified != this.modified) {
            this.modified = modified;
            notifyListenersDocumentModifyStatusUpdated();
        }
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    private void notifyListenersDocumentModifyStatusUpdated(){
        for(var listener: listeners){
            listener.documentModifyStatusUpdated(this);
        }
    }

    private void notifyListenersDocumentFilePathUpdated(){
        for(var listener: listeners){
            listener.documentFilePathUpdated(this);
        }
    }
}
