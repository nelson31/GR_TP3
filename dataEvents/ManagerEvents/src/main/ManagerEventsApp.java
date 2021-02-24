package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe ManagerEventsApp que é a classe principal deste manager!!
 *
 * @author Nelson Faria(A84727) e Miguel Oliveira(A83819)
 * @version 1.0 (02/2021)
 */

public class ManagerEventsApp extends Application {

    // Tela da Principal
    private static Scene mainScene;


    @Override
    public void start(Stage primaryStage) {

        try {

            primaryStage.setTitle("Datas de Eventos");

            Parent homefxml = FXMLLoader.load(getClass().getResource("/main/Views/home.fxml"));
            mainScene = new Scene(homefxml);

            primaryStage.setScene(mainScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e){
            System.out.println("Não foi possível iniciar a aplicação!!");
            System.exit(1);
        }
    }

    /**
     * Metodo principal do projeto
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
