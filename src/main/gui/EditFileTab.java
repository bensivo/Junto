package main.gui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import main.networking.JuntoConnection;
import main.optransform.Applicator;
import main.optransform.OpParser;
import main.optransform.Operation;
import main.util.Logger;

import java.util.List;

/**
 * Custom Tab that contains a TextArea, and is linked to a specific file.
 */
public class EditFileTab extends Tab{
    TextArea textArea = null;
    String filepath = "", title = "";
    JuntoConnection juntoConnection = null;

    boolean detectChange = true;

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

                        if(juntoConnection!=null){
                            juntoConnection.broadcast(op);
                        }
                    }

                    //Logger.logI("EDIT_FILE_TAB", "Change in tab: " + title);
                    //Diff diff = new Diff(title, oldValue, newValue);

                    //if(juntoConnection!=null){
                    //    juntoConnection.handleLocalDiff(diff);
                    //}
                    //else{
                    //    Logger.logE("EDIT_FILE_TAB", "Change not handled because juntoConnection is null");
                    //}
                }
            });

        this.setContent(textArea);
    }

    /**
     * Changes the textArea of this tab according to the given Diff object
     * @param diff
     */
    //public void applyDiff(Diff diff){
    //    String s = textArea.getText();
    //    StringBuilder builder = new StringBuilder(s);
    //    builder.delete(diff.getIndex(), diff.getIndex() + diff.getDel().length());
    //    builder.insert(diff.getIndex(), diff.getAdd());

    //    String newString = builder.toString();
    //    detectChange = false; //keep the onChange() event from being fired when we call setText
    //    textArea.setText(newString);
    //    detectChange = true;
    //}

    public void applyOp(Operation op){
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
