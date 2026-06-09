package com.group1.myvitals.controller;

/**
 * Implemented by controllers loaded into the MainMenu centre pane
 * that need a reference back to MainMenuController for navigation.
 * Pattern: Observer (lightweight — parent notifies child of context)
 */
public interface MainMenuAware {
    void setMainMenuController(MainMenuController mmc);
}
