package main.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import main.Comunicator.Event;
import main.Comunicator.ListEvents;
import main.Comunicator.Manager;
import main.Models.EventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class EventsAtualizator implements Runnable{

    /**
     * Variável que guarda o tempo do período de
     * pesquisas realizadas ao agente em milis
     */
    private int actualization_period;

    /**
     * Variáveç que guarda o objeto da classe
     * manager responsável por realizar as consultas
     * dos dados com recurso ao agente snmp
     */
    private Manager m;

    /**
     * Variável que guarda a lista de eventos que
     * vão sendo obtidos pela consulta ao agente
     */
    private ListEvents le;

    /**
     * Variável que guarda a tabela na qual serão
     * colocados e, neste caso, atualizados os eventos.
     */
    private TableView<EventModel> eventsTable;

    /**
     * Variável que serve para garantir exclusão
     * mútua aquando de alterações na tabela
     */
    private Lock l;

    /**
     * Construtor para objetos da classe EventsAtualizator.
     * @param actualization_period
     * @param m
     * @param eventsTable
     */
    public EventsAtualizator(int actualization_period, Manager m, ListEvents le,
                             TableView<EventModel> eventsTable, Lock l) {
        this.actualization_period = actualization_period;
        this.m = m;
        this.le = le;
        this.eventsTable = eventsTable;
        this.l = l;
    }

    /**
     * Método que retorna uma lista com o conteúdo da
     * tabela para voltar a atualizar apenas o que estiver
     * a ser mostrado
     * @return
     */
    public List<Integer> getTableContentIndexes(){

        List<Integer> context = new ArrayList<>();
        for(EventModel em : this.eventsTable.getItems())
            context.add(em.getIndex());
        return context;
    }

    @Override
    public void run() {

        while(true) {

            System.out.println("Esperando para atuaizar...");
            try{
                Thread.sleep(this.actualization_period);
            }
            catch (InterruptedException e){
                System.err.println("[INFO] Tempo de espera não foi totalmente respeitado");
            }

            m.start();

            // Buscar a lista de eventos resultante
            this.le.setEventos(m.getEventos().getEventos());

            /* Garantimos exclusão mútua no acesso à tabela */
            this.l.lock();

            List<Integer> context = this.getTableContentIndexes();
            eventsTable.getItems().clear();

            List<EventModel> atual = new ArrayList<>();
            for (Event e : this.le.getEventos().values()) {

                if(context.contains(e.getIndex())) {
                    EventModel em = new EventModel(e.getIndex(), e.getIdentificacao(), e.getMsg(), e.getAnos(), e.getMeses(), e.getSemanas(), e.getDias(), e.getHoras(), e.getMinutos());
                    atual.add(em);
                    System.out.println("Atualizei evento: " + em.toString());
                }
            }
            eventsTable.getItems().addAll(atual);
            System.out.println("> Atualizei dados");

            /* Cedemos o respetivo lock */
            this.l.unlock();
        }
    }
}
