package Xbss.Utils;


import javafx.application.Application;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-06-10-11:55
 * @descirbe :返回一个Id cell,可以直接加在tableview上
 */
public class IndexCell extends Application {
    public static TableColumn getIndexCell(){
        TableColumn<Object, String> id = new TableColumn<>("Id");
        id.setCellFactory((col) -> {
            TableCell<Object, String> cell = new TableCell<Object, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        int rowIndex = this.getIndex() + 1;
                        this.setText(String.valueOf(rowIndex));
                    }
                }
            };
            return cell;
        });
        return id;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
//        ObservableList<User> observableList = FXCollections.observableArrayList();
//        List<User> userList = ReadUserExample.getUser();
//        observableList.addAll(userList);
//
//        TableView<User> tableView = new TableView<>(observableList);
//        TableColumn<Student, Number> id = new TableColumn<>("id");
//        id.setCellValueFactory(param -> {
//            SimpleIntegerProperty integerProperty = new SimpleIntegerProperty(param.getValue().getId());
//            return integerProperty;
//        });
//        TableColumn<User, String> username = new TableColumn<>("用户名");
//        username.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
//                SimpleStringProperty username = new SimpleStringProperty(param.getValue().getUsername());
//                return username;
//            }
//        });
//        TableColumn<User, String> pass = new TableColumn<>("密码");
//        pass.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
//                SimpleStringProperty pass = new SimpleStringProperty(param.getValue().getPass());
//                return pass;
//            }
//        });
//        TableColumn id = IndexCell.getIndexCell();
//        tableView.getColumns().addAll(id,username,pass);
//        tableView.setPrefHeight(300);
//        tableView.setPrefWidth(400);
//        double width = tableView.getPrefWidth()/tableView.getColumns().size();
//        id.setPrefWidth(width-20);
//        username.setPrefWidth(width);
//        pass.setPrefWidth(width);
//
//        HBox box = new HBox();
//        box.setPadding(new Insets(50,50,50,50));
//        box.setAlignment(Pos.BOTTOM_CENTER);
//        box.getChildren().add(tableView);
//
//        Scene scene = new Scene(box);
//
//        primaryStage.setScene(scene);
//        primaryStage.setWidth(500);
//        primaryStage.setHeight(300);
//        primaryStage.setTitle("用户信息");
//        primaryStage.show();
    }
}
