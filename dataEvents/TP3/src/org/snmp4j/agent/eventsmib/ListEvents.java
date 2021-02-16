package org.snmp4j.agent.eventsmib;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ListEvents {

    /* Lista de Eventos presentes no ficheiro .txt */
    private List<Event> eventos;

    /* Nome do ficheiro de eventos */
    private static String FILE_NAME = "dataEvents.txt";

    public ListEvents() throws IOException {

        this.eventos = new ArrayList<>();

        // Ler as linahs do ficheiro de eventos
        List<String> linhas = ReadFile.read_lines(FILE_NAME);
        // Iterar pelas varias linhas e adicionar os respetivos campos
        for (String linha : linhas){
            // Fazer parse ao varios campos de cada linha
            String[] campos = linha.split(";");
            // <ident>;<msgpast>;<msgpres>;<msgfut>;data;hora;datadel;horadel
            String ident = campos[0].replace("\"","");
            String msgpast = campos[1].replace("\"","");
            String msgpres = campos[2].replace("\"","");
            String msgfut = campos[3].replace("\"","");
            LocalDate data = LocalDate.parse(campos[4]);
            LocalTime hora = LocalTime.parse(campos[5]);
            LocalDate datadel = LocalDate.parse(campos[6]);
            LocalTime horadel = LocalTime.parse(campos[7]);
            Event evento = new Event(ident,msgpast,msgpres,msgfut,data,hora,datadel,horadel);
            this.eventos.add(evento);
        }
    }

    public List<Event> getEventos() {
        return eventos;
    }

    public void setEventos(List<Event> eventos) {
        this.eventos = eventos;
    }

    @Override
    public String toString() {
        return "ListEvents{" +
                "eventos=" + eventos +
                '}';
    }
}
