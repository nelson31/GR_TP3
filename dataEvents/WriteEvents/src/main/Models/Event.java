package main.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Event {

    /* Indice do evento */
    private int index;
    /* Identificação do Evento */
    private String identificacao;
    /* Mensagem para o evento caso este seja passado */
    private String msgPast;
    /* Mensagem para o evento caso este seja presente */
    private String msgPres;
    /* Mensagem para o evento caso este seja futuro */
    private String msgFut;
    /* Data em que o evento se vai realizar */
    private LocalDate dataEvento;
    /* Hora em que o evento se vai realizar */
    private LocalTime horaEvento;
    /* Data em que o evento deve ser apagado da MIB */
    private LocalDate dataDelete;
    /* Hora em que o evento deve ser apagado da MIB */
    private LocalTime horaDelete;

    /**
     * CONSTRUTOR parametrizado de Event
     * @param identificacao
     * @param msgPast
     * @param msgPres
     * @param msgFut
     * @param dataEvento
     * @param horaEvento
     * @param dataDelete
     * @param horaDelete
     */
    public Event(int index, String identificacao, String msgPast, String msgPres, String msgFut, LocalDate dataEvento, LocalTime horaEvento, LocalDate dataDelete, LocalTime horaDelete) {
        this.index = index;
        this.identificacao = identificacao;
        this.msgPast = msgPast;
        this.msgPres = msgPres;
        this.msgFut = msgFut;
        this.dataEvento = dataEvento;
        this.horaEvento = horaEvento;
        this.dataDelete = dataDelete;
        this.horaDelete = horaDelete;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public LocalTime getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(LocalTime horaEvento) {
        this.horaEvento = horaEvento;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(identificacao);
        sb.append("\";\"");
        sb.append(msgPast);
        sb.append("\";\"");
        sb.append(msgPres);
        sb.append("\";\"");
        sb.append(msgFut);
        sb.append("\";");
        sb.append(dataEvento);
        sb.append(";");
        sb.append(horaEvento);
        sb.append(";");
        sb.append(dataDelete);
        sb.append(";");
        sb.append(horaDelete);
        return sb.toString();
    }

    /**
     * Metodo que diz se este evento é passado
     * (Ver melhor isto!!)
     */
    public boolean isFromPast(){

        LocalDateTime atual = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);

        return atual.until(data, ChronoUnit.DAYS) < 0;
    }

    /**
     * Metodo que diz se este evento é presente
     * (Ver melhor isto!!)
     */
    public boolean isFromPres(){

        LocalDateTime atual = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);

        return atual.until(data, ChronoUnit.DAYS) == 0;
    }

    /**
     * Metodo que diz se este evento é futuro
     * (Ver melhor isto!!)
     */
    public boolean isFromFut(){

        LocalDateTime atual = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);

        return atual.until(data, ChronoUnit.DAYS) > 0;
    }

}
