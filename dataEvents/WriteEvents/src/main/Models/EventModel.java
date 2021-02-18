package main.Models;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventModel {

    private Integer index;
    private String ident;
    private LocalDate data;
    private LocalTime hora;

    public EventModel(Integer index, String ident, LocalDate data, LocalTime hora) {
        this.index = index;
        this.ident = ident;
        this.data = data;
        this.hora = hora;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}
