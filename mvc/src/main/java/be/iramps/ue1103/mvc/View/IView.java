package be.iramps.ue1103.mvc.View;

import java.util.ArrayList;

import be.iramps.ue1103.mvc.Controller.Controller;

public interface IView {
    public void setController(Controller control);
    public void launchApp();
    public void stopApp();
    public void showPrincipalWindow();
    public void addNewSection();
    public void showAllSections(ArrayList<String> listeSection);
    public void showSection(ArrayList<String> infoSection);
    
}
