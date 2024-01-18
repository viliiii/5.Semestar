package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.defaultModels.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.exceptions.DocumentModelException;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JNotepadPP extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;


    private MultipleDocumentModel model;
    private List<SingleDocumentListener> singleDocumentListeners;

    public JNotepadPP() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(0, 0);
        setSize(600, 600);
        model = new DefaultMultipleDocumentModel();
        singleDocumentListeners = new ArrayList<>();

        initGUI();
    }

    private void initGUI() {

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(model.getVisualComponent(), BorderLayout.CENTER);

        createActions();
        createMenus();
        createToolbars();
        initMultipleModelListeners();

    }

    private Action openNewDocumentAction = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            model.createNewDocument();
        }
    };

    private Action openDocumentAction = new AbstractAction() {

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

    private Action saveDocumentAction = new AbstractAction() {

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

    private Action saveAsDocumentAction = new AbstractAction() {

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
                model.getCurrentDocument().setFilePath(jfc.getSelectedFile().toPath());

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

    private Action deleteSelectedPartAction = new AbstractAction() {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {

            JTextArea editor = model.getCurrentDocument().getTextComponent();



            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
            if(len==0) return;
            int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
            try {
                doc.remove(offset, len);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    };

    private Action toggleCaseAction = new AbstractAction() {

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

    private Action exitAction = new AbstractAction() {

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
                KeyStroke.getKeyStroke("control n"));
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
                KeyStroke.getKeyStroke("control s")
        );
        saveDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to save current file to disk."
        );

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
        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {

            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                saveItem.setEnabled(false);
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        });
        singleDocumentListeners.add(saveItemListener);
        saveItem.setEnabled(false);
        fileMenu.add(saveItem);

        fileMenu.add(new JMenuItem(saveAsDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        editMenu.add(new JMenuItem(deleteSelectedPartAction));
        editMenu.add(new JMenuItem(toggleCaseAction));

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
        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {

            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                saveButton.setEnabled(false);
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        });
        singleDocumentListeners.add(saveButtonListener);
        toolBar.add(saveButton);
        toolBar.add(new JButton(saveAsDocumentAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(deleteSelectedPartAction));
        toolBar.add(new JButton(toggleCaseAction));

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

    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new JNotepadPP().setVisible(true);
            }
        });
    }
}
