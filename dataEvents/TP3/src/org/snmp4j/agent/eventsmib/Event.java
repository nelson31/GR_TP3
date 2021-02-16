package org.snmp4j.agent.eventsmib;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Event {

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
    public Event(String identificacao, String msgPast, String msgPres, String msgFut, LocalDate dataEvento, LocalTime horaEvento, LocalDate dataDelete, LocalTime horaDelete) {
        this.identificacao = identificacao;
        this.msgPast = msgPast;
        this.msgPres = msgPres;
        this.msgFut = msgFut;
        this.dataEvento = dataEvento;
        this.horaEvento = horaEvento;
        this.dataDelete = dataDelete;
        this.horaDelete = horaDelete;
    }

    @Override
    public String toString() {
        return "Event{" +
                "identificacao='" + identificacao + '\'' +
                ", msgPast='" + msgPast + '\'' +
                ", msgPres='" + msgPres + '\'' +
                ", msgFut='" + msgFut + '\'' +
                ", dataEvento=" + dataEvento +
                ", horaEvento=" + horaEvento +
                ", dataDelete=" + dataDelete +
                ", horaDelete=" + horaDelete +
                '}';
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

    /**
     * Metodo que nos retorna o resultado do campo identificação no
     * formato OCTETSTRING
     */
    public OctetString getIdentFormat(){

        OctetString o = new OctetString();

        o.setValue(this.identificacao);

        return o;
    }

    /**
     * Metodo que nos retorna o resultado do campo mensagem passada no
     * formato OCTETSTRING
     */
    public OctetString getMsgFormat(){

        OctetString o = new OctetString();

        if(isFromFut()) {
            o.setValue(this.msgFut);
        } else if (isFromPast()){
            o.setValue(this.msgPast);
        } else {
            o.setValue(this.msgPres);
        }

        return o;
    }

    /**
     * Metodo que nos retorna o numero de anos que faltam ou ja passaram
     * para o evento
     */
    public Integer32 getAnosFormat(){

        LocalDateTime atual = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(data, ChronoUnit.YEARS));

        return i;
    }

    /**
     * Metodo que nos retorna o numero de meses que faltam ou ja passaram
     * para o evento
     */
    public Integer32 getMesesFormat(){

        LocalDateTime aux = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime atual = aux.plusYears(aux.until(data, ChronoUnit.YEARS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(data, ChronoUnit.MONTHS));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 12);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de semanas que faltam ou ja passaram
     * para o evento
     */
    public Integer32 getSemanasFormat(){

        LocalDateTime aux = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime atual = aux.plusMonths(aux.until(data, ChronoUnit.MONTHS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(data, ChronoUnit.WEEKS));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 4);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de dias que faltam ou ja passaram
     * para o evento
     */
    public Integer32 getDiasFormat(){

        LocalDateTime aux = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime atual = aux.plusWeeks(aux.until(data, ChronoUnit.WEEKS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(data, ChronoUnit.DAYS));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 7);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de horas que faltam ou ja passaram
     * para o evento
     */
    public Integer32 getHorasFormat(){

        LocalDateTime aux = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime atual = aux.plusDays(aux.until(data, ChronoUnit.DAYS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(data, ChronoUnit.HOURS));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 24);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de minutos que faltam ou ja passaram
     * para o evento
     */
    public Integer32 getMinutosFormat(){

        LocalDateTime aux = LocalDateTime.now();
        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime atual = aux.plusHours(aux.until(data, ChronoUnit.HOURS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(data, ChronoUnit.MINUTES));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 60);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de anos que faltam
     * para apagar o registo deste evento da MIB
     */
    public Integer32 getAnosDelFormat(){

        LocalDateTime data = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime datadel = LocalDateTime.of(this.dataDelete,this.horaDelete);

        Integer32 i = new Integer32();

        i.setValue((int) data.until(datadel, ChronoUnit.YEARS));

        return i;
    }

    /**
     * Metodo que nos retorna o numero de meses que faltam
     * para apagar o evento da MIB
     */
    public Integer32 getMesesDelFormat(){

        LocalDateTime aux = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime datadel = LocalDateTime.of(this.dataDelete,this.horaDelete);
        LocalDateTime atual = aux.plusYears(aux.until(datadel, ChronoUnit.YEARS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(datadel, ChronoUnit.MONTHS));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 12);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de semanas que faltam
     * para apagar o evento da MIB
     */
    public Integer32 getSemanasDelFormat(){

        LocalDateTime aux = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime datadel = LocalDateTime.of(this.dataDelete,this.horaDelete);
        LocalDateTime atual = aux.plusMonths(aux.until(datadel, ChronoUnit.MONTHS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(datadel, ChronoUnit.WEEKS));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 4);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de dias que faltam
     * para apagar o evento da MIB
     */
    public Integer32 getDiasDelFormat(){

        LocalDateTime aux = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime datadel = LocalDateTime.of(this.dataDelete,this.horaDelete);
        LocalDateTime atual = aux.plusWeeks(aux.until(datadel, ChronoUnit.WEEKS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(datadel, ChronoUnit.DAYS));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 7);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de horas que faltam
     * para apagar o evento da MIB
     */
    public Integer32 getHorasDelFormat(){

        LocalDateTime aux = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime datadel = LocalDateTime.of(this.dataDelete,this.horaDelete);
        LocalDateTime atual = aux.plusDays(aux.until(datadel, ChronoUnit.DAYS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(datadel, ChronoUnit.HOURS));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 24);
        }

        return i;
    }

    /**
     * Metodo que nos retorna o numero de minutos que faltam
     * para apagar o evento da MIB
     */
    public Integer32 getMinutosDelFormat(){

        LocalDateTime aux = LocalDateTime.of(this.dataEvento,this.horaEvento);
        LocalDateTime datadel = LocalDateTime.of(this.dataDelete,this.horaDelete);
        LocalDateTime atual = aux.plusHours(aux.until(datadel, ChronoUnit.HOURS));

        Integer32 i = new Integer32();

        i.setValue((int) atual.until(datadel, ChronoUnit.MINUTES));
        if(i.getValue() < 0){
            i.setValue(i.getValue() + 60);
        }

        return i;
    }
}
