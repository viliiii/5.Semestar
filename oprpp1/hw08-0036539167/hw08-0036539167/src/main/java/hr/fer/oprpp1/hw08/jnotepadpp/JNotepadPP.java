package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.defaultModels.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.exceptions.DocumentModelException;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JNotepadPP extends JFrame {
    private static final long serialVersionUID = 1L;


    private MultipleDocumentModel model;

    public JNotepadPP() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(0, 0);
        setSize(600, 600);
        model = new DefaultMultipleDocumentModel();

        initGUI();
    }

    private void initGUI() {

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(model.getVisualComponent(), BorderLayout.CENTER);

        createActions();
        createMenus();
        createToolbars();

    }

    private Action openDocumentAction = new AbstractAction() {

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

            /*if(!Files.isReadable(filePath)) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "Datoteka "+fileName.getAbsolutePath()+" ne postoji!",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            byte[] okteti;
            try {
                okteti = Files.readAllBytes(filePath);
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(
                        JNotepad.this,
                        "Pogreška prilikom čitanja datoteke "+fileName.getAbsolutePath()+".",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String tekst = new String(okteti, StandardCharsets.UTF_8);
            editor.setText(tekst);
            openedFilePath = filePath;*/
        }
    };

    private Action saveDocumentAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if(model.getCurrentDocument().getFilePath()==null) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Save document");
                if(jfc.showSaveDialog(JNotepad.this)!=JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(
                            JNotepad.this,
                            "Ništa nije snimljeno.",
                            "Upozorenje",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                openedFilePath = jfc.getSelectedFile().toPath();
            }
            byte[] podatci = editor.getText().getBytes(StandardCharsets.UTF_8);
            try {
                Files.write(openedFilePath, podatci);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(
                        JNotepad.this,
                        "Pogreška prilikom zapisivanja datoteke "+openedFilePath.toFile().getAbsolutePath()+".\nPažnja: nije jasno u kojem je stanju datoteka na disku!",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(
                    JNotepad.this,
                    "Datoteka je snimljena.",
                    "Informacija",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    private Action deleteSelectedPartAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
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

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
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

        saveDocumentAction.putValue(
                Action.NAME,
                "Save");
        saveDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control S"));
        saveDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_S);
        saveDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to save current file to disk.");

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

        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAction));
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

        toolBar.add(new JButton(openDocumentAction));
        toolBar.add(new JButton(saveDocumentAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(deleteSelectedPartAction));
        toolBar.add(new JButton(toggleCaseAction));

        this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new JNotepad().setVisible(true);
            }
        });
    }
}
