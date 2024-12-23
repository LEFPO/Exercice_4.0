package be.iramps.ue1103.mvc.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

import be.iramps.ue1103.mvc.Controller.Controller;

public class PrimaryView extends Application implements PropertyChangeListener, IView {
    private static Scene scene;
    private static Stage stage;
    private Pane actualParent; 
    private Controller control;

    public void setController(Controller control) {
        this.control = control;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "listeSection":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class)) //Pour être sur que c'est une ArrayList.
                    this.showAllSections((ArrayList<String>) evt.getNewValue());
                break;

            case "sectionSelected":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class))
                    this.showSection((ArrayList<String>) evt.getNewValue());
            default:
                break;//Bien mettre une exception pour mieux se retrouvé
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        PrimaryView.stage = stage;
        // Préparation du stage pour gérer la fermeture du programme.
        PrimaryView.stage.setOnCloseRequest(this.control.generateCloseEvent());

        // Préparation de la première fenêtre
        showPrincipalWindow();
        stage.show();
    }

    public void showPrincipalWindow(){
        actualParent = new VBox();
        HBox boutonParent = new HBox();
        Button afficher = new Button("Afficher les sections");
        Button ajouter = new Button("Ajouter" );
        
        Supplier<String[]> supplier = () -> new String[] {""};
        afficher.setOnAction(control.generateEventHandlerAction("show-sections", supplier ));
        ajouter.setOnAction(control.generateEventHandlerAction("add-section", supplier ));

        boutonParent.getChildren().addAll(afficher, ajouter);
        actualParent.getChildren().add(boutonParent);
        scene = new Scene(actualParent, 640, 480);
        stage.setScene(scene);
    }

    @Override
    public void launchApp() {
        Platform.startup(() -> {
            Stage stage = new Stage();
            try {
                this.start(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stopApp() {        
        Platform.exit();
    }

    public void showAllSections(ArrayList<String> listeSection){
         ObservableList<String> sections = FXCollections.observableArrayList(listeSection);
         ListView<String> listView = new ListView<String>(sections);
         Supplier<String[]> supplier = () -> new String[] {listView.getSelectionModel().getSelectedItem()};
         listView.setOnMouseClicked(control.generateEventHandlerMouse("show-section", supplier));
         showPrincipalWindow();
         actualParent.getChildren().add(listView);
    }

    public void showSection(ArrayList<String> infoSection){
        String id = infoSection.get(0);
        String nom = infoSection.get(1);
        actualParent = new VBox(10);

        // 1ère ligne
        HBox buttonParent = new HBox(5);
        Button afficher = new Button("Afficher les sections");
        Button sauver = new Button("Sauver les changements");
        Button supprimer = new Button("Supprimer la section");

        // 2ème ligne
        HBox infoParent = new HBox(10);
        Label idTexte = new Label("ID:");
        Label nomTexte = new Label("Nom: ");
        Label idL = new Label(id);
        TextField nomT = new TextField(nom);
        
        Supplier<String[]> supplier = () -> new String[] {id, nomT.getText()};
        sauver.setOnAction(control.generateEventHandlerAction(
                "update-section",supplier
                ));

        supplier = () -> new String[] {""};
        afficher.setOnAction(control.generateEventHandlerAction("show-sections", supplier ));      
        
        supplier = () -> new String[] {id};
        supprimer.setOnAction(control.generateEventHandlerAction("delete-section", supplier));
        buttonParent.getChildren().add(afficher); 
        buttonParent.getChildren().add(sauver);
        buttonParent.getChildren().add(supprimer);
       
        infoParent.getChildren().addAll(idTexte,idL,nomTexte,nomT);

        actualParent.getChildren().addAll(buttonParent, infoParent);
        scene.setRoot(actualParent);

    }

    public void addNewSection(){
        actualParent = new VBox(10);

        // 1ère ligne
        HBox buttonParent = new HBox(5);
        Button afficher = new Button("Afficher les sections");
        Button sauver = new Button("Sauver les changements");

        // 2ème ligne
        HBox infoParent = new HBox(10);
        Label idTexte = new Label("ID:");
        Label nomTexte = new Label("Nom: ");
        Label idL = new Label("N/A");
        TextField nomT = new TextField("");

        
        Supplier<String[]> supplier = () -> new String[] {""};
        afficher.setOnAction(control.generateEventHandlerAction("show-sections", supplier ));
        supplier = () -> new String[] {nomT.getText()};
        sauver.setOnAction(control.generateEventHandlerAction("insert-section",supplier));

        buttonParent.getChildren().add(afficher); 
        buttonParent.getChildren().add(sauver);
       
        infoParent.getChildren().addAll(idTexte,idL,nomTexte,nomT);

        actualParent.getChildren().addAll(buttonParent, infoParent);
        scene.setRoot(actualParent);
    }
}
