package main.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Comunicator.Event;
import main.Comunicator.ListEvents;
import main.Comunicator.Manager;
import main.Models.EventModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HomeController {

    public Button sairButton;
    public TableView eventsTable;
    public ComboBox criterioButton;
    public ComboBox valoresButton;

    // Lista de Eventos
    private ListEvents le;


    public void initialize() throws IOException {

        // Efetuar os pedidos SNMP ao Agente para obter os eventos
        Manager m = new Manager();
        m.start();

        // Buscar a lista de eventos resultante
        this.le = m.getEventos();

        // Definir os criterios de filtragem
        Set<String> crit = this.getCriterios();
        ObservableList<String> listaitems = FXCollections.observableArrayList(crit);
        criterioButton.setItems(listaitems);
        criterioButton.setValue("Todos");

        // Criar a tabela
        criarTable();

        for(Event e : this.le.getEventos().values()){
            EventModel em = new EventModel(e.getIndex(),e.getIdentificacao(),e.getMsg(),e.getAnos(),e.getMeses(),e.getSemanas(),e.getDias(),e.getHoras(),e.getMinutos());
            eventsTable.getItems().add(em);
        }

    }

    private void criarTable() {
        // Indice do evento
        TableColumn tcIndex = new TableColumn("Index");
        tcIndex.setEditable(false);
        tcIndex.setReorderable(false);
        tcIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
        // Identificacao Evento
        TableColumn tcIdent = new TableColumn("Identificação");
        tcIdent.setMinWidth(200);
        tcIdent.setEditable(false);
        tcIdent.setReorderable(false);
        tcIdent.setCellValueFactory(new PropertyValueFactory<>("ident"));
        // Msg Evento
        TableColumn tcMsg = new TableColumn("Mensagem");
        tcMsg.setEditable(false);
        tcMsg.setMinWidth(200);
        tcMsg.setReorderable(false);
        tcMsg.setCellValueFactory(new PropertyValueFactory<>("msg"));
        // Anos
        TableColumn tcAnos = new TableColumn("Anos");
        tcAnos.setEditable(false);
        tcAnos.setReorderable(false);
        tcAnos.setCellValueFactory(new PropertyValueFactory<>("anos"));
        // Meses
        TableColumn tcMeses = new TableColumn("Meses");
        tcMeses.setEditable(false);
        tcMeses.setReorderable(false);
        tcMeses.setCellValueFactory(new PropertyValueFactory<>("meses"));
        // Semanas
        TableColumn tcSemanas = new TableColumn("Semanas");
        tcSemanas.setEditable(false);
        tcSemanas.setReorderable(false);
        tcSemanas.setCellValueFactory(new PropertyValueFactory<>("semanas"));
        // Dias
        TableColumn tcDias = new TableColumn("Dias");
        tcDias.setEditable(false);
        tcDias.setReorderable(false);
        tcDias.setCellValueFactory(new PropertyValueFactory<>("dias"));
        // Horas
        TableColumn tcHoras = new TableColumn("Horas");
        tcHoras.setEditable(false);
        tcHoras.setReorderable(false);
        tcHoras.setCellValueFactory(new PropertyValueFactory<>("horas"));
        // Minutos
        TableColumn tcMinutos = new TableColumn("Minutos");
        tcMinutos.setEditable(false);
        tcMinutos.setReorderable(false);
        tcMinutos.setCellValueFactory(new PropertyValueFactory<>("minutos"));
        eventsTable.getColumns().clear();
        eventsTable.getItems().clear();
        eventsTable.getColumns().addAll(tcIndex, tcIdent, tcMsg, tcAnos, tcMeses,tcSemanas, tcDias, tcHoras, tcMinutos);
    }

    @FXML
    public void btCriterioAction(){

        if(criterioButton.getValue() != null) {

            switch (criterioButton.getValue().toString()) {

                case "Todos": {

                    criarTable();
                    valoresButton.getItems().clear();
                    valoresButton.setDisable(true);
                    for (Event e : this.le.getEventos().values()) {
                        EventModel em = new EventModel(e.getIndex(), e.getIdentificacao(), e.getMsg(), e.getAnos(), e.getMeses(), e.getSemanas(), e.getDias(), e.getHoras(), e.getMinutos());
                        eventsTable.getItems().add(em);
                    }
                    break;
                }
                case "Ano": {

                    valoresButton.setDisable(false);
                    Set<String> crit = this.le.getAnos();
                    ObservableList<String> listaitems = FXCollections.observableArrayList(crit);
                    valoresButton.setItems(listaitems);
                    break;
                }
                case "Mês": {
                    valoresButton.setDisable(false);
                    Set<String> crit = this.le.getMeses();
                    ObservableList<String> listaitems = FXCollections.observableArrayList(crit);
                    valoresButton.setItems(listaitems);
                    break;
                }
                case "Semana": {
                    valoresButton.setDisable(false);
                    Set<String> crit = this.le.getSemanas();
                    ObservableList<String> listaitems = FXCollections.observableArrayList(crit);
                    valoresButton.setItems(listaitems);
                    break;
                }
                case "Dia": {
                    valoresButton.setDisable(false);
                    Set<String> crit = this.le.getDias();
                    ObservableList<String> listaitems = FXCollections.observableArrayList(crit);
                    valoresButton.setItems(listaitems);
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * Mostrar a tabela mediante um determinado critério e um valor
     * relativo a esse critério
     * Por exemplo, se o critério for o mês, então nos valores só podem estar meses
     * que tẽm eventos
     */
    @FXML
    public void btValoresAction(){

        criarTable();

        if(valoresButton.getValue() != null && criterioButton.getValue() != null) {

            List<Event> eventos;

            // Verificar quais os criterios
            switch (criterioButton.getValue().toString()) {

                case "Ano": {
                    eventos = this.le.getEventosAno(valoresButton.getValue().toString());
                    break;
                }
                case "Mês": {
                    eventos = this.le.getEventosMes(valoresButton.getValue().toString());
                    break;
                }
                case "Semana": {
                    eventos = this.le.getEventosSemana(valoresButton.getValue().toString());
                    break;
                }
                case "Dia": {
                    eventos = this.le.getEventosDia(valoresButton.getValue().toString());
                    break;
                }
                default: {
                    eventos = (List<Event>) this.le.getEventos().values();
                    break;
                }
            }
            // Adicionar os eventos
            for (Event e : eventos) {
                EventModel em = new EventModel(e.getIndex(), e.getIdentificacao(), e.getMsg(), e.getAnos(), e.getMeses(), e.getSemanas(), e.getDias(), e.getHoras(), e.getMinutos());
                eventsTable.getItems().add(em);
            }
        }
    }

    @FXML
    public void btSairAction(){

        System.exit(0);
    }

    /**
     * Criar os criterios de filtragem
     * @return
     */
    public Set<String> getCriterios(){

        Set<String> crit = new TreeSet<>();

        crit.add("Todos");
        crit.add("Ano");
        crit.add("Mês");
        crit.add("Semana");
        crit.add("Dia");

        return crit;
    }
}

