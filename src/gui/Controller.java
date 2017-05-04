package gui;

import gui.mainPage.MainPage;
import javafx.scene.layout.Pane;

/**
 * Created by vasco on 04-05-2017.
 */
public interface Controller<E extends Enum<E>> {
    void disableCurrState();
    void setNewState (E newState);
    void setPane(Pane pane, boolean arg);

}
