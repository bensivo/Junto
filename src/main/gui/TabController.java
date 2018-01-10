package main.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabController {
    TabPane tabPane;

    public TabController(TabPane tabPane){
        this.tabPane = tabPane;
    }

    /**
     * Create a new tab with the given title
     */
    public void newTab(String name){
        ObservableList<Tab> tabs = this.tabPane.getTabs();
        tabs.add(new EditFileTab(name, null));
    }

    /**
     * Create a new Untitled tab and add it to the panel
     *
     * Tab will be named "Untitled"+n such that the title is unique
     *   ex. "Untitled1", "Untitled2"
     */
    public void newTab(){
        ObservableList<Tab> tabs = this.tabPane.getTabs();
        String[] titles = new String[tabs.size()];

        for(int i=0; i<tabs.size(); i++){
            EditFileTab editFileTab = (EditFileTab) tabs.get(i);
            //titles[i] = editFileTab.getFilepath();
        }

        newTab(getUniqueTitle(titles));
    }

    /**
     * Find the String of the form "Untitled1"..."Untitled2"..."Untitled3"...
     * That isn't already in the array given. Used to label new, unnamed tabs
     *
     * @param titles Titles already in use
     * @return "Untitled" + n so that the string is unique among given titles
     */
    private String getUniqueTitle(String[] titles){
        int count = 1;
        boolean flag;
        do{
            flag = false;
            for (String title : titles) {
                if (title.equals("Untitled" + count)) {
                    flag = true;
                }
            }
        } while(!flag);

        return "Untitled" + count;
    }


}

