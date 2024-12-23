package be.iramps.ue1103.mvc.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

import be.iramps.ue1103.mvc.Controller.Controller;

public class Main extends Application implements PropertyChangeListener, IView {
    private static Scene scene;
    private static Stage stage;
    private Pane actualParent; 
    private Controller control;
    private Section sectionWindow;
    private Status statusWindows;

    public void setController(Controller control) {
        this.control = control;
        this.sectionWindow = new Section(this);
        this.statusWindows = new Status(this);
    }

    public Controller getController(){
        return this.control;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "listeSection":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class)) {
                    this.showAllSections((ArrayList<String>) evt.getNewValue());
                }
                break;

            case "sectionSelected":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class)) {
                    this.showSection((ArrayList<String>) evt.getNewValue());
                }
                break;
            case "listeStatus":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class)) {
                    this.showAllStatus((ArrayList<String>) evt.getNewValue());
                }
                break;
            case "statusSelected":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class)) {
                    this.showStatus((ArrayList<String>) evt.getNewValue());
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Main.stage = stage;
        // Préparation du stage pour gérer la fermeture du programme.
        Main.stage.setOnCloseRequest(this.control.generateCloseEvent());

        // Préparation de la première fenêtre
        showPrincipalWindow();
        stage.show();
    }

    public void showPrincipalWindow(){
        actualParent = new VBox();
        Supplier<String[]> supplier = () -> new String[] {""};

        // Sections
        HBox boutonSection = new HBox();
        Button afficherSection = new Button("Afficher les sections");
        Button ajouterSection = new Button("Ajouter" );        
        afficherSection.setOnAction(control.generateEventHandlerAction("show-sections", supplier ));
        ajouterSection.setOnAction(control.generateEventHandlerAction("add-section", supplier ));
        boutonSection.getChildren().addAll(afficherSection, ajouterSection);
        actualParent.getChildren().add(boutonSection);

        //Status        
        HBox boutonStatus = new HBox();
        Button afficherStatus = new Button("Afficher les status");
        Button ajouterStatus = new Button("Ajouter" );
        afficherStatus.setOnAction(control.generateEventHandlerAction("show-status", supplier ));
        ajouterStatus.setOnAction(control.generateEventHandlerAction("add-status", supplier ));
        boutonStatus.getChildren().addAll(afficherStatus, ajouterStatus);
        actualParent.getChildren().add(boutonStatus);


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
         ListView<String> listView = this.sectionWindow.showAllSections(listeSection);
         showPrincipalWindow();
         actualParent.getChildren().add(listView);
    }

    public void showSection(ArrayList<String> infoSection){
        scene.setRoot(this.sectionWindow.showSection(infoSection));
    }

    public void addNewSection(){
        scene.setRoot(this.sectionWindow.addNewSection());
    }

    @Override
    public void addNewStatus() {
        scene.setRoot(this.statusWindows.addNewStatus());
    }

    @Override
    public void showAllStatus(ArrayList<String> listeStatus) {
        ListView<String> listView = this.statusWindows.showAllstatus(listeStatus);
        showPrincipalWindow();
        actualParent.getChildren().add(listView);
    }
    @Override
    public void showStatus(ArrayList<String> infoStatus) {
        scene.setRoot(this.statusWindows.showStatus(infoStatus));
    }
}
