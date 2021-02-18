package org.snmp4j.agent.eventsmib;

import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

import java.io.IOException;

public class AtualizaMIB {

    /* Objeto responsavel por conter as MIB*/
    private DataEventsMib mib;
    // Lista de Eventos
    private ListEvents le;

    /**
     * Construtor parametrizado de AtualizaMIB
     * @param modules
     */
    public AtualizaMIB(Modules modules) {
        this.mib = modules.getDataEventsMib();
        this.le = new ListEvents();
    }

    /**
     * Classe principal por onde se comeca a atualizacao da MIB
     * (Fazes de teste ainda!!!)
     * @throws IOException
     */
    public void atualizaMIB() throws IOException {

        // inteiros para cntar o número de eventos passados, presentes e futuros
        int eventosPast = 0, eventosPres = 0, eventosFut = 0;
        // Obtem os últimos dados atualizados da MIB
        this.le.obtemDados();
        // Iterar sobre os varios eventos e verificar pela existencia
        // de alteracoes
        int i = 1;
        for(Event e : this.le.getEventos()){

            atualizaTabela(i, e);
            // Contar os tipos de eventos
            if(e.isFromPast()){
                eventosPast++;
            } else if(e.isFromFut()){
                eventosFut++;
            } else if(e.isFromPres()){
                eventosPres++;
            }
            i++;
        }
        // Adicionar o total de eventos na MIB
        this.mib.getDataEventsTotal().setValue(new Counter32(this.le.getEventos().size()));
        // Adicionar o numero de eventos presentes, passados e futuros
        this.mib.getDataEventsTotalPast().setValue(new Counter32(eventosPast));
        this.mib.getDataEventsTotalPresent().setValue(new Counter32(eventosPres));
        this.mib.getDataEventsTotalFuture().setValue(new Counter32(eventosFut));
    }

    /**
     * Meto que serve para atualizar uma linha de uma tabela correspondente a um dado evento
     * @param i
     * @param e
     */
    private void atualizaTabela(int i, Event e) {

        // Linha da tabela
        DataEventsMib.DataEventsEntryRow er = this.mib.getDataEventsEntry().getModel().getRow(new OID(String.valueOf(i)));

        // Verificar se a linha existe na tabela
        if(er != null) {
            // atualizar os valores das varias colunas desta linha
            er.setDataEventsIndex(new Counter32(i));
            er.setDataEventsIdent(e.getIdentFormat());
            er.setDataEventsMsg(e.getMsgFormat());
            er.setDataEventsTimeYears(e.getAnosFormat());
            er.setDataEventsTimeMonths(e.getMesesFormat());
            er.setDataEventsTimeWeeks(e.getSemanasFormat());
            er.setDataEventsTimeDays(e.getDiasFormat());
            er.setDataEventsTimeHours(e.getHorasFormat());
            er.setDataEventsTimeMinutes(e.getMinutosFormat());
            er.setDataEventsTimeDeleteYears(e.getAnosDelFormat());
            er.setDataEventsTimeDeleteMonths(e.getMesesDelFormat());
            er.setDataEventsTimeDeleteWeeks(e.getSemanasDelFormat());
            er.setDataEventsTimeDeleteDays(e.getDiasDelFormat());
            er.setDataEventsTimeDeleteHours(e.getHorasDelFormat());
            er.setDataEventsTimeDeleteMinutes(e.getMinutosDelFormat());
        } else {
            Variable[] variaveis = new Variable[15];

            variaveis[0] = new Counter32(i);
            variaveis[1] = e.getIdentFormat();
            variaveis[2] = e.getMsgFormat();
            variaveis[3] = e.getAnosFormat();
            variaveis[4] = e.getMesesFormat();
            variaveis[5] = e.getSemanasFormat();
            variaveis[6] = e.getDiasFormat();
            variaveis[7] = e.getHorasFormat();
            variaveis[8] = e.getMinutosFormat();
            variaveis[9] = e.getAnosDelFormat();
            variaveis[10] = e.getMesesDelFormat();
            variaveis[11] = e.getSemanasDelFormat();
            variaveis[12] = e.getDiasDelFormat();
            variaveis[13] = e.getHorasDelFormat();
            variaveis[14] = e.getMinutosDelFormat();

            MOMutableTableModel model = (MOMutableTableModel) this.mib.getDataEventsEntry().getModel();
            // Adicionar a nova linha a tabela
            this.mib.adicionaLinhaTabela(i,model,variaveis);
        }
    }

}
