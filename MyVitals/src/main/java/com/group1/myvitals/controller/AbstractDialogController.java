package com.group1.myvitals.controller;

import javafx.fxml.FXML;

/**
 * Base class for modal dialog controllers that perform an action and
 * notify a caller via a callback when done.
 */
public abstract class AbstractDialogController {

    private Runnable onComplete;

    public void setOnComplete(Runnable callback) {
        this.onComplete = callback;
    }

    /** Runs the callback then closes the window. Call at the end of handleAction(). */
    protected void complete() {
        if (onComplete != null) onComplete.run();
        closeWindow();
    }

    @FXML
    protected void handleExit() {
        closeWindow();
    }

    @FXML
    protected abstract void handleAction();

    protected abstract void closeWindow();
}
