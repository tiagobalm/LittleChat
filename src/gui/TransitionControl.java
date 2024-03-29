package gui;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;


public class TransitionControl {
    /**
     * Show transition.
     * @param pane Panel.
     * @param show Show.
     * @param tt TranslateTransition.
     */
    public static void showTransition(Pane pane, boolean show, TranslateTransition tt) {
        if( show ) {
            pane.setDisable(false);
            pane.setVisible(true);
            tt.play();
        } else {
            tt.setOnFinished(e -> {
                tt.getNode().setVisible(false);
                tt.getNode().setDisable(true);
            });
            tt.play();
        }
    }
}
