package gui;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;

public interface Controller<E extends Enum<E>> {

    /**
     * Disable current state.
     */
    void disableCurrState();

    /**
     * Set new state.
     * @param newState New state.
     */
    void setNewState (E newState);

    /**
     * Set pane.
     * @param pane panel.
     * @param arg boolean.
     */
    void setPane(Pane pane, boolean arg);

    /**
     * Get panel transition.
     * @param pane Panel.
     * @param show Show.
     * @return TranslateTransition.
     */
    TranslateTransition getPaneTransition(Pane pane, boolean show);
}
