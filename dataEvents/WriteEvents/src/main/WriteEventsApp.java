package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe WriteEventsApp responsavel por iniciar a execucao desta aplicação.
 * Esta aplicaçao serve para escrever e eliminar eventos no ficheiro de eventos
 *
 * @author Nelson Faria e Miguel Oliveira
 * @version 1.0 (02/2021)
 */

public class WriteEventsApp extends Application {

    // Tela da Principal
    private static Scene mainScene;


    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Datas de Eventos");

        Parent homefxml = FXMLLoader.load(getClass().getResource("/main/Views/home.fxml"));
        mainScene = new Scene(homefxml);

        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
