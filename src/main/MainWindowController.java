package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.gui.EditFileTab;
import main.gui.TabController;
import main.networking.*;
import main.networking.interfaces.NetworkManagerListener;
import main.networking.interfaces.NetworkManager;
import main.optransform.Diff;

import java.io.File;

/**
 * Main controller of the application. Defines UI operations, and implements
 * networking operations, inasmuch as they relate to the UI.
 */
public class MainWindowController implements NetworkManagerListener {

    JuntoConnection connection;

    @FXML private BorderPane border_pane;
    @FXML private TabPane tabPane;
    @FXML private Button button_save, button_new, button_save_as, button_open, button_host, button_join;
    @FXML private VBox vbox_left;
    @FXML private HBox hbox_top;

    private TabController tabController = null;
    private Stage primaryStage = null;

    @FXML
    public void initialize(){

        //Create networking objects
        connection = new JuntoConnection();
        connection.attach(this);

        initUI();


        button_host.setOnMouseClicked(
            (MouseEvent e)->{
                System.out.println("host button clicked");
                connection.switchNetworkManager(NetworkManager.TYPE_SERVER);
                connection.start();
                button_join.setDisable(true);
            }
        );
        button_join.setOnMouseClicked(
            (MouseEvent e)->{
                System.out.println("join button clicked");
                connection.switchNetworkManager(NetworkManager.TYPE_CLIENT);
                connection.start();
                button_host.setDisable(true);
            }
        );
        button_save.setOnMouseClicked(
                (MouseEvent e)->{
                    System.out.println("save button clicked");
                }
        );
        button_open.setOnMouseClicked(
                (MouseEvent e)->{
                    System.out.println("open button clicked");
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open File");
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

                    if(primaryStage!=null) {
                        File selectedFile = fileChooser.showOpenDialog(primaryStage);
                        String filepath = selectedFile.getAbsolutePath();
                        String filename = selectedFile.getName();
                        tabController.newTab(filename);
                    }
                }
        );
        button_new.setOnMouseClicked(
                (MouseEvent e)->{
                    System.out.println("new button clicked");
                    tabController.newTab();
                    //tabController.newTab();
                }
        );
        button_save_as.setOnMouseClicked(
                (MouseEvent e)->{
                    System.out.println("save_as button clicked");
                }
        );

    }

    private void initUI(){
        button_host.setMaxWidth(Double.MAX_VALUE);
        button_join.setMaxWidth(Double.MAX_VALUE);

        tabController = new TabController(tabPane, this.connection);
        tabController.newTab();

        border_pane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        primaryStage = (Stage)newWindow;
                    }
                });
            }
        });
    }

    @Override
    public void onDiffPacketReceived(Diff diff) {
        for(Tab tab: tabPane.getTabs()){
            if(tab instanceof EditFileTab){
                EditFileTab editFileTab = (EditFileTab)tab;
                if(editFileTab.getTitle().equals(diff.getSourceId())){
                    editFileTab.applyDiff(diff);
                }
            }

        }
    }
}
