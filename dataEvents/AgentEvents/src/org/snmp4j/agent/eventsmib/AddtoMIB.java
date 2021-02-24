package org.snmp4j.agent.eventsmib;

import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Variable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddtoMIB {

    // Lista de Eventos
    private ListEvents le;
    // Linhas a adicionar à tabela
    private List<Variable[]> tableRows;
    // Linha atual
    private int currentRow;
    // Coluna atual
    private int currentCol;
    // Valor total de eventos
    private long totalEventos;
    // Valor total de eventos que sao passados
    private long totalEventosPast;
    // Valor total de eventos que sao presentes
    private long totalEventosPres;
    // Valor total de eventos que sao futuros
    private long totalEventosFut;

    // Numero total de colunas
    private static int COLUMNSSIZE = 15;

    public AddtoMIB(){

        currentRow = 0;
        currentCol = 0;

        this.tableRows = new ArrayList<>();

        try {
            /* Adicionar os eventos à tabela(linhas) */
            this.le = new ListEvents();
            this.le.obtemDados();
            // Adicionar os restantes valores dos eventos
            for(Event e : le.getEventos()){
                if(!e.toDelete()) {
                    addRowValue(new Counter32(currentRow + 1));
                    addRowValue(e.getIdentFormat());
                    addRowValue(e.getMsgFormat());
                    addRowValue(e.getAnosFormat());
                    addRowValue(e.getMesesFormat());
                    addRowValue(e.getSemanasFormat());
                    addRowValue(e.getDiasFormat());
                    addRowValue(e.getHorasFormat());
                    addRowValue(e.getMinutosFormat());
                    addRowValue(e.getAnosDelFormat());
                    addRowValue(e.getMesesDelFormat());
                    addRowValue(e.getSemanasDelFormat());
                    addRowValue(e.getDiasDelFormat());
                    addRowValue(e.getHorasDelFormat());
                    addRowValue(e.getMinutosDelFormat());
                    if (e.isFromPast()) {
                        this.totalEventosPast++;
                    } else if (e.isFromFut()) {
                        this.totalEventosFut++;
                    } else if (e.isFromPres()) {
                        this.totalEventosPres++;
                    }
                }
            }
            // Numero total de eventos
            this.totalEventos = this.totalEventosPast + this.totalEventosPres + this.totalEventosFut;
        } catch (IOException e){
            System.out.println("Erro ao ler o ficheiro de eventos .txt!!!");
        }
    }

    /**
     * Metodo que serve para retornar as linhas a serem adcionadas a MIB
     * @return
     */
    public List<Variable[]> getTableRows() {
        return tableRows;
    }

    public long getTotalEventos() {
        return totalEventos;
    }

    public long getTotalEventosPast() {
        return totalEventosPast;
    }

    public long getTotalEventosPres() {
        return totalEventosPres;
    }

    public long getTotalEventosFut() {
        return totalEventosFut;
    }

    /**
     * Metodo que adiciona um valor a uma dada coluna e linha
     * @param variable
     */
    public void addRowValue(Variable variable) {
        if (tableRows.size() == currentRow) {
            tableRows.add(new Variable[COLUMNSSIZE]);
        }
        tableRows.get(currentRow)[currentCol] = variable;
        currentCol++;
        if (currentCol >= COLUMNSSIZE) {
            currentRow++;
            currentCol = 0;
        }
    }

}
