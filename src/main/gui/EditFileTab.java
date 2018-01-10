package main.gui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import main.networking.JuntoConnection;
import main.util.Diff;

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
        this.filepath = "";
        this.juntoConnection = juntoConnection;
        this.textArea = new TextArea();
        detectChange = true;

        textArea.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (detectChange) {
                    Diff diff = new Diff(filepath, oldValue, newValue);
                    if(juntoConnection!=null){
                        juntoConnection.handleLocalDiff(diff);
                    }
                }
            });

        this.setContent(textArea);
    }

    /**
     * Changes the textArea of this tab according to the given Diff object
     * @param diff
     */
    public void applyDiff(Diff diff){
        String s = textArea.getText();
        StringBuilder builder = new StringBuilder(s);
        builder.delete(diff.getIndex(), diff.getIndex() + diff.getDel().length());
        builder.insert(diff.getIndex(), diff.getAdd());

        String newString = builder.toString();
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
}
