package hr.fer.oprpp1.hw08.jnotepadpp.listeners;

import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class DefaultSingleDocumentListener implements SingleDocumentListener {

    @Override
    public void documentModifyStatusUpdated(SingleDocumentModel model) {

    }

    @Override
    public void documentFilePathUpdated(SingleDocumentModel model) {
        //tu bi sad trebalo od cijelog notepada enableat save button.
    }
}
