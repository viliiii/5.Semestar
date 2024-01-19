package hr.fer.oprpp1.hw08.jnotepadpp.defaultModels;

import hr.fer.oprpp1.hw08.jnotepadpp.exceptions.DocumentModelException;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    private List<SingleDocumentModel> documentList;
    private SingleDocumentModel current;
    private List<MultipleDocumentListener> listeners;
    private SingleDocumentListener iconListener;

    private ImageIcon modified;
    private ImageIcon notModified;


    public DefaultMultipleDocumentModel() {
        documentList = new ArrayList<>();
        listeners = new ArrayList<>();
        addChangeListener((e) -> {
            SingleDocumentModel newCurrent = documentList.get(getSelectedIndex());
            notifyListenersCurrentDocumentChanged(current, newCurrent);
            current = newCurrent;

        });
        initIcons();
        iconListener = new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                setIconAt(documentList.indexOf(current), current.isModified() ? modified : notModified);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {

            }
        };


        createNewDocument();
        setIconAt(documentList.indexOf(current), current.isModified() ? modified : notModified);
    }

    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    @Override
    public SingleDocumentModel createNewDocument() {

        SingleDocumentModel newDoc = new DefaultSingleDocumentModel(null, "");
        newDoc.addSingleDocumentListener(iconListener); //!!
        documentList.add(newDoc);
        this.add(newDoc.getTextComponent());
        setSelectedIndex(documentList.indexOf(newDoc));
        notifyListenersCurrentDocumentChanged(current, newDoc);
        current = newDoc;
        setTitleAt(documentList.indexOf(current), "unnamed");
        setToolTipTextAt(documentList.indexOf(current), "unnamed");
        notifyListenersDocumentAdded();
        return newDoc;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return current;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        if(path == null) throw new NullPointerException("Path cannot be null.");
        if(documentList.stream().anyMatch(dm -> {
            if(dm.getFilePath() != null){
                return dm.getFilePath().equals(path);
            }else{
            return false;}
        })) {
            setSelectedIndex(getIndexOfDocument(findForPath(path)));
            notifyListenersCurrentDocumentChanged(current, findForPath(path));
            current = findForPath(path);
            return null;
        }

        try{
            String content = Files.readString(path);
            SingleDocumentModel newDoc = new DefaultSingleDocumentModel(path, content);
            newDoc.addSingleDocumentListener(iconListener);
            documentList.add(newDoc);
            this.add(newDoc.getTextComponent());
            setSelectedIndex(documentList.indexOf(newDoc));
            notifyListenersCurrentDocumentChanged(current, newDoc);
            current = newDoc;
            setTitleAt(documentList.indexOf(current), current.getFilePath().getFileName().toString());
            setToolTipTextAt(documentList.indexOf(current), current.getFilePath().toString());
            notifyListenersDocumentAdded();
            return newDoc;
        }catch(IOException e){
            throw new DocumentModelException("Cannot open file");
        }

    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        if(model == null) throw new NullPointerException("Model cannot be null.");
        if(newPath == null){
            newPath = model.getFilePath();
        }

        try{
            String content = model.getTextComponent().getText();
            Files.write(newPath, content.getBytes());

            model.setFilePath(newPath);
            model.setModified(false);
            setTitleAt(documentList.indexOf(current), current.getFilePath().getFileName().toString());
            setToolTipTextAt(documentList.indexOf(current), current.getFilePath().toString());
            setIconAt(documentList.indexOf(current), current.isModified() ? modified : notModified);

        }catch (IOException e) {
            throw new DocumentModelException("Could not save.");
        }
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        if(current == model){
            current = documentList.getLast();
            setSelectedIndex(documentList.indexOf(current));
        }
        documentList.remove(model);
        this.remove(model.getTextComponent());
        notifyListenersDocumentRemoved(model);
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documentList.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documentList.get(index);
    }

    @Override
    public SingleDocumentModel findForPath(Path path) {
        if(path == null) throw new NullPointerException("Path cannot be null.");

        Optional<SingleDocumentModel> found = documentList.stream()
                .filter(dm -> dm.getFilePath().equals(path))
                .findFirst();

        return found.orElse(null);
    }

    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        return documentList.indexOf(doc);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<SingleDocumentModel> iterator() {

        Iterator<SingleDocumentModel> it = documentList.iterator();
        return new Iterator<SingleDocumentModel>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public SingleDocumentModel next() {
                return it.next();
            }
        };
    }

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Actions are performed in the order of iteration, if that
     * order is specified.  Exceptions thrown by the action are relayed to the
     * caller.
     * <p>
     * The behavior of this method is unspecified if the action performs
     * side effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     * @since 1.8
     */
    @Override
    public void forEach(Consumer<? super SingleDocumentModel> action) {
        for(var dm: documentList){
            action.accept(dm);
        }
    }

    private void notifyListenersCurrentDocumentChanged(SingleDocumentModel previous, SingleDocumentModel current){
        for(var l:listeners){
            l.currentDocumentChanged(previous, current);
        }
    }

    private void notifyListenersDocumentAdded(){
        for(var l:listeners){
            l.documentAdded(current);
        }
    }

    private void notifyListenersDocumentRemoved(SingleDocumentModel model){
        for(var l:listeners){
            l.documentRemoved(model);
        }
    }

    private void initIcons() {
        InputStream is = this.getClass().getResourceAsStream("icons/disketteModified.png");
        InputStream is2 = this.getClass().getResourceAsStream("icons/diskette.png");
        if(is == null || is2 == null) throw new DocumentModelException("Could not load icon.");
        byte[] bytes1;
        byte[] bytes2;
        try {
            bytes1 = is.readAllBytes();
            bytes2 = is2.readAllBytes();
            is.close();
            is2.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        modified = new ImageIcon(bytes1);
        modified = new ImageIcon(modified.getImage().getScaledInstance(10,10, Image.SCALE_SMOOTH));
        notModified = new ImageIcon(bytes2);
        notModified = new ImageIcon(notModified.getImage().getScaledInstance(10,10, Image.SCALE_SMOOTH));
    }


}
