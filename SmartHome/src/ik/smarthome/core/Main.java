package ter.lab.project;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import ter.lab.components.Component;
import ter.lab.factory.*;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main extends Application {
    private ObservableList<Component> list = FXCollections.observableArrayList();
    private Component currElem = null;
    private Factory factoryComp = null;
    Label lTime = new Label("hh:mm");

    @Override
    public void start(Stage primaryStage){
        TableView<Component> table = new TableView<>();
        TableColumn<Component, String> componentNameCol = new TableColumn<>("Название");
        componentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(componentNameCol);
        table.prefWidthProperty().bind(table.widthProperty());
        table.setItems(list);

        //
        // Header
        //
        BorderPane pHeader = new BorderPane();
        pHeader.setLeft(new Label("Список компонентов Умного Дома:"));
        pHeader.setRight(lTime);

        //
        // Column with a list of components of the Smart House
        //
        final VBox vbInfo = new VBox(10);
        table.getSelectionModel().getSelectedItems().addListener((ListChangeListener) c -> {
            vbInfo.getChildren().clear();
            if(!list.isEmpty()){
                currElem = (Component) c.getList().get(0);
                vbInfo.getChildren().addAll(currElem.getMenu());
            }
        });
        table.getSelectionModel().select(0);
        table.disableProperty().bind(Bindings.size(list).isEqualTo(0));

        vbInfo.setAlignment(Pos.TOP_LEFT);
        vbInfo.disableProperty().bind(Bindings.size(list).isEqualTo(0));

        TextField tfCompName = new TextField ();
        HBox hbCompName = new HBox(10);
        hbCompName.getChildren().addAll(new Label("Название:\t"), tfCompName);

        ComboBox cbCompType = new ComboBox();
        cbCompType.getItems().addAll(
                Freezer.class.getName(),
                HeatingFloor.class.getName(),
                AirConditioning.class.getName(),
                MicrowaveOven.class.getName(),
                ShowerRoom.class.getName()
        );
        cbCompType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)->{
            try {
                factoryComp = (Factory)Class.forName((String)cbCompType.getValue()).getConstructor().newInstance();
                tfCompName.setText(factoryComp.getCompName());
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e1) {
                e1.printStackTrace();
            }
        });

        HBox hbCompType = new HBox(10);
        hbCompType.getChildren().addAll(new Label("Тип:\t"), cbCompType);

        Button bCompAdd = new Button("Добавить компонент");
        bCompAdd.setOnAction(e -> {
            try {
                // check whether the device type is selected
                if(Objects.equals(cbCompType.getValue(),null)){
                    throw new ExceptionInInitializerError("Выберете тип устройства!");
                }
                // checking that component already exists
                if (Objects.equals(tfCompName.getText(), "")) {
                    throw new ExceptionInInitializerError("Введите имя компонента!");
                }
                for (Component object : list) {
                    if (Objects.equals(object.getName(), tfCompName.getText())) {
                        throw new ExceptionInInitializerError("Компонент с таким именем уже существет!");
                    }
                }

                list.add(factoryComp.createComponent(tfCompName.getText()));
                tfCompName.clear();
                table.getSelectionModel().select(list.size()-1);
                cbCompType.getSelectionModel().selectLast();
                cbCompType.getSelectionModel().selectFirst();
            } catch (ExceptionInInitializerError el){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информационный диалог");
                alert.setHeaderText("Предупреждение!");
                alert.setContentText(el.getMessage());
                alert.showAndWait();
            }
        });
        Button bCompDel = new Button("Удалить компонент");
        bCompDel.setOnAction(e -> {
            if(!Objects.equals(currElem,null)){
                list.remove(currElem);
                if(list.size() > 0){
                    table.getSelectionModel().select(0);
                }
            }
        });
        bCompDel.disableProperty().bind(Bindings.size(list).isEqualTo(0));

        VBox vbList = new VBox(10,
                table,
                hbCompType,
                hbCompName,
                bCompAdd,
                bCompDel
        );

        //
        // Window content composition
        //
        StackPane root = new StackPane();
        root.setPadding(new Insets(10));

        VBox vbMain = new VBox(10);
        HBox hbWork = new HBox(10);

        hbWork.getChildren().add(vbList);
        hbWork.getChildren().add(vbInfo);

        vbMain.getChildren().add(pHeader);
        vbMain.getChildren().add(hbWork);
        root.getChildren().add(vbMain);

        primaryStage.setTitle("Buniaiev SmartHouse");
        primaryStage.minWidthProperty().setValue(700);
        primaryStage.minHeightProperty().setValue(400);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        //
        // Simulation of the system in real time
        //
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Date date = new Date();
            list.forEach((elem) -> {
                if(elem.work(date) && elem==currElem){
                    vbInfo.getChildren().clear();
                    vbInfo.getChildren().addAll(currElem.getMenu());
                }
            });
            lTime.setText(sdf.format(date));
        }),
            new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

