package org.snmp4j.agent.eventsmib;

//|:AgenPro|=_BEGIN
//|AgenPro:|

import java.io.*;
import java.text.*;
import java.util.*;

import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.cfg.*;
import org.snmp4j.agent.io.*;
import org.snmp4j.agent.io.prop.*;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.*;
import org.snmp4j.util.*;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.JavaLogFactory;
import org.snmp4j.agent.mo.util.VariableProvider;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.agent.request.RequestStatus;
import org.snmp4j.agent.request.Request;
import org.snmp4j.agent.request.SubRequestIterator;
import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.agent.mo.MOFactory;

//|:AgenPro|=import
//|AgenPro:|


public class Agent implements VariableProvider {

  static {
    LogFactory.setLogFactory(new JavaLogFactory());
  }
  
  private static final String DEFAULT_CL_PARAMETERS = "-c[s{=Agent.cfg}] -bc[s{=Agent.bc}]";
  private static final String DEFAULT_CL_COMMANDS = "#address[s{=udp:127.0.0.1/161}<(udp|tcp):.*[/[0-9]+]?>] ..";

  private LogAdapter logger = LogFactory.getLogger(Agent.class);

  protected AgentConfigManager agent;
  protected MOServer server;
  private String configFile;
  private File bootCounterFile;

  // supported MIBs
  protected Modules modules;


