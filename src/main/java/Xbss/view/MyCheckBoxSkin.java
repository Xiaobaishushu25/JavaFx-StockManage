package Xbss.view;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-29-11:15
 * @describe ：仿照原生CheckBoxSkin改的，这个skin不显示原生的左边那个小方框
 */
public class MyCheckBoxSkin extends LabeledSkinBase<CheckBox, ButtonBehavior<CheckBox>> {
    private final StackPane box = new StackPane();
    private StackPane innerbox;

    public MyCheckBoxSkin(CheckBox checkbox) {
        super(checkbox, new ButtonBehavior<CheckBox>(checkbox));

        box.getStyleClass().setAll("box");
        innerbox = new StackPane();
        innerbox.getStyleClass().setAll("mark");
        innerbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        box.getChildren().add(innerbox);
        updateChildren();
    }
}
