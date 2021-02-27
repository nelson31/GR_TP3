package main.Comunicator;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Event {

    /* Indice do evento */
    private int index;
    /* Identificação do Evento */
    private String identificacao;
    /* Mensagem para o evento */
    private String msg;
    /* Quantos anos faltam/passaram para o evento*/
    private int anos;
    /* Quantos meses faltam/passaram para o evento*/
    private int meses;
    /* Quantos semanas faltam/passaram para o evento*/
    private int semanas;
    /* Quantos dias faltam/passaram para o evento*/
    private int dias;
    /* Quantos horas faltam/passaram para o evento*/
    private int horas;
    /* Quantos minutos faltam/passaram para o evento*/
    private int minutos;
    /* Quantos anos faltam/passaram para apagar o evento*/
    private int anosDel;
    /* Quantos meses faltam/passaram para apagar o evento*/
    private int mesesDel;
    /* Quantos semanas faltam/passaram para apagar o evento*/
    private int semanasDel;
    /* Quantos dias faltam/passaram para apagar o evento*/
    private int diasDel;
    /* Quantos horas faltam/passaram para apagar o evento*/
    private int horasDel;
    /* Quantos minutos faltam/passaram para apagar o evento*/
    private int minutosDel;


    /**
     * CONSTRUTOR parametrizado de Event
     * @param index
     * @param identificacao
     * @param msg
     * @param anos
     * @param meses
     * @param semanas
     * @param dias
     * @param horas
     * @param minutos
     * @param anosDel
     * @param mesesDel
     * @param semanasDel
     * @param diasDel
     * @param horasDel
     * @param minutosDel
     */
    public Event(int index, String identificacao, String msg, int anos, int meses, int semanas, int dias, int horas, int minutos, int anosDel, int mesesDel, int semanasDel, int diasDel, int horasDel, int minutosDel) {
        this.index = index;
        this.identificacao = identificacao;
        this.msg = msg;
        this.anos = anos;
        this.meses = meses;
        this.semanas = semanas;
        this.dias = dias;
        this.horas = horas;
        this.minutos = minutos;
        this.anosDel = anosDel;
        this.mesesDel = mesesDel;
        this.semanasDel = semanasDel;
        this.diasDel = diasDel;
        this.horasDel = horasDel;
        this.minutosDel = minutosDel;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getAnos() {
        return anos;
    }

    public void setAnos(int anos) {
        this.anos = anos;
    }

    public int getMeses() {
        return meses;
    }

    public void setMeses(int meses) {
        this.meses = meses;
    }

    public int getSemanas() {
        return semanas;
    }

    public void setSemanas(int semanas) {
        this.semanas = semanas;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getAnosDel() {
        return anosDel;
    }

    public void setAnosDel(int anosDel) {
        this.anosDel = anosDel;
    }

    public int getMesesDel() {
        return mesesDel;
    }

    public void setMesesDel(int mesesDel) {
        this.mesesDel = mesesDel;
    }

    public int getSemanasDel() {
        return semanasDel;
    }

    public void setSemanasDel(int semanasDel) {
        this.semanasDel = semanasDel;
    }

    public int getDiasDel() {
        return diasDel;
    }

    public void setDiasDel(int diasDel) {
        this.diasDel = diasDel;
    }

    public int getHorasDel() {
        return horasDel;
    }

    public void setHorasDel(int horasDel) {
        this.horasDel = horasDel;
    }

    public int getMinutosDel() {
        return minutosDel;
    }

    public void setMinutosDel(int minutosDel) {
        this.minutosDel = minutosDel;
    }

    /**
     * Obter o ano do evento
     * @return
     */
    public String getAnoFormat(){

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime data = now.plus(this.anos, ChronoUnit.YEARS);
        data = data.plus(this.meses,ChronoUnit.MONTHS);
        data = data.plus(this.semanas,ChronoUnit.WEEKS);
        data = data.plus(this.dias,ChronoUnit.DAYS);
        data = data.plus(this.horas,ChronoUnit.HOURS);
        data = data.plus(this.minutos,ChronoUnit.MINUTES);
        data = data.plus(now.getSecond(),ChronoUnit.SECONDS);

        return String.valueOf(data.getYear());
    }

    /**
     * Obter o mes do evento
     * @return
     */
    public String getMesFormat(){

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime data = now.plus(this.anos, ChronoUnit.YEARS);
        data = data.plus(this.meses,ChronoUnit.MONTHS);
        data = data.plus(this.semanas,ChronoUnit.WEEKS);
        data = data.plus(this.dias,ChronoUnit.DAYS);
        data = data.plus(this.horas,ChronoUnit.HOURS);
        data = data.plus(this.minutos,ChronoUnit.MINUTES);
        data = data.plus(now.getSecond(),ChronoUnit.SECONDS);

        return data.getMonth() + "-" + data.getYear();
    }

    /**
     * Obter o semana do evento
     * @return
     */
    public String getSemanaFormat(){

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime data = now.plus(this.anos, ChronoUnit.YEARS);
        data = data.plus(this.meses,ChronoUnit.MONTHS);
        data = data.plus(this.semanas,ChronoUnit.WEEKS);
        data = data.plus(this.dias,ChronoUnit.DAYS);
        data = data.plus(this.horas,ChronoUnit.HOURS);
        data = data.plus(this.minutos,ChronoUnit.MINUTES);
        data = data.plus(now.getSecond(),ChronoUnit.SECONDS);

        LocalDateTime domingo = data.plusDays(-data.getDayOfWeek().getValue());
        LocalDateTime sabado = domingo.plusDays(6);

        return "de " + domingo.toLocalDate().toString() + " a " + sabado.toLocalDate().toString();
    }

    /**
     * Obter o dia do evento
     * @return
     */
    public String getDiaFormat(){

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime data = now.plus(this.anos, ChronoUnit.YEARS);
        data = data.plus(this.meses,ChronoUnit.MONTHS);
        data = data.plus(this.semanas,ChronoUnit.WEEKS);
        data = data.plus(this.dias,ChronoUnit.DAYS);
        data = data.plus(this.horas,ChronoUnit.HOURS);
        data = data.plus(this.minutos,ChronoUnit.MINUTES);
        data = data.plus(now.getSecond(),ChronoUnit.SECONDS);

        return String.valueOf(data.toLocalDate());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(index);
        sb.append(";\"");
        sb.append(identificacao);
        sb.append("\";\"");
        sb.append(msg);
        sb.append("\";");
        sb.append(anos);
        sb.append(";");
        sb.append(meses);
        sb.append(";");
        sb.append(semanas);
        sb.append(";");
        sb.append(dias);
        sb.append(";");
        sb.append(horas);
        sb.append(";");
        sb.append(minutos);
        sb.append(";");
        sb.append(anosDel);
        sb.append(";");
        sb.append(mesesDel);
        sb.append(";");
        sb.append(semanasDel);
        sb.append(";");
        sb.append(diasDel);
        sb.append(";");
        sb.append(horasDel);
        sb.append(";");
        sb.append(minutosDel);
        return sb.toString();
    }

}

