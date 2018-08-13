package main.gui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import main.networking.DataPacketRouter;
import main.networking.JuntoConnection;
import main.networking.core.DataPacket;
import main.networking.utils.ByteUtils;
import main.optransform.Applicator;
import main.optransform.OpParser;
import main.optransform.OpTContainer;
import main.optransform.Operation;
import main.util.Logger;

import java.util.List;

import static main.networking.core.DataPacket.TYPE_OP;

/**
 * Custom Tab that contains a TextArea, and is linked to a specific file.
 */
public class EditFileTab extends Tab implements OpTContainer, DataPacketRouter.Receiver{
    private TextArea textArea = null;
    private String filepath = "";
    private String title = "";
    private JuntoConnection juntoConnection = null;

    private boolean detectChange = true;

    public EditFileTab(String title){
        this(title, "", null);
    }

    public EditFileTab(String title, String filepath){
        this(title, filepath, null);
    }

    public EditFileTab(String title, String filepath, JuntoConnection juntoConnection){
        super(title);
        this.title = title;
        this.filepath = "";
        this.juntoConnection = juntoConnection;
        this.textArea = new TextArea();
        detectChange = true;

        // Attach a listener for onChange events
        textArea.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (detectChange) {
                    List<Operation> operations = OpParser.getOperations(oldValue, newValue);
                    for(Operation op: operations){
                        Logger.logI("EDIT_FILE_TAB", "Change in tab: " + op.toString());
                        op.source = this.title;

                        if(juntoConnection != null){
                            if(juntoConnection.getNetworkManager() != null){
                                juntoConnection.getNetworkManager().broadcast(op);
                            }
                        }
                    }
                }
            });

        this.setContent(textArea);
    }

    @Override
    public boolean shouldReceivePacket(DataPacket dp){
        return dp.type == TYPE_OP;
    }

    @Override
    public void receivePacket(DataPacket dp){
        try{
            Operation operation = (Operation)ByteUtils.fromBytes(dp.data);
            this.applyOperation(operation);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override //OpTContainer
    public void applyOperation(Operation op){
        String newString =  Applicator.applyOperation(textArea.getText(), op);
        detectChange = false; //keep the onChange() event from being fired when we call setText
        textArea.setText(newString);
        detectChange = true;
    }

    public TextArea getTextArea(){
        return this.textArea;
    }

    public String getFilepath(){
        return this.filepath;
    }

    public String getTitle(){return this.title;}
}
