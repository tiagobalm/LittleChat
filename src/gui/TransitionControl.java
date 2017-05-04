package gui;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;

/**
 * Created by vasco on 04-05-2017.
 */
public class TransitionControl {
    public static void showTransition(Pane pane, boolean show, TranslateTransition tt) {
        if( show ) {
            pane.setDisable(false);
            pane.setVisible(true);
            TranslateTransition t = tt;
            tt.play();
        } else {
            TranslateTransition t = tt;
            tt.setOnFinished(e -> {
                tt.getNode().setVisible(false);
                tt.getNode().setDisable(true);
            });
            tt.play();
        }
    }
}
