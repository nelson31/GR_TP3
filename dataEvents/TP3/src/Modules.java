 

//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import org.snmp4j.agent.mo.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.smi.OctetString;


//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class Modules implements MOGroup {

  private static final LogAdapter LOGGER = 
      LogFactory.getLogger(Modules.class);

  private DataEventsMib dataEventsMib;

  private MOFactory factory;

//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

  public Modules() {
   dataEventsMib = new DataEventsMib(); 
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

  public Modules(MOFactory factory) {
   dataEventsMib = new DataEventsMib(factory); 
//--AgentGen BEGIN=_CONSTRUCTOR
//--AgentGen END
  } 

  public void registerMOs(MOServer server, OctetString context) 
    throws DuplicateRegistrationException 
  {
	  dataEventsMib.registerMOs(server, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
	  dataEventsMib.unregisterMOs(server, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  public DataEventsMib getDataEventsMib() {
    return dataEventsMib;
  }


//--AgentGen BEGIN=_METHODS
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END

}

