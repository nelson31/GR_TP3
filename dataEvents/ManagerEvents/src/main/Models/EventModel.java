package main.Models;

import java.util.Objects;

public class EventModel {

    private Integer index;
    private String ident;
    private String msg;
    private Integer anos;
    private Integer meses;
    private Integer semanas;
    private Integer dias;
    private Integer horas;
    private Integer minutos;


    public EventModel(Integer index, String ident, String msg, Integer anos, Integer meses, Integer semanas, Integer dias, Integer horas, Integer minutos) {
        this.index = index;
        this.ident = ident;
        this.msg = msg;
        this.anos = anos;
        this.meses = meses;
        this.semanas = semanas;
        this.dias = dias;
        this.horas = horas;
        this.minutos = minutos;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getAnos() {
        return anos;
    }

    public void setAnos(Integer anos) {
        this.anos = anos;
    }

    public Integer getMeses() {
        return meses;
    }

    public void setMeses(Integer meses) {
        this.meses = meses;
    }

    public Integer getSemanas() {
        return semanas;
    }

    public void setSemanas(Integer semanas) {
        this.semanas = semanas;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EventModel that = (EventModel) o;
        return index.equals(that.index);
    }

    @Override
    public int hashCode() {

        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "index=" + index +
                ", ident='" + ident + '\'' +
                ", msg='" + msg + '\'' +
                ", anos=" + anos +
                ", meses=" + meses +
                ", semanas=" + semanas +
                ", dias=" + dias +
                ", horas=" + horas +
                ", minutos=" + minutos +
                '}';
    }
}
