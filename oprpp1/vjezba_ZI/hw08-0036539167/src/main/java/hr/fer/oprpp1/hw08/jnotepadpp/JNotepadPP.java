package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.defaultModels.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.exceptions.DocumentModelException;
import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;
import ispit.zi.zad1.ExamZad01_1;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.crypto.KeySelectorResult;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class JNotepadPP extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private StringBuilder clipBoard;
    private final MultipleDocumentModel model;
    private final List<SingleDocumentListener> singleDocumentListeners;
    private JPanel statusBar;
    private final FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

    private int sel = 0; //length of currently selected text

    public JNotepadPP() {

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             */
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction.actionPerformed(null);
            }
        });
        setLocation(0, 0);
        setSize(800, 600);
        setTitle("(unnamed)" + " - JNotepad++");
        model = new DefaultMultipleDocumentModel();
        model.getCurrentDocument().getTextComponent().addCaretListener(ee->updateStatusBar());
        singleDocumentListeners = new ArrayList<>();
        clipBoard = new StringBuilder();

        //LocalizationProvider.getInstance().addLocalizationListener(this::createActions);
        //flp.addLocalizationListener(this::createActions);
        
        initGUI();
    }

    private void initGUI() {

        this.getContentPane().setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(model.getVisualComponent());
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);


        createActions();
        createMenus();
        createToolbars();
        createStatusBar();
        initMultipleModelListeners();

    }

    private final LocalizableAction openNewDocumentAction = new LocalizableAction("new", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.createNewDocument();
            model.getCurrentDocument().getTextComponent().addCaretListener(ee->updateStatusBar());
        }
    };

    private final LocalizableAction openDocumentAction = new LocalizableAction("open", flp) {

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
                model.getCurrentDocument().getTextComponent().addCaretListener(ee->updateStatusBar());
            }catch (DocumentModelException dme){
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "Pogreška prilikom čitanja datoteke "+fileName.getAbsolutePath()+".",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    };

    private final LocalizableAction saveDocumentAction = new LocalizableAction("save", flp) {

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

    private final LocalizableAction saveAsDocumentAction = new LocalizableAction("saveAs", flp) {

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

    private final LocalizableAction closeDocumentAction = new LocalizableAction("close", flp) {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            model.closeDocument(model.getCurrentDocument());
        }
    };

    private final LocalizableAction cutSelectedPartAction = new LocalizableAction("cut", flp) {
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

    private final LocalizableAction copySelectedPartAction = new LocalizableAction("copy", flp) {
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

    private final LocalizableAction pasteClipboardAction = new LocalizableAction("paste", flp) {
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


    private final LocalizableAction deleteSelectedPartAction = new LocalizableAction("deleteSelectedText", flp) {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            simpleAction((o, l, d) -> {
                d.remove(o, l);
            });
        }
    };

    private final LocalizableAction toggleCaseAction = new LocalizableAction("toggleCase", flp) {

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

    private final LocalizableAction upperCaseAction = new LocalizableAction("upperCase", flp) {

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
                }
            }
            return new String(znakovi);
        }
    };

    private final LocalizableAction lowerCaseAction = new LocalizableAction("lowerCase", flp) {

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
                if(Character.isUpperCase(c)) {
                    znakovi[i] = Character.toLowerCase(c);
                }
            }
            return new String(znakovi);
        }
    };

    private final LocalizableAction ascendingSortAction = new LocalizableAction("ascendingSort", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Locale lan_locale = new Locale(flp.getLanguage());
            Collator lan_collator = Collator.getInstance(lan_locale);

            JTextArea editor = model.getCurrentDocument().getTextComponent();
            Document doc = editor.getDocument();

            int startOffset = editor.getSelectionStart();
            int endOffset = editor.getSelectionEnd();
            if(Math.abs(startOffset - endOffset) == 0) return;

            try {
                int startLine = editor.getLineStartOffset(editor.getLineOfOffset(startOffset));
                int endLine = editor.getLineEndOffset(editor.getLineOfOffset(endOffset));

                String selectedText = doc.getText(startLine, endLine-startLine);
                String[] selectedSplit = selectedText.split("\n");
                Arrays.sort(selectedSplit, lan_collator);
                doc.remove(startLine, endLine - startLine);
                doc.insertString(startLine, String.join("\n", selectedSplit), null);

            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }

        }
    };

    private final LocalizableAction descendingSortAction = new LocalizableAction("descendingSort", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Locale lan_locale = new Locale(flp.getLanguage());
            Collator lan_collator = Collator.getInstance(lan_locale);

            JTextArea editor = model.getCurrentDocument().getTextComponent();
            Document doc = editor.getDocument();

            int startOffset = editor.getSelectionStart();
            int endOffset = editor.getSelectionEnd();
            if(Math.abs(startOffset - endOffset) == 0) return;

            try {
                int startLine = editor.getLineStartOffset(editor.getLineOfOffset(startOffset));
                int endLine = editor.getLineEndOffset(editor.getLineOfOffset(endOffset));

                String selectedText = doc.getText(startLine, endLine-startLine);
                String[] selectedSplit = selectedText.split("\n");
                Arrays.sort(selectedSplit, Collections.reverseOrder(lan_collator));
                doc.remove(startLine, endLine - startLine);
                doc.insertString(startLine, String.join("\n", selectedSplit), null);

            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }

        }
    };

    private final LocalizableAction uniqueAction = new LocalizableAction("unique", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {

            JTextArea editor = model.getCurrentDocument().getTextComponent();
            Document doc = editor.getDocument();

            int startOffset = editor.getSelectionStart();
            int endOffset = editor.getSelectionEnd();
            if(Math.abs(startOffset - endOffset) == 0) return;

            try {
                int startLine = editor.getLineStartOffset(editor.getLineOfOffset(startOffset));
                int endLine = editor.getLineEndOffset(editor.getLineOfOffset(endOffset));

                String selectedText = doc.getText(startLine, endLine-startLine);
                String[] selectedSplit = selectedText.split("\n");
                Set<String> unique = new LinkedHashSet<>(Arrays.asList(selectedSplit));
                doc.remove(startLine, endLine - startLine);
                doc.insertString(startLine, String.join("\n", unique), null);

            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }

        }
    };

    private final LocalizableAction statsAction = new LocalizableAction("stats", flp) {
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
                    String.format(flp.getString("statsStats"), charCount, nonBlankCount, lineCount),
                    (String)this.getValue("NAME"),
                    JOptionPane.INFORMATION_MESSAGE);

        }
    };

    private final LocalizableAction exitAction = new LocalizableAction("exit", flp) {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {

            int rep = model.getNumberOfDocuments();
            for(int i = 0; i <rep; i++) {
                if(model.getDocument(model.getIndexOfDocument(model.getCurrentDocument())).isModified()){
                    int result = JOptionPane.showOptionDialog(
                            JNotepadPP.this,
                            "Do you want to save changes?",
                            "Unsaved Changes",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{"Save", "Don't Save", "Cancel"},
                            "Save"  // Defaultna opcija
                    );

                    if(result == JOptionPane.YES_OPTION){
                        saveDocumentAction.actionPerformed(null);
                        closeDocumentAction.actionPerformed(null);
                    }else if(result == JOptionPane.NO_OPTION){
                        closeDocumentAction.actionPerformed(null);
                    }else if(result == JOptionPane.CANCEL_OPTION){
                        return;
                    }


                }else{
                    closeDocumentAction.actionPerformed(null);
                }
            }

            System.exit(0);
        }
    };

    private void createActions() {
        openDocumentAction.putValue(
                Action.NAME,
                flp.getString("open"));
        openDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control O"));
        openDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_O);
        openDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                flp.getString("openDes"));

        openNewDocumentAction.putValue(Action.NAME, flp.getString("new"));
        openNewDocumentAction.putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control N"));
        openNewDocumentAction.putValue(Action.SHORT_DESCRIPTION,
                flp.getString("newDes"));

        saveAsDocumentAction.putValue(
                Action.NAME,
                flp.getString("saveAs"));
        saveAsDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        saveAsDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_S);
        saveAsDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                flp.getString("saveAsDes"));

        saveDocumentAction.putValue(
                Action.NAME,
                flp.getString("save")
        );
        saveDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control S")
        );
        saveDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                flp.getString("saveDes")
        );

        closeDocumentAction.putValue(Action.NAME, flp.getString("close"));
        closeDocumentAction.putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control E"));
        closeDocumentAction.putValue(Action.SHORT_DESCRIPTION,
                flp.getString("closeDes"));

        deleteSelectedPartAction.putValue(
                Action.NAME,
                flp.getString("deleteSelectedText"));
        deleteSelectedPartAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("F2"));
        deleteSelectedPartAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_D);
        deleteSelectedPartAction.putValue(
                Action.SHORT_DESCRIPTION,
                flp.getString("deleteSelectedTextDes"));

        // Cut action
        cutSelectedPartAction.putValue(Action.NAME, flp.getString("cut"));
        cutSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutSelectedPartAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("cutDes"));

        // Copy action
        copySelectedPartAction.putValue(Action.NAME, flp.getString("copy"));
        copySelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copySelectedPartAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("copyDes"));

        // Paste action
        pasteClipboardAction.putValue(Action.NAME, flp.getString("paste"));
        pasteClipboardAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteClipboardAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("pasteDes"));

        statsAction.putValue(Action.NAME, flp.getString("stats"));
        statsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
        statsAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("statsDes"));


        toggleCaseAction.putValue(
                Action.NAME,
                flp.getString("toggleCase"));
        toggleCaseAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control F3"));
        toggleCaseAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_T);
        toggleCaseAction.putValue(
                Action.SHORT_DESCRIPTION,
                flp.getString("toggleCaseDes"));

        upperCaseAction.putValue(Action.NAME, "upperCase");
        upperCaseAction.putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control U"));
        upperCaseAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("upperCaseDes"));

        lowerCaseAction.putValue(Action.NAME, "lowerCase");
        lowerCaseAction.putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control L"));
        lowerCaseAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("lowerCaseDes"));

        ascendingSortAction.putValue(Action.NAME, flp.getString("ascendingSort"));
        ascendingSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
        ascendingSortAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("ascendingSortDes"));

        descendingSortAction.putValue(Action.NAME, flp.getString("descendingSort"));
        descendingSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
        descendingSortAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descendingSortDes"));

        uniqueAction.putValue(Action.NAME, flp.getString("unique"));
        uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 8"));
        uniqueAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("uniqueDes"));

        exitAction.putValue(
                Action.NAME,
                flp.getString("exit"));
        exitAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control X"));
        exitAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_X);
        exitAction.putValue(
                Action.SHORT_DESCRIPTION,
                flp.getString("exitDes"));
    }

    private void updateStatusBar() {
        JTextArea editor = model.getCurrentDocument().getTextComponent();
        Document doc = editor.getDocument();

        int length = doc.getLength();
        int ln = 1;
        int col = 1;
        sel = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

        int caretPos = editor.getCaretPosition();

            Element root = doc.getDefaultRootElement();

            ln = root.getElementIndex(caretPos);
            col = caretPos - root.getElement(ln).getStartOffset();

            JLabel leftLabel = (JLabel) statusBar.getComponent(0);
            JLabel rightLabel = (JLabel) statusBar.getComponent(1);

            leftLabel.setText(flp.getString("length") + length);
            rightLabel.setText(String.format("Ln: %d  Col: %d  Sel: %d", ln+1, col+1, sel));



    }
    private void createStatusBar(){
        statusBar = new JPanel(new GridLayout(1, 3));
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        JLabel leftLabel = new JLabel(flp.getString("length") + " 0");
        leftLabel.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));
        leftLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel rightLabel = new JLabel("Ln:1  Col:1  Sel:0");
        rightLabel.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));
        rightLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel clockLabel = new JLabel("");

        clockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        Thread clockThread = new Thread(() -> {
            while (true) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String formattedDate = dateFormat.format(new Date());
                SwingUtilities.invokeLater(() -> clockLabel.setText(formattedDate));

                try {
                    // Pauziranje niti svake sekunde
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clockThread.setDaemon(true); // Postavljamo nit kao daemon kako bi se zaustavila zajedno s glavnom niti
        clockThread.start();


        statusBar.add(leftLabel);
        statusBar.add(rightLabel);
        statusBar.add(clockLabel);
    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        LJMenu fileMenu = new LJMenu("file", flp);
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

        LJMenu editMenu = new LJMenu("edit", flp);
        menuBar.add(editMenu);

        editMenu.add(new JMenuItem(deleteSelectedPartAction));
        editMenu.add(new JMenuItem(toggleCaseAction));
        editMenu.add(new JMenuItem(copySelectedPartAction));
        editMenu.add(new JMenuItem(cutSelectedPartAction));
        editMenu.add(new JMenuItem(pasteClipboardAction));

        LJMenu toolsMenu = new LJMenu("tools", flp);
        menuBar.add(toolsMenu);


        LJMenu caseMenu = new LJMenu("changeCase", flp);
        toolsMenu.add(caseMenu);

        JMenuItem toggleCase = new JMenuItem(toggleCaseAction);
        JMenuItem upperCase = new JMenuItem(upperCaseAction);
        JMenuItem lowerCase = new JMenuItem(lowerCaseAction);
        toggleCase.setEnabled(false);
        upperCase.setEnabled(false);
        lowerCase.setEnabled(false);
        caseMenu.add(toggleCase);
        caseMenu.add(upperCase);
        caseMenu.add(lowerCase);


        model.getCurrentDocument().getTextComponent().addCaretListener((e) -> {
            updateStatusBar();
            toggleCase.setEnabled(sel > 0);
            upperCase.setEnabled(sel > 0);
            lowerCase.setEnabled(sel > 0);
        });


        LJMenu sortMenu = new LJMenu("sort", flp);
        toolsMenu.add(sortMenu);

        sortMenu.add(ascendingSortAction);
        sortMenu.add(descendingSortAction);

        toolsMenu.add(uniqueAction);



        LJMenu languagesMenu = new LJMenu("languages", flp);
        JMenuItem en = new JMenuItem("English");
        en.addActionListener((e) -> LocalizationProvider.getInstance().setLanguage("en"));
        languagesMenu.add(en);

        JMenuItem hr = new JMenuItem("Hrvatski");
        hr.addActionListener((e) -> LocalizationProvider.getInstance().setLanguage("hr"));
        languagesMenu.add(hr);

        JMenuItem sl = new JMenuItem("Slovenski");
        sl.addActionListener((e) -> LocalizationProvider.getInstance().setLanguage("sl"));
        languagesMenu.add(sl);
        menuBar.add(languagesMenu);

        JMenu ispitMenu = new JMenu("Ispit");
        ispitMenu.add(new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new ExamZad01_1();
                dialog.setVisible(true);
            }
        }));
        menuBar.add(ispitMenu);




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
                setTitle(currentModel.getFilePath() == null ? "(unnamed) - JNotepad++" : currentModel.getFilePath().toString() + " - JNotepad++");
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
