package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.TableView;


public class WriteEventsApp extends Application {

    // Stage (O que esta a ser mostrado para o utilizador)
    private static Stage stage;

    // Tela da Principal
    private static Scene mainScene;


    @Override
    public void start(Stage primaryStage) throws Exception{

        stage = primaryStage;

        primaryStage.setTitle("Datas de Eventos");

        Parent homefxml = FXMLLoader.load(getClass().getResource("/main/Views/home.fxml"));
        mainScene = new Scene(homefxml);

        primaryStage.setScene(mainScene);
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Usado para ser chamado quando se quiser mudar de tela
     * @param scene
     */
    public static void changeScreen(Scene scene){

        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
