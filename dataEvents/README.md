# Trabalho 3 de Gestão de Redes

Autorer: Nelson Faria (A84727) e Miguel Oliveira (A83819)

ATENÇÃO: Recomenda-se o uso do IDE IntelliJ em todo o projeto por questões de facilidade!!!


# dataEvents


dependencias
=======

As dependências para cada projeto e que devem ser adicionadas às librarias são(correspondência dependencia -> módulo):

           $ javafx-sdk-15.0.1/lib    ->    WriteEvents
           $ snmp4j-3.4.4/dist        ->    AgentEvents
           $ snmp4j-agent-3.3.6/dist  ->    AgentEvents
           $ snmp4j-3.4.4/dist        ->    ManagerEvents
           $ javafx-sdk-15.0.1/lib    ->    ManagerEvents


WriteEvents
=======


Neste módulo aquilo que se salienta de mais importante é:

          - Escrita de novas linhas no ficheiro de eventos(dataEvents.txt) localizado em ./AgentEvents/dataEvents.txt.
          - Leitura do ficheiro de eventos(dataEvents.txt) para se saber quais os eventos disponíveis para eliminar


ATENÇÃO: Se usar intelliJ, adicione no menu (Run -> VM options) a seguinte frase:

          $ '--module-path $PROJECT DIR$/../dependencias/javafx-sdk-15.0.1/lib --add-modules javafx.controls,javafx.fxml’

AgentEvents
=======

Neste módulo aquilo que se salienta de mais importante é:

          - Lançamento do agente que ficará a correr na porta privada 3003.
          - Preenchimento e atulização das instâncias da dataEventsMIB!!

Nota: Deverá se passar como parâmetro também o protocolo de transporte, endereço e porta a que o agente ficará a escuta!!! Exemplo poderá ser:

           > udp:127.0.0.1/3003 tcp:127.0.0.1/3003


ManagerEvents
=======


Por fim, neste módulo aquilo que é mais importante é:

          - Envio do pedido ao agente que corre no aplicação AgentEvents (udp:localhost/3003)
          - Lançamento da interface gráfica onde se pode ver uma lista organizada por:
                  - Todos os eventos 
                  - Eventos que aconteceram/vão acontecer num(a) dado(a):
                               - Ano
                               - Mês
                               - Semana
                               - Dia

ATENÇÃO: Se usar intelliJ, adicione no menu (Run -> VM options) a seguinte frase:

          $ '--module-path $PROJECT DIR$/../dependencias/javafx-sdk-15.0.1/lib --add-modules javafx.controls,javafx.fxml’


DATA-EVENTS-MIB.txt
=======

Este ficheiro é a implementação em SMI da MIB usada no projeto e que foi gerado pela aplicação MIB Designer para posteriormente ser usado pelo AgenPro para gerar o código Java do agente!!!
