package Comunicator;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEvents {

    /* Variavel que a partir de um identificador de evento(index) nos
     * da o objeto correspondente a um evento */
    private Map<Integer,Event> eventos;
    // Classe que serve para comunicar com o agente
    private ComunicadorSNMP comSNMP;
    /* Variavel que guarda o total de eventos existentes*/
    private int total_eventos;
    /* Variavel que guarda o total de eventos passados existentes*/
    private int total_eventos_past;
    /* Variavel que guarda o total de eventos presentes existentes*/
    private int total_eventos_pres;
    /* Variavel que guarda o total de eventos futuros existentes*/
    private int total_eventos_fut;

    /**
     * COnstrutor de ListEvents
     */
    public ListEvents(){
        this.eventos = new HashMap<>();
    }

    // getters and setters

    public Map<Integer, Event> getEventos() {
        return eventos;
    }

    public void setEventos(Map<Integer, Event> eventos) {
        this.eventos = eventos;
    }

    public ComunicadorSNMP getComSNMP() {
        return comSNMP;
    }

    public void setComSNMP(ComunicadorSNMP comSNMP) {
        this.comSNMP = comSNMP;
    }

    public int getTotal_eventos() {
        return total_eventos;
    }

    public void setTotal_eventos(int total_eventos) {
        this.total_eventos = total_eventos;
    }

    public int getTotal_eventos_past() {
        return total_eventos_past;
    }

    public void setTotal_eventos_past(int total_eventos_past) {
        this.total_eventos_past = total_eventos_past;
    }

    public int getTotal_eventos_pres() {
        return total_eventos_pres;
    }

    public void setTotal_eventos_pres(int total_eventos_pres) {
        this.total_eventos_pres = total_eventos_pres;
    }

    public int getTotal_eventos_fut() {
        return total_eventos_fut;
    }

    public void setTotal_eventos_fut(int total_eventos_fut) {
        this.total_eventos_fut = total_eventos_fut;
    }

    /**
     * Metodo que vai pegar na listas de atributos de todos os processos
     * e vai criar os objetos representativos desses processos e adiciona-los
     * ao Map de processos, caso ainda nao existam lá
     * Lista <- [ [pid,name,path,cpu,mem],...]
     * @param tabresultados
     */
    public void setInfoEventos(List<List<String>> tabresultados, LocalDateTime data_hora, int total, int total_past, int total_pres, int total_fut) {

        // Constantes
        this.total_eventos = total;
        this.total_eventos_past = total_past;
        this.total_eventos_pres = total_pres;
        this.total_eventos_fut = total_fut;
        // Eventos
        for(List<String> linha : tabresultados){

            String ident, msg;
            try{
                ident = new String(octetStringToBytes(linha.get(1)),StandardCharsets.UTF_8);
                msg = new String(octetStringToBytes(linha.get(2)),StandardCharsets.UTF_8);
            } catch (NumberFormatException e){
                ident = linha.get(1);
                msg = linha.get(2);
            }

            Event e = new Event(
                    Integer.parseInt(linha.get(0)),
                    ident,
                    msg,
                    Integer.parseInt(linha.get(3)),
                    Integer.parseInt(linha.get(4)),
                    Integer.parseInt(linha.get(5)),
                    Integer.parseInt(linha.get(6)),
                    Integer.parseInt(linha.get(7)),
                    Integer.parseInt(linha.get(8)),
                    Integer.parseInt(linha.get(9)),
                    Integer.parseInt(linha.get(10)),
                    Integer.parseInt(linha.get(11)),
                    Integer.parseInt(linha.get(12)),
                    Integer.parseInt(linha.get(13)),
                    Integer.parseInt(linha.get(14)));

            this.eventos.put(Integer.parseInt(linha.get(0)),e);
        }
    }

    /**
     * Metodo que serve para dar inicio aos pedidos ao host em questao
     * dos objetos necessarios relativos aos varios processos em execução
     * @return
     * @throws RuntimeException
     */
    public void gerePedidos() throws RuntimeException{

        // Envia o pedido especificado nos oids e recebe uma tabela com o
        // resultado dos pedidos efetuados
        List<List<String>> tabresultados = comSNMP.enviaPedido();
        // Remover as constantes
        int total = Integer.parseInt(tabresultados.get(0).remove(0));
        int total_past = Integer.parseInt(tabresultados.get(0).remove(0));
        int total_pres = Integer.parseInt(tabresultados.get(0).remove(0));
        int total_fut = Integer.parseInt(tabresultados.get(0).remove(0));
        LocalDateTime data_hora = LocalDateTime.now();
        // Colocar a informação nos eventos respetivos
        this.setInfoEventos(tabresultados,data_hora,total,total_past,total_pres,total_fut);
    }

    /**
     * Metodo responsavel por produzor uma String representativa da
     * informaçao
     * @return
     */
    public String toString(){

        StringBuilder sb = new StringBuilder();

        sb.append("Total de Eventos = ").append(this.total_eventos).append("\n");
        sb.append("Total de Eventos Passados = ").append(this.total_eventos_past).append("\n");
        sb.append("Total de Eventos Presentes = ").append(this.total_eventos_pres).append("\n");
        sb.append("Total de Eventos Futuros = ").append(this.total_eventos_fut).append("\n");

        for(Event e : this.eventos.values()) {
            sb.append(e.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Metodo que a partir de uma OCTECTSTRING nos converte num array de inteiros
     * @param value_ipar
     * @return
     */
    public static byte[] octetStringToBytes(String value_ipar)
    {
        String[] bytes = value_ipar.split("[^0-9A-Fa-f]");

        byte[] result = new byte[bytes.length];

        for(int counter = 0; counter < bytes.length; counter++)
            result[counter] = (byte) Integer.parseInt(bytes[counter], 16);

        return result;
    }

}
