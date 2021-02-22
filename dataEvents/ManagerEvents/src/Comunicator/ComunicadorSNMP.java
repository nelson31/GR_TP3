package Comunicator;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Comunicador usada para sempre que é necessario comunicar
 * com o agente para obter dados de monitorização desta aplicaçao,
 * possui os metodos disponiveis para o fazer
 *
 * @version 2.0 (02/2021)
 */

public class ComunicadorSNMP {

    // Classe core do SNMP4J! É com esta classe que é possível enviar e
    // receber pdu's
    private Snmp snmp;
    // Classe Target que contem as definicoes do agente de forma a se poder
    // efetuar os pedidos
    private Target<?> alvo;

    /* Constante que define o OID em string do caminho para o dataEventsEntry */
    public static String OID_dataEventsEntry = ".1.3.6.1.4.1.8888.5.1";
    /* Constante que guarda o OID do numero total de eventos registados na MIB */
    public static String OID_dataEventsTotal = ".1.3.6.1.4.1.8888.1";
    /* Constante que guarda o OID do numero total de eventos passados registados na MIB */
    public static String OID_dataEventsTotalPast = ".1.3.6.1.4.1.8888.2";
    /* Constante que guarda o OID do numero total de eventos presentes registados na MIB */
    public static String OID_dataEventsTotalPres = ".1.3.6.1.4.1.8888.3";
    /* Constante que guarda o OID do numero total de eventos futuros registados na MIB */
    public static String OID_dataEventsTotalFut = ".1.3.6.1.4.1.8888.4";

    /**
     * Construtor da classe ComunicadorSNMP
     * @param snmp
     */
    public ComunicadorSNMP(Snmp snmp, Target<?> target){
        this.snmp = snmp;
        this.alvo = target;
    }


    /**
     * Metodo que cria todos os OID necessarios para obter a informaçao das
     * medicoes de performance do agente
     * @return oids
     */
    public OID[] formaOIDS() {

        OID[] oids = new OID[19];

        // Criar o OID de todos os eventos registados na MIB
        oids[0] = new OID(OID_dataEventsTotal);
        // Criar o OID de todos os eventos passados registados na MIB
        oids[1] = new OID(OID_dataEventsTotalPast);
        // Criar o OID de todos os eventos presentes registados na MIB
        oids[2] = new OID(OID_dataEventsTotalPres);
        // Criar o OID de todos os eventos futuros registados na MIB
        oids[3] = new OID(OID_dataEventsTotalFut);
        // Criar o OID das varias colunas da tabela
        for(int i = 1; i <= 15 ; i++){
            oids[i + 3] = new OID(OID_dataEventsEntry + "." + i);
        }

        return oids;
    }

    /**
     * Metodo que serve ppara enviar o pedido para o agente e receber a sua resposta
     * Este pedido é para os casos em que se pretende ir buscar dados de colunas
     * de tabelas da MIB (Foi feito de forma a ser sincrono)
     * Nota: Esta é uma versão usando TreeUtils para ir buscar o timestamp
     * na MIB do Agente
     * @return
     * @throws RuntimeException
     */
    public List<List<String>> enviaPedido()
            throws RuntimeException{

        OID[] oids = this.formaOIDS();
        DefaultPDUFactory dpdu = new DefaultPDUFactory(PDU.GETBULK);
        TreeUtils tu = new TreeUtils(this.snmp, dpdu);
        // Enviar o pedido para o alvo e obter uma lista de resultados
        List<TreeEvent> res_list = tu.walk(this.alvo, oids);
        // Lista de todos os atributos pedidos organizados por ordem em que
        // os processos aparecem na MIB
        List<List<String>> list_proc = new ArrayList<>();
        System.out.println(res_list);
        // Iterar sobre os vários processos
        for(TreeEvent tre : res_list) {

            if(tre.isError()) {
                throw new RuntimeException(tre.getErrorMessage());
            }
            VariableBinding[] vbs = tre.getVariableBindings();
            if(vbs.length > 0) {
                List<String> listAtrib = new ArrayList<>();
                // Iterar sobre os varios valores
                for (VariableBinding vb : vbs) {

                    listAtrib.add(vb.toValueString());
                }
                list_proc.add(listAtrib);
            }
        }
        return list_proc;
    }

    /**
     * Fazer parse da data e hora obtidas aquando do pedido de GETBULK
     * ao Agente SNMP a ser monitorizado
     * @param vb
     * @return timestamp
     */
    private LocalDateTime getLocalDateTime(VariableBinding vb) {
        int[] bytes = octetStringToBytes(vb.toValueString());
        int year = (bytes[0] * 256) + bytes[1];
        int month = bytes[2];
        int day = bytes[3];
        int hour = bytes[4];
        int minute = bytes[5];
        int second = bytes[6];
        int deci_sec = 0;
        if (bytes.length >= 8)
            deci_sec = bytes[7];
        return LocalDateTime.of(year, month, day,
                hour, minute, second, deci_sec * 100);
    }

    /**
     * Metodo que a partir de uma OCTECTSTRING nos converte num array de inteiros
     * @param value_ipar
     * @return
     */
    public static int[] octetStringToBytes(String value_ipar)
    {
        String[] bytes = value_ipar.split("[^0-9A-Fa-f]");

        int[] result = new int[bytes.length];

        for(int counter = 0; counter < bytes.length; counter++)
            result[counter] = Integer.parseInt(bytes[counter], 16);

        return result;
    }
}
