package Xbss.view.UIFun;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-10-25-23:44
 * @describe 空窗口
 */
public class EmptyStage extends Stage{
    {
        // 全透明
        this.setOpacity(0);
        // 无任务栏图标
        this.initStyle(StageStyle.UTILITY);
        Scene scene = new Scene(new StackPane());
//           scene.getStylesheets().addAll(CFFXUtils.getStylesheets());//加载css
        this.setScene(scene);
        this.setWidth(0);
        this.setHeight(0);
    }
}
