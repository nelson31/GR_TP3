package org.snmp4j.agent.eventsmib;

import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Variable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddtoMIB {

    // Linhas a adicionar à tabela
    private final List<Variable[]> tableRows = new ArrayList<>();
    // Linha atual
    private int currentRow;
    // Coluna atual
    private int currentCol;

    // Numero total de colunas
    private static int COLUMNSSIZE = 15;

    public AddtoMIB(){

        currentRow = 0;
        currentCol = 0;

        try {
            ListEvents le = new ListEvents();
            for(Event e : le.getEventos()){
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
            }
        } catch (IOException e){
            System.out.println("Erro ao ler o ficheiro de eventos .txt!!!");
        }
    }

    public List<Variable[]> getTableRows() {
        return tableRows;
    }

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
