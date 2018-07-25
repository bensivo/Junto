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
import main.networking.core.DataPacket;
import main.networking.interfaces.NetworkManager;
import main.networking.utils.ByteUtils;
import main.optransform.Operation;

import java.io.File;
import java.io.IOException;

import static main.networking.core.DataPacket.TYPE_OP;

/**
 * Main controller of the application. Defines UI operations, and implements
 * networking operations, inasmuch as they relate to the UI.
 */
public class MainWindowController {

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

        // Create networking objects
        connection = new JuntoConnection();

        // New Routing rule. All Operation packets get routed to onOperationReceived()
        connection.getDataPacketRouter().registerReceiver(new DataPacketRouter.Receiver() {
            @Override
            public boolean shouldReceivePacket(DataPacket dp) {
                return dp.type == TYPE_OP;
            }

            @Override
            public void receivePacket(DataPacket dp) {
                try{
                    Operation operation = (Operation) ByteUtils.fromBytes(dp.data);
                    MainWindowController.this.onOperationReceived(operation);
                }catch(IOException | ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        });

        initUILayout();
        setOnClickEvents();
    }

    /**
     * Handle operations from remote connections
     *
     * @param op operation received
     */
    private void onOperationReceived(Operation op) {
        for(Tab tab: tabPane.getTabs()){
            if(tab instanceof EditFileTab){
                //TODO: Get it to actually route to the correct tab
                EditFileTab editFileTab = (EditFileTab)tab;
                editFileTab.applyOperation(op);
            }

        }
    }

    /**
     * Initializes UI layout: set button width and height, initialize external UI components, etc.
     */
    private void initUILayout(){
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

    /**
     * Define what each button does on click.
     */
    private void setOnClickEvents(){
        button_host.setOnMouseClicked(
                (MouseEvent e)->{
                    System.out.println("host button clicked");
                    connection.switchNetworkManager(NetworkManager.TYPE_SERVER);
                    connection.getNetworkManager().start();
                    button_join.setDisable(true);
                }
        );
        button_join.setOnMouseClicked(
                (MouseEvent e)->{
                    System.out.println("join button clicked");
                    connection.switchNetworkManager(NetworkManager.TYPE_CLIENT);
                    connection.getNetworkManager().start();
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

}
