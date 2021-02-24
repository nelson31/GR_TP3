package main.Comunicator;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class Manager {

    // Classe core do SNMP4J! É com esta classe que é possível enviar e
    // receber pdu's
    private Snmp snmp;
    // Lists de Eventos registados num dado momento
    private ListEvents eventos;

    /* Endereço do host */
    private static String ENDERECO = "127.0.0.1";
    /* Protocolo de transporte usado pelo host (deve ser udp) */
    private static String TRANSPORTE = "udp";
    /* Porta usada pelo agente do host */
    private static int PORTA = 3003;
    /* Community String associada a este host */
    private static String COMMUNITY_STRING = "public";


    /**
     * Construtor de Manager
     */
    public Manager()
            throws IOException {

        // Criar lista de eventos
        this.eventos = new ListEvents();
        /* Usado como canal de comunicação com o host */
        TransportMapping trans = new DefaultUdpTransportMapping();
        this.snmp = new Snmp(trans);
        trans.listen();
    }

    public ListEvents getEventos() {
        return eventos;
    }

    /**
     * Metodo usado para definir todos os parametros necessarios
     * do alvo(host) a ser monitorizado
     * @return
     */
    private Target getTarget() {

        // Endereço do host target
        Address end_alvo = GenericAddress.parse(TRANSPORTE + ":" + ENDERECO + "/" + PORTA);
        CommunityTarget alvo = new CommunityTarget();
        // Neste momento so suporta versao 2
        alvo.setCommunity(new OctetString(COMMUNITY_STRING));
        alvo.setAddress(end_alvo);
        // Define o número de tentativas a serem realizadas antes
        // que o timeout de uma pedido seja atingido.
        alvo.setRetries(2);
        // Define o timeout para o pedido em milissegundos
        alvo.setTimeout(2000);
        alvo.setVersion(SnmpConstants.version2c);
        return alvo;
    }


    /**
     * Metodo por onde se inicia a execucao do manager
     */
    public void start(){

        /* Objeto que contem os metodos necessarios a comunicaao com o host */
        ComunicadorSNMP c = new ComunicadorSNMP(this.snmp,this.getTarget());
        this.eventos.setComSNMP(c);

        // Efetua os pedidos e obtem a lista dos processos em execucao
        this.eventos.gerePedidos();
    }

}
