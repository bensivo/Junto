package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.gui.EditFileTab;
import main.gui.TabController;
import main.networking.*;
import main.networking.interfaces.NetworkManagerListener;
import main.networking.interfaces.NetworkManager;
import main.util.Diff;

public class MainWindowController implements NetworkManagerListener {

    JuntoConnection connection;
    boolean detectChange = true;

    @FXML private TabPane tabPane;
    @FXML private Button button_save, button_new, button_save_as, button_open, button_host, button_join;
    @FXML private VBox vbox_left;
    @FXML private HBox hbox_top;

    private TabController tabController;

    @FXML
    public void initialize(){
        initUI();

        //Create networking objects
        connection = new JuntoConnection();
        connection.attach(this);

        tabController = new TabController(tabPane);
        tabController.newTab();

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
                }
        );
        button_new.setOnMouseClicked(
                (MouseEvent e)->{
                    System.out.println("new button clicked");
                    tabController.newTab();
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
    }

    @Override
    public void onDiffPacketReceived(Diff diff) {
        for(Tab tab: tabPane.getTabs()){
            if(tab instanceof EditFileTab){
                EditFileTab editFileTab = (EditFileTab)tab;
                if(editFileTab.getFilepath().equals(diff.getSourceId())){
                    editFileTab.applyDiff(diff);
                }
            }

        }
    }
}
