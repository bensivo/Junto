package main.gui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import main.networking.JuntoConnection;
import main.util.Diff;

public class EditFileTab extends Tab{
    TextArea textArea;
    String fileName, title;
    JuntoConnection juntoConnection;

    boolean detectChange = true;

    public EditFileTab(String title, JuntoConnection juntoConnection){
        super(title);
        this.fileName = title; //TODO Change this to the actual filename
        this.juntoConnection = juntoConnection;
        this.textArea = new TextArea();

        textArea.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (detectChange) {
                    Diff diff = new Diff(this.fileName, oldValue, newValue);
                    juntoConnection.handleLocalDiff(diff);
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
        detectChange = false; //keep the onChange() event from being fired when we setText
        textArea.setText(newString);
        detectChange = true;

    }

    public TextArea getTextArea(){
        return this.textArea;
    }

    public String getFileName(){
        return this.fileName;
    }
}
