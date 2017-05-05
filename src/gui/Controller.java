package gui;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;

public interface Controller<E extends Enum<E>> {
    void disableCurrState();
    void setNewState (E newState);
    void setPane(Pane pane, boolean arg);
    TranslateTransition getPaneTransition(Pane pane, boolean show);
}