  public Agent(Map args) {
    configFile = (String)((List)args.get("c")).get(0);
    bootCounterFile = new File((String)((List)args.get("bc")).get(0));

    server = new DefaultMOServer();
    MOServer[] moServers = new MOServer[] { server };
    InputStream configInputStream =
        Agent.class.getResourceAsStream("AgentConfig.properties");
    if (configInputStream == null) {
      System.err.println("Unable to load AgentConfig.properties. File not found. Aborting");
      System.exit(1);
    }
    final Properties props = new Properties();
    try {
      props.load(configInputStream);
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    MOInputFactory configurationFactory = new MOInputFactory() {
      public MOInput createMOInput() {
        return new PropertyMOInput(props, Agent.this);
      }
    };
    MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
    addListenAddresses(messageDispatcher, (List)args.get("address"));
    agent = new AgentConfigManager(new OctetString(MPv3.createLocalEngineID()),
                                   messageDispatcher,
                                   null,
                                   moServers,
                                   ThreadPool.create("Agent", 3),
                                   configurationFactory,
                                   new DefaultMOPersistenceProvider(moServers,
                                                                    configFile),
                                   new EngineBootsCounterFile(bootCounterFile));
  }

  protected void addListenAddresses(MessageDispatcher md, List addresses) {
    if (addresses == null || addresses.isEmpty()) {
      return;
    }
    for (Iterator it = addresses.iterator(); it.hasNext();) {
      Address address = GenericAddress.parse((String)it.next());
      TransportMapping tm =
          TransportMappings.getInstance().createTransportMapping(address);
      if (tm != null) {
        md.addTransportMapping(tm);
      }
      else {
        logger.warn("No transport mapping available for address '"+
                    address+"'.");
      }
    }
  }

  public void run() {
    // initialize agent before registering our own modules
    agent.initialize();
    /*try {
      OID dataEventsTable = new OID(".1.3.6.1.4.1.8888.5.1");
      MOTableBuilder builder = new MOTableBuilder(dataEventsTable)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_OCTET_STRING, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_OCTET_STRING, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              .addColumnType(SMIConstants.SYNTAX_INTEGER32, MOAccessImpl.ACCESS_READ_ONLY)
              // Normally you would begin loop over you two domain objects here
              .addRowValue(new Integer32(1))
              .addRowValue(new OctetString("00:00:00:00:01"))
              .addRowValue(new OctetString("00:00:00:00:02"))
              .addRowValue(new Integer32(24))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(24))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(24))
              .addRowValue(new Integer32(24))
              //next row
              .addRowValue(new Integer32(2))
              .addRowValue(new OctetString("00:00:00:00:01"))
              .addRowValue(new OctetString("00:00:00:00:02"))
              .addRowValue(new Integer32(24))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(24))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(1500))
              .addRowValue(new Integer32(24))
              .addRowValue(new Integer32(24));
      server.register(builder.build(), null);
    } catch (DuplicateRegistrationException e){
      e.printStackTrace();
    }*/
    // this requires sysUpTime to be available.
    registerMIBs();
    // add proxy forwarder
    agent.setupProxyForwarder();
    // now continue agent setup and launch it.
    agent.run();
    /* SO PARA TESTE
    try {
      ListEvents le = new ListEvents();
      for(Event e : le.getEventos()){
        //System.out.println("Ident: " + e.getIdentFormat());
        System.out.println("Anos: " + e.getAnosFormat().getValue());
        System.out.println("Meses: " + e.getMesesFormat().getValue());
        System.out.println("Semanas: " + e.getSemanasFormat().getValue());
        System.out.println("Dias: " + e.getDiasFormat().getValue());
        System.out.println("Horas: " + e.getHorasFormat().getValue());
        System.out.println("Minutos: " + e.getMinutosFormat().getValue());
      }
    } catch (IOException e){
      System.out.println("Ficheiro não existente!!");
    }
    */
    //System.out.println(this.getVariable(".1.3.6.1.4.1.8888.3.0"));
  }

  /**
   * Get the {@link MOFactory} that creates the various MOs (MIB Objects).
   * @return
   *    a {@link DefaultMOFactory} instance by default.
   */
  protected MOFactory getFactory() {
    //|:AgenPro|=factory
	return DefaultMOFactory.getInstance();
    //|AgenPro:|
  }

  /**
   * Register your own MIB modules in the specified context of the agent.
   * The {@link MOFactory} provided to the <code>Modules</code> constructor
   * is returned by {@link #getFactory()}.
   */
  protected void registerMIBs()
  {
    if (modules == null) {
      modules = new Modules(getFactory());
    }
    try {
      modules.registerMOs(server, null);
      //|:AgenPro|=registerContext
      //|AgenPro:|
    }
    catch (DuplicateRegistrationException drex) {
      logger.error("Duplicate registration: "+drex.getMessage()+ "" +
                   " MIB object registration may be incomplete!", drex);
    }
  }


  public Variable getVariable(String name) {
    OID oid;
    OctetString context = null;
    int pos = name.indexOf(':');
    if (pos >= 0) {
      context = new OctetString(name.substring(0, pos));
      oid = new OID(name.substring(pos+1, name.length()));
    }
    else {
      oid = new OID(name);
    }
    final DefaultMOContextScope scope =
        new DefaultMOContextScope(context, oid, true, oid, true);
    MOQuery query = new DefaultMOQuery(scope, false, this);
    ManagedObject mo = server.lookup(query);
    if (mo != null) {
      final VariableBinding vb = new VariableBinding(oid);
      final RequestStatus status = new RequestStatus();
      SubRequest req = new SubRequest() {
        private boolean completed;
        private MOQuery query;

        public boolean hasError() {
          return false;
        }

        public void setErrorStatus(int errorStatus) {
          status.setErrorStatus(errorStatus);
        }

        public int getErrorStatus() {
          return status.getErrorStatus();
        }

        public RequestStatus getStatus() {
          return status;
        }

        public MOScope getScope() {
          return scope;
        }

        public VariableBinding getVariableBinding() {
          return vb;
        }

        public Request getRequest() {
          return null;
        }

        public Object getUndoValue() {
          return null;
        }

        public void setUndoValue(Object undoInformation) {
        }

        public void completed() {
          completed = true;
        }

        public boolean isComplete() {
          return completed;
        }

        public void setTargetMO(ManagedObject managedObject) {
        }

        public ManagedObject getTargetMO() {
          return null;
        }

        public int getIndex() {
          return 0;
        }

        public void setQuery(MOQuery query) {
          this.query = query;
        }

        public MOQuery getQuery() {
          return query;
        }

        public SubRequestIterator repetitions() {
          return null;
        }

        public void updateNextRepetition() {
        }

        public Object getUserObject() {
          return null;
        }

        public void setUserObject(Object userObject) {
        }

      };
      mo.get(req);
      return vb.getVariable();
    }
    return null;
  }

  public AgentConfigManager getAgentConfigManager() {
    return agent;
  }
  
  private static void defaultMain(String args[]) {
    ArgumentParser parser =
        new ArgumentParser(DEFAULT_CL_PARAMETERS,
                           DEFAULT_CL_COMMANDS);
    Map commandLineParameters = null;
    try {
      commandLineParameters = parser.parse(args);
      Agent agent = new Agent(commandLineParameters);
      // Add all available security protocols (e.g. SHA,MD5,DES,AES,3DES,..)
      SecurityProtocols.getInstance().addDefaultProtocols();
      // configure system group:
      // Set system description:
      // sampleAgent.agent.getSysDescr().setValue("My system description".getBytes());
      // Set system OID (= OID of the AGENT-CAPABILITIES statement describing
      // the implemented MIB objects of this agent:
      // sampleAgent.agent.getSysOID().setValue("1.3.1.6.1.4.1....");
      // Set the system services
      // sampleAgent.agent.getSysServices().setValue(72);
      agent.run();
      // Fire a sample named trap generated by AgenPro:
      // agent.modules.getIfMib().fireLinkDownIf10001(agent.server, 
      // 	 agent.agent.getNotificationOriginator(), null, null);
    }
    catch (ParseException ex) {
      System.err.println(ex.getMessage());
    }
  }

  /**
   * Runs a sample agent with a default configuration defined by
   * <code>AgentConfig.properties</code>. A sample command line is:
   * <pre>
   * -c Agent.cfg -bc Agent.bc udp:127.0.0.1/4700 tcp:127.0.0.1/4700
   * </pre>
   *
   * @param args
   *    the command line arguments defining at least the listen addresses.
   *    The format is <code>-c[s{=Agent.cfg}] -bc[s{=Agent.bc}]
   *    #address[s<(udp|tcp):.*[/[0-9]+]?>] ..</code>. For the format
   *    description see {@link ArgumentParser}.
   */
  public static void main(String[] args) {
    //|:AgenPro|=main
	defaultMain(args);
    //|AgenPro:|
  }
}
