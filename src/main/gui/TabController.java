package main.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import main.networking.JuntoConnection;

public class TabController {
    TabPane tabPane;
    JuntoConnection juntoConnection = null;

    public TabController(TabPane tabPane, JuntoConnection juntoConnection) {
        this.tabPane = tabPane;
        this.juntoConnection = juntoConnection;
    }

    /**
     * Create a new tab with the given title
     */
    public void newTab(String name){
        ObservableList<Tab> tabs = this.tabPane.getTabs();
        tabs.add(new EditFileTab(name, null, juntoConnection));
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
            titles[i] = editFileTab.getTitle();
        }

        newTab(getUniqueTitle(titles));
    }


    /**
     * Find the String of the form "Untitled1"..."Untitled2"..."Untitled3"...
     * That isn't already in the array given.
     *
     * @param titles Titles already in use
     * @return "Untitled" + n so that the string is unique among given titles
     */
    private String getUniqueTitle(String[] titles){
        int count = 1;
        boolean uniqueTitleFound;
        do{
            uniqueTitleFound = true;

            for (String title : titles) {
                System.out.println("Title:"+title);
                if(title.equals("Untitled" + count)){
                    uniqueTitleFound = false;
                    break;
                }
            }

            if(!uniqueTitleFound)
                count++;
        } while(!uniqueTitleFound);

        return String.valueOf("Untitled" + count);
    }


}

