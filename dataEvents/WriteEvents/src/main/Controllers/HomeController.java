package main.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Models.Event;
import main.Models.EventModel;
import main.Models.ListEvents;
import main.Models.Writer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class HomeController {
    public TextField msgfutButton;
    public TextField msgpresButton;
    public TextField msgpastButton;
    public TextField identButton;
    public Button eliminarButton;
    public TextField insereIndexButton;
    public TableView tableEvents;
    public Button agendarButton;
    public Button sairButton;
    public DatePicker dataButton;
    public DatePicker datadelButton;
    public TextField horaButton;
    public TextField horadelButton;

    // Lista de eventos
    private ListEvents le;

    // Nome do ficheiro de eventos
    public static String EVENTS_DIR = "./../TP3/";
    private static String EVENTS_FILENAME = "dataEvents.txt";

    public void initialize(){

        try {
            // Inicializar a lista de eventos
            this.le = new ListEvents();
            this.le.preencheEventos(EVENTS_DIR + EVENTS_FILENAME);

            // Indice do evento
            TableColumn tcIndex = new TableColumn("Index");
            tcIndex.setEditable(false);
            tcIndex.setReorderable(false);
            tcIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
            // Identificacao Evento
            TableColumn tcIdent = new TableColumn("Identificação");
            tcIdent.setEditable(false);
            tcIdent.setReorderable(false);
            tcIdent.setCellValueFactory(new PropertyValueFactory<>("ident"));
            // Data
            TableColumn tcData = new TableColumn("Data");
            tcData.setEditable(false);
            tcData.setReorderable(false);
            tcData.setCellValueFactory(new PropertyValueFactory<>("data"));
            // Hora
            TableColumn tcHora = new TableColumn("Hora");
            tcHora.setEditable(false);
            tcHora.setReorderable(false);
            tcHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
            tableEvents.getColumns().clear();
            tableEvents.getItems().clear();
            tableEvents.getColumns().addAll(tcIndex, tcIdent, tcData, tcHora);
            for(Event e : this.le.getEventos()){
                EventModel em = new EventModel(e.getIndex(),e.getIdentificacao(),e.getDataEvento(),e.getHoraEvento());
                tableEvents.getItems().add(em);
            }

        } catch (IOException e){
            System.out.println("Erro ao carregar o ficheiro de eventos!!");
        }
    }

    @FXML
    public void btAgendarAction(){

        try {
            if (identButton.getText().length() != 0
                    && msgpastButton.getText().length() != 0
                    && msgpresButton.getText().length() != 0
                    && msgfutButton.getText().length() != 0
                    && dataButton.getEditor().getText().length() != 0
                    && datadelButton.getEditor().getText().length() != 0
                    && horaButton.getText().length() != 0
                    && horadelButton.getText().length() != 0) {
                System.out.println("Agendar");

                this.writeEvento();
                lancaAlerta(Alert.AlertType.CONFIRMATION, "Evento agendado", "Confirmado", "Evento agendado com sucesso!!!");
            } else {
                lancaAlerta(Alert.AlertType.ERROR, "Erro no registo", "Erro nos argumentos", "Não foi possível agendar o evento!!!");
            }
            this.cleanFields();
            this.initialize();
        } catch (IOException e){
            lancaAlerta(Alert.AlertType.ERROR, "Erro na escrita", "Erro ao escrever para ficheiro", "Não foi possível agendar o evento!!!");
            this.cleanFields();
            this.initialize();
        } catch (DateTimeParseException e){
            lancaAlerta(Alert.AlertType.ERROR, "Erro no registo", "Erro nos argumentos", "Não foi possível agendar o evento!!!");
            this.cleanFields();
            this.initialize();
        }
    }

    private void lancaAlerta(Alert.AlertType error, String s, String s2, String s3) {
        Alert a = new Alert(error);

        a.setTitle(s);

        a.setHeaderText(s2);

        a.setContentText(s3);

        a.showAndWait();
    }

    @FXML
    public void btEliminarAction(){

        List<Event> lista = this.le.getEventos();
        int ind = Integer.parseInt(insereIndexButton.getText());
        try {
            if (ind <= lista.size()) {

                lista.remove(ind - 1);

                Writer.create(this.le.toString(),EVENTS_FILENAME);

                this.moveFicheiro();

                lancaAlerta(Alert.AlertType.CONFIRMATION, "Evento eliminado", "Eliminado", "Evento eliminado com sucesso!!!");
            } else {
                lancaAlerta(Alert.AlertType.ERROR, "Erro no registo", "Erro nos argumentos", "Não foi possível agendar o evento!!!");
            }
            this.cleanFields();
            this.initialize();
        } catch (NumberFormatException e){
            lancaAlerta(Alert.AlertType.ERROR, "Erro no registo", "Erro nos argumentos", "Não foi possível agendar o evento!!!");
            this.cleanFields();
            this.initialize();
        } catch (IOException e){
            lancaAlerta(Alert.AlertType.ERROR, "Erro na escrita", "Erro ao escrever para ficheiro", "Não foi possível eliminar o evento!!!");
            this.cleanFields();
            this.initialize();
        }

        System.out.println("Eliminar!!");
    }

    @FXML
    public void btSairAction(){

        System.exit(0);
    }

    /**
     * Escreve o novo evento para o ficheiro
     * @throws IOException
     * @throws DateTimeParseException
     */
    private void writeEvento() throws IOException, DateTimeParseException {

        LocalDate data = LocalDate.parse(dataButton.getEditor().getText(),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate datadel = LocalDate.parse(datadelButton.getEditor().getText(),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalTime hora = LocalTime.parse(horaButton.getText());
        LocalTime horadel = LocalTime.parse(horadelButton.getText());

        Event e = new Event(0,identButton.getText(),msgpastButton.getText(),msgpresButton.getText(),msgfutButton.getText(),data,hora,datadel,horadel);
        Writer.write(e.toString(),EVENTS_FILENAME);
    }

    /**
     * Limpa os campos da tela
     */
    private void cleanFields(){

        identButton.clear();
        msgpastButton.clear();
        msgpresButton.clear();
        msgfutButton.clear();
        dataButton.getEditor().clear();
        horaButton.clear();
        datadelButton.getEditor().clear();
        horadelButton.clear();
        insereIndexButton.clear();
    }

    /**
     * Move o ficheiro temporario para o destino
     * @return
     */
    private boolean moveFicheiro(){

        File dataEvents = new File(EVENTS_FILENAME);

        File diretoriadestino = new File(EVENTS_DIR);

        return dataEvents.renameTo(new File(diretoriadestino,dataEvents.getName()));
    }

}
