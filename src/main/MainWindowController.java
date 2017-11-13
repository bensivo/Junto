package main;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.networking.ClientConnection;
import main.networking.JuntoClient;
import main.networking.JuntoServer;
import main.util.ByteUtils;
import main.util.StringChange;

public class MainWindowController implements ClientConnection.ClientConnectionListener{

    JuntoServer server;
    JuntoClient client;

    @FXML
    private TextArea mainTextArea;

    @FXML private Button button_save, button_new, button_save_as, button_open, button_host, button_join;

    @FXML private VBox vbox_left;
    @FXML private HBox hbox_top;

    @FXML
    public void initialize(){
        initUI();

        server = new JuntoServer(this);
        client = new JuntoClient();

        mainTextArea.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                StringChange stringChange = new StringChange(oldValue, newValue);
                System.out.println(stringChange.toStringShort());
                try{
                    if(client.isConnected()){
                        byte[] bytes = ByteUtils.toBytes(stringChange);
                        DataPacket packet = new DataPacket("client", "server", DataPacket.TYPE_CHAT, bytes);
                        client.sendPacket(packet);
                        System.out.println("String change Packet sent");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        );

        button_host.setOnMouseClicked(
            (MouseEvent e)->{
                System.out.println("host button clicked");
                server.setPort(5001);
                server.start();
            }
        );
        button_join.setOnMouseClicked(
            (MouseEvent e)->{
                System.out.println("disable button clicked");
                client.setServerName("localhost");
                client.setServerPort(5001);
                client.start();
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
    public void onStringChangePacket(StringChange stringChange) {
        String s = mainTextArea.getText();
        StringBuilder builder = new StringBuilder(s);
        builder.delete(stringChange.getIndex(), stringChange.getIndex() + stringChange.getDel().length());
        builder.insert(stringChange.getIndex(), stringChange.getAdd());

        String newString = builder.toString();
        mainTextArea.setText(newString);

    }
}
