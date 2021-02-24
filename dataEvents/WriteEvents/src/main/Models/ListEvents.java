package main.Models;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ListEvents {

    // Lista de eventos
    private List<Event> eventos;

    public ListEvents(){

        this.eventos = new ArrayList<>();
    }

    public List<Event> getEventos() {
        return eventos;
    }

    /**
     * Metodo que a partir do nome do ficheiro de eventos,
     * preenche a lista de eventos
     * @param filename
     * @throws IOException
     */
    public void preencheEventos(String filename) throws IOException {

        // Eliminar os dados que possam estar na lista de eventos
        this.eventos.clear();

        List<String> linhas = Reader.read_lines(filename);

        int i = 1;
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
            Event evento = new Event(i,ident,msgpast,msgpres,msgfut,data,hora,datadel,horadel);
            this.eventos.add(evento);
            i++;
        }
    }

    /**
     * Adicionar evento individual
     * @param e
     */
    public void addEvento(Event e){
        this.eventos.add(e);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Event e : this.eventos){

            sb.append(e.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
