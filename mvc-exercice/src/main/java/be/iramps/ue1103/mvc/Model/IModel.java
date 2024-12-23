package be.iramps.ue1103.mvc.Model;

import java.beans.PropertyChangeListener;

public interface IModel {
    public void addPropertyChangeListener(PropertyChangeListener pcl);
    public void removePropertyChangeListener(PropertyChangeListener pcl);
    public void getAllSection();
    public void getSection(String sectionName);
    public void deleteSection(String id);
    public void updateSection(String id, String nom); 
    public void insertSection(String nom);


    public void getAllStatus();
    public void insertStatus(String nom);
    public void updateStatus(String id, String nom);
    public void deleteStatus(String id);
    public void getIDStatus(String nom);
    public void getStatus(String statusName);

    public void close();


}
