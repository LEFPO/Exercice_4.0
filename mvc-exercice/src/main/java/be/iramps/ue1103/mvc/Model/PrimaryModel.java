package be.iramps.ue1103.mvc.Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import be.iramps.ue1103.mvc.Model.BL.Section;
import be.iramps.ue1103.mvc.Model.BL.Status;
import be.iramps.ue1103.mvc.Model.DAL.Sections.ISectionDAO;
import be.iramps.ue1103.mvc.Model.DAL.Sections.SectionDAO;
import be.iramps.ue1103.mvc.Model.DAL.Status.IStatusDAO;
import be.iramps.ue1103.mvc.Model.DAL.Status.StatusDAO;

public class PrimaryModel implements IModel{
    private PropertyChangeSupport support;
    private ISectionDAO iSectionDAO;
    private IStatusDAO iStatusDAO;
    Connection connexion;


    public PrimaryModel() {
        this.support = new PropertyChangeSupport(this);

        // Configuration de la connexion
        String url = "jdbc:postgresql://localhost:5432/ecole";
        String user = "postgres";
        String password = "Arkana10021994";

        try {
            // Connexion à la base de données
            this.connexion = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie à la base de données !");

            this.support = new PropertyChangeSupport(this);
            // Instanciation des DAO avec la connexion
            this.iSectionDAO = new SectionDAO(this.connexion);
            this.iStatusDAO = new StatusDAO(this.connexion);
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    @Override
    public void getAllSection(){
        ArrayList<Section> sections = this.iSectionDAO.getSections();
        ArrayList<String> sectionsName = new ArrayList<>();
        for (Section section : sections) {
            sectionsName.add(section.getNom());
        }
        support.firePropertyChange("listeSection", "", sectionsName);        
    }

    @Override
    public void getSection(String sectionName){
        int id = this.iSectionDAO.getIDSection(sectionName);
        ArrayList<String> infoSection = new ArrayList<>();
        infoSection.add(Integer.toString(id));
        infoSection.add(sectionName);
        support.firePropertyChange("sectionSelected", "", infoSection );
    }

    @Override
    public void deleteSection(String id) {
        this.iSectionDAO.deleteSection(Integer.parseInt(id));
        this.getAllSection();
    }

    @Override
    public void updateSection(String id, String nom) {
        this.iSectionDAO.updateSection(Integer.parseInt(id), nom);
        this.getSection(nom);
    }

    @Override
    public void insertSection(String nom) {
        this.iSectionDAO.addSection(nom);
        this.getSection(nom);
    }

    @Override
    public void getAllStatus() {
        ArrayList<Status> status = this.iStatusDAO.getStatus();
        ArrayList<String> statusName = new ArrayList<>();
        for (Status status1 : status) {
            statusName.add(status1.getNom());
        }
        support.firePropertyChange("listeStatus", "", statusName);
    }

    @Override
    public void insertStatus(String nom){
        this.iStatusDAO.addStatus(nom);
    }
    @Override
    public void updateStatus(String id, String nom){
        this.iStatusDAO.updateStatus(Integer.parseInt(id), nom);
    }
    @Override
    public void deleteStatus(String id){
        this.iStatusDAO.deleteStatus(Integer.parseInt(id));
    }
    @Override
    public void getIDStatus(String nom){
        this.iStatusDAO.getIDStatus(nom);
    }
    @Override
    public void getStatus(String statusName){
        int id = this.iStatusDAO.getIDStatus(statusName);
        ArrayList<String> infoStatus = new ArrayList<>();
        infoStatus.add(Integer.toString(id));
        infoStatus.add(statusName);
        support.firePropertyChange("statusSelected", "", infoStatus );
    }

    @Override
    public void close() {
        this.iSectionDAO.close();
        this.iStatusDAO.close();
    }

}
