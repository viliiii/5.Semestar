package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.defaultModels.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.exceptions.DocumentModelException;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JNotepadPP extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    private StringBuilder clipBoard;
    private final MultipleDocumentModel model;
    private final List<SingleDocumentListener> singleDocumentListeners;


    public JNotepadPP() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(0, 0);
        setSize(800, 600);
        model = new DefaultMultipleDocumentModel();
        singleDocumentListeners = new ArrayList<>();
        clipBoard = new StringBuilder();
        initGUI();
    }

    private void initGUI() {

        this.getContentPane().setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(model.getVisualComponent());
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);

        createActions();
        createMenus();
        createToolbars();
        initMultipleModelListeners();

    }

    private final Action openNewDocumentAction = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            model.createNewDocument();
        }
    };

    private final Action openDocumentAction = new AbstractAction() {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open file");
            if(fc.showOpenDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
                return;
            }
            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();

            try {
                model.loadDocument(filePath);
            }catch (DocumentModelException dme){
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "Pogreška prilikom čitanja datoteke "+fileName.getAbsolutePath()+".",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    };

    private final Action saveDocumentAction = new AbstractAction() {

        @Serial
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {

            Path newPath = null;

            if(model.getCurrentDocument().getFilePath()==null) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Save document");
                if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(
                            JNotepadPP.this,
                            "Ništa nije snimljeno.",
                            "Upozorenje",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                newPath = jfc.getSelectedFile().toPath();

            }


            try{
                model.saveDocument(model.getCurrentDocument(), newPath);
            } catch (DocumentModelException mde) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "Pogreška prilikom zapisivanja datoteke "+model.getCurrentDocument().getFilePath().toFile().getAbsolutePath()+".\nPažnja: nije jasno u kojem je stanju datoteka na disku!",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(
                    JNotepadPP.this,
                    "Datoteka je snimljena.",
                    "Informacija",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    private final Action saveAsDocumentAction = new AbstractAction() {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {

                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Save document");
                if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(
                            JNotepadPP.this,
                            "Ništa nije snimljeno.",
                            "Upozorenje",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                File selectedFile = jfc.getSelectedFile();

                if(selectedFile.exists()){
                    int result = JOptionPane.showConfirmDialog(JNotepadPP.this,
                            "Datoteka već postoji, želite li je zamijeniti?",
                            "Upozorenje",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if(result != JOptionPane.YES_OPTION) return;
                }

                model.getCurrentDocument().setFilePath(selectedFile.toPath());

            try {
                model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath());
            } catch (DocumentModelException mde) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "Pogreška prilikom zapisivanja datoteke "+model.getCurrentDocument().getFilePath().toFile().getAbsolutePath()+".\nPažnja: nije jasno u kojem je stanju datoteka na disku!",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(
                    JNotepadPP.this,
                    "Datoteka je snimljena.",
                    "Informacija",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    private final Action closeDocumentAction = new AbstractAction() {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            model.closeDocument(model.getCurrentDocument());
        }
    };

    private final Action cutSelectedPartAction = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            simpleAction((o, l, d)->{
                clipBoard.setLength(0);
                clipBoard.append(d.getText(o,l));
                d.remove(o, l);
            });
        }
    };

    private final Action copySelectedPartAction = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            simpleAction((o, l, d) -> {
                clipBoard.setLength(0);
                clipBoard.append(d.getText(o,l));
            });
        }
    };

    private final Action pasteClipboardAction = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            simpleAction((o, l, d) -> {
                JTextArea editor = model.getCurrentDocument().getTextComponent();
                d.insertString(editor.getCaret().getMark(), clipBoard.toString(), null);
            });
        }
    };


    private final Action deleteSelectedPartAction = new AbstractAction() {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            simpleAction((o, l, d) -> {
                d.remove(o, l);
            });
        }
    };

    private final Action toggleCaseAction = new AbstractAction() {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
            int offset = 0;
            if(len!=0) {
                offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
            } else {
                len = doc.getLength();
            }
            try {
                String text = doc.getText(offset, len);
                text = changeCase(text);
                doc.remove(offset, len);
                doc.insertString(offset, text, null);
            } catch(BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        private String changeCase(String text) {
            char[] znakovi = text.toCharArray();
            for(int i = 0; i < znakovi.length; i++) {
                char c = znakovi[i];
                if(Character.isLowerCase(c)) {
                    znakovi[i] = Character.toUpperCase(c);
                } else if(Character.isUpperCase(c)) {
                    znakovi[i] = Character.toLowerCase(c);
                }
            }
            return new String(znakovi);
        }
    };

    private final Action statsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int charCount = 0;
            int nonBlankCount = 0;
            int lineCount = 0;

            Document doc = model.getCurrentDocument().getTextComponent().getDocument();

            charCount = doc.getLength();

            try{
                String content = doc.getText(0, charCount);
                for(char c: content.toCharArray()) {
                    if(!Character.isWhitespace(c)) nonBlankCount++;
                }

                lineCount = content.split("\n").length;
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(
                    JNotepadPP.this,
                    String.format("Your document has %d" +
                            "characters, %d non-blank characters and %d lines.", charCount, nonBlankCount, lineCount),
                    (String)this.getValue("NAME"),
                    JOptionPane.INFORMATION_MESSAGE);

        }
    };

    private final Action exitAction = new AbstractAction() {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    private void createActions() {
        openDocumentAction.putValue(
                Action.NAME,
                "Open");
        openDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control O"));
        openDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_O);
        openDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to open existing file from disk.");

        openNewDocumentAction.putValue(Action.NAME, "New");
        openNewDocumentAction.putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control N"));
        openNewDocumentAction.putValue(Action.SHORT_DESCRIPTION,
                "Creates new blank document.");

        saveAsDocumentAction.putValue(
                Action.NAME,
                "SaveAs");
        saveAsDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        saveAsDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_S);
        saveAsDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to save current file to disk with specified new name.");

        saveDocumentAction.putValue(
                Action.NAME,
                "Save"
        );
        saveDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control S")
        );
        saveDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to save current file to disk."
        );

        closeDocumentAction.putValue(Action.NAME, "Close");
        closeDocumentAction.putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control E"));
        closeDocumentAction.putValue(Action.SHORT_DESCRIPTION,
                "Closes the current document.");

        deleteSelectedPartAction.putValue(
                Action.NAME,
                "Delete selected text");
        deleteSelectedPartAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("F2"));
        deleteSelectedPartAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_D);
        deleteSelectedPartAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to delete the selected part of text.");

        // Cut action
        cutSelectedPartAction.putValue(Action.NAME, "Cut");
        cutSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutSelectedPartAction.putValue(Action.SHORT_DESCRIPTION, "Cut selected text to clipboard");

        // Copy action
        copySelectedPartAction.putValue(Action.NAME, "Copy");
        copySelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copySelectedPartAction.putValue(Action.SHORT_DESCRIPTION, "Copy selected text to clipboard");

        // Paste action
        pasteClipboardAction.putValue(Action.NAME, "Paste");
        pasteClipboardAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteClipboardAction.putValue(Action.SHORT_DESCRIPTION, "Paste text from clipboard");

        statsAction.putValue(Action.NAME, "Stats");
        statsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
        statsAction.putValue(Action.SHORT_DESCRIPTION, "Information about document.");


        toggleCaseAction.putValue(
                Action.NAME,
                "Toggle case");
        toggleCaseAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control F3"));
        toggleCaseAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_T);
        toggleCaseAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to toggle character case in selected part of text or in entire document.");


        exitAction.putValue(
                Action.NAME,
                "Exit");
        exitAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control X"));
        exitAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_X);
        exitAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Exit application.");
    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(openNewDocumentAction));
        fileMenu.add(new JMenuItem(openDocumentAction));
        JMenuItem saveItem = new JMenuItem(saveDocumentAction);
        SingleDocumentListener saveItemListener = new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                saveItem.setEnabled(model.isModified());
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {

            }
        };
        model.getCurrentDocument().addSingleDocumentListener(saveItemListener);

        singleDocumentListeners.add(saveItemListener);
        saveItem.setEnabled(false);
        fileMenu.add(saveItem);

        fileMenu.add(new JMenuItem(saveAsDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(closeDocumentAction));
        fileMenu.add(new JMenuItem(exitAction));
        fileMenu.add(new JMenuItem(statsAction));

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        editMenu.add(new JMenuItem(deleteSelectedPartAction));
        editMenu.add(new JMenuItem(toggleCaseAction));
        editMenu.add(new JMenuItem(copySelectedPartAction));
        editMenu.add(new JMenuItem(cutSelectedPartAction));
        editMenu.add(new JMenuItem(pasteClipboardAction));

        this.setJMenuBar(menuBar);
    }

    private void createToolbars() {
        JToolBar toolBar = new JToolBar("Alati");
        toolBar.setFloatable(true);

        toolBar.add(new JButton(openNewDocumentAction));
        toolBar.add(new JButton(openDocumentAction));
        JButton saveButton = new JButton(saveDocumentAction);
        saveButton.setEnabled(false);
        SingleDocumentListener saveButtonListener = new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                saveButton.setEnabled(model.isModified());
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {

            }
        };
        model.getCurrentDocument().addSingleDocumentListener(saveButtonListener);

        singleDocumentListeners.add(saveButtonListener);
        toolBar.add(saveButton);
        toolBar.add(new JButton(saveAsDocumentAction));
        toolBar.add(new JButton(closeDocumentAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(deleteSelectedPartAction));
        toolBar.add(new JButton(toggleCaseAction));
        toolBar.add(new JButton(copySelectedPartAction));
        toolBar.add(new JButton(cutSelectedPartAction));
        toolBar.add(new JButton(pasteClipboardAction));
        toolBar.add(new JButton(statsAction));

        this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    private void initMultipleModelListeners() {
        MultipleDocumentListener mainListener = new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                for(var l: singleDocumentListeners){
                    previousModel.removeSingleDocumentListener(l);
                    currentModel.addSingleDocumentListener(l);
                }
                currentModel.setModified(currentModel.isModified());
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {

            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        };

        model.addMultipleDocumentListener(mainListener);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new JNotepadPP().setVisible(true);
            }
        });
    }

    private void simpleAction(SimpleDocumentAction action){
        JTextArea editor = model.getCurrentDocument().getTextComponent();
        Document doc = editor.getDocument();
        int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
        //if(len==0) return;
        int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
        try {
            action.performAction(offset, len, doc);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    interface SimpleDocumentAction {
        void performAction(int offset, int len, Document doc) throws BadLocationException;
    }


}
