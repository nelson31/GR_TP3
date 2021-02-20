package org.snmp4j.agent.eventsmib;

//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import org.snmp4j.smi.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;


//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class DataEventsMib 
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup 
//--AgentGen BEGIN=_IMPLEMENTS
//--AgentGen END
{

  private static final LogAdapter LOGGER = 
      LogFactory.getLogger(DataEventsMib.class);

//--AgentGen BEGIN=_STATIC
//--AgentGen END

  // Factory
  private MOFactory moFactory = 
    DefaultMOFactory.getInstance();

  // Constants 

  /**
   * OID of this MIB module for usage which can be 
   * used for its identification.
   */
  public static final OID oidDataEventsMib =
    new OID(new int[] { 1,3,6,1,4,1,8888 });

  // Identities
  // Scalars
  public static final OID oidDataEventsTotal = 
    new OID(new int[] { 1,3,6,1,4,1,8888,1,0 });
  public static final OID oidDataEventsTotalPast = 
    new OID(new int[] { 1,3,6,1,4,1,8888,2,0 });
  public static final OID oidDataEventsTotalPresent = 
    new OID(new int[] { 1,3,6,1,4,1,8888,3,0 });
  public static final OID oidDataEventsTotalFuture = 
    new OID(new int[] { 1,3,6,1,4,1,8888,4,0 });
  // Tables

  // Notifications

  // Enumerations




  // TextualConventions

  // Scalars
  private MOScalar<Counter32> dataEventsTotal;
  private MOScalar<Counter32> dataEventsTotalPast;
  private MOScalar<Counter32> dataEventsTotalPresent;
  private MOScalar<Counter32> dataEventsTotalFuture;

  // Tables
  public static final OID oidDataEventsEntry = 
    new OID(new int[] { 1,3,6,1,4,1,8888,5,1 });

  // Index OID definitions
  public static final OID oidDataEventsIndex =
    new OID(new int[] { 1,3,6,1,4,1,8888,5,1,1 });

  // Column TC definitions for dataEventsEntry:
    
    // Column sub-identifier definitions for dataEventsEntry:
    public static final int colDataEventsIndex = 1;
    public static final int colDataEventsIdent = 2;
    public static final int colDataEventsMsg = 3;
    public static final int colDataEventsTimeYears = 4;
    public static final int colDataEventsTimeMonths = 5;
    public static final int colDataEventsTimeWeeks = 6;
    public static final int colDataEventsTimeDays = 7;
    public static final int colDataEventsTimeHours = 8;
    public static final int colDataEventsTimeMinutes = 9;
    public static final int colDataEventsTimeDeleteYears = 10;
    public static final int colDataEventsTimeDeleteMonths = 11;
    public static final int colDataEventsTimeDeleteWeeks = 12;
    public static final int colDataEventsTimeDeleteDays = 13;
    public static final int colDataEventsTimeDeleteHours = 14;
    public static final int colDataEventsTimeDeleteMinutes = 15;

    // Column index definitions for dataEventsEntry:
    public static final int idxDataEventsIndex = 0;
    public static final int idxDataEventsIdent = 1;
    public static final int idxDataEventsMsg = 2;
    public static final int idxDataEventsTimeYears = 3;
    public static final int idxDataEventsTimeMonths = 4;
    public static final int idxDataEventsTimeWeeks = 5;
    public static final int idxDataEventsTimeDays = 6;
    public static final int idxDataEventsTimeHours = 7;
    public static final int idxDataEventsTimeMinutes = 8;
    public static final int idxDataEventsTimeDeleteYears = 9;
    public static final int idxDataEventsTimeDeleteMonths = 10;
    public static final int idxDataEventsTimeDeleteWeeks = 11;
    public static final int idxDataEventsTimeDeleteDays = 12;
    public static final int idxDataEventsTimeDeleteHours = 13;
    public static final int idxDataEventsTimeDeleteMinutes = 14;

  private MOTableSubIndex[] dataEventsEntryIndexes;
  private MOTableIndex dataEventsEntryIndex;
  private AddtoMIB addMIB;

    @SuppressWarnings(value={"rawtypes"})
    private MOTable<DataEventsEntryRow, MOColumn,
        MOTableModel<DataEventsEntryRow>> dataEventsEntry;
    private MOTableModel<DataEventsEntryRow> dataEventsEntryModel;


//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

  /**
   * Constructs a DataEventsMib instance without actually creating its
   * <code>ManagedObject</code> instances. This has to be done in a
   * sub-class constructor or after construction by calling 
   * {@link #createMO(MOFactory moFactory)}. 
   */
  protected DataEventsMib() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

  /**
   * Constructs a DataEventsMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied 
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   */
  public DataEventsMib(MOFactory moFactory) {
  	this();
    //--AgentGen BEGIN=_FACTORYCONSTRUCTOR::factoryWrapper
    //--AgentGen END
  	this.moFactory = moFactory;
    createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
//--AgentGen END
  }

//--AgentGen BEGIN=_CONSTRUCTORS
//--AgentGen END

  /**
   * Create the ManagedObjects defined for this MIB module
   * using the specified {@link MOFactory}.
   * @param moFactory
   *    the <code>MOFactory</code> instance to use for object 
   *    creation.
   */
  protected void createMO(MOFactory moFactory) {
    // Criar o objeto que contem os dados a adicionar a MIB
    this.addMIB = new AddtoMIB();
    addTCsToFactory(moFactory);
    dataEventsTotal = 
      moFactory.createScalar(oidDataEventsTotal,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32(this.addMIB.getTotalEventos()));
    dataEventsTotalPast = 
      moFactory.createScalar(oidDataEventsTotalPast,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32(this.addMIB.getTotalEventosPast()));
    dataEventsTotalPresent = 
      moFactory.createScalar(oidDataEventsTotalPresent,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32(this.addMIB.getTotalEventosPres()));
    dataEventsTotalFuture = 
      moFactory.createScalar(oidDataEventsTotalFuture,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32(this.addMIB.getTotalEventosFut()));
    createDataEventsEntry(moFactory);
  }

  public MOScalar<Counter32> getDataEventsTotal() {
    return dataEventsTotal;
  }
  public MOScalar<Counter32> getDataEventsTotalPast() {
    return dataEventsTotalPast;
  }
  public MOScalar<Counter32> getDataEventsTotalPresent() {
    return dataEventsTotalPresent;
  }
  public MOScalar<Counter32> getDataEventsTotalFuture() {
    return dataEventsTotalFuture;
  }


    @SuppressWarnings(value={"rawtypes"})
    public MOTable<DataEventsEntryRow,MOColumn,MOTableModel<DataEventsEntryRow>> getDataEventsEntry() {
        return dataEventsEntry;
    }


    @SuppressWarnings(value={"unchecked"})
    private void createDataEventsEntry(MOFactory moFactory) {
        // Index definition
    dataEventsEntryIndexes = 
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidDataEventsIndex, 
                               SMIConstants.SYNTAX_INTEGER, 1, 1)
    };

    dataEventsEntryIndex = 
      moFactory.createIndex(dataEventsEntryIndexes,
                            false,
                            new MOTableIndexValidator() {
      public boolean isValidIndex(OID index) {
        boolean isValidIndex = true;
    //--AgentGen BEGIN=dataEventsEntry::isValidIndex
    //--AgentGen END
        return isValidIndex;
      }
    });

        // Columns
        MOColumn<?>[] dataEventsEntryColumns = new MOColumn<?>[15];
        dataEventsEntryColumns[idxDataEventsIndex] =
        moFactory.createColumn(colDataEventsIndex,
                               SMIConstants.SYNTAX_COUNTER32,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsIdent] =
        moFactory.createColumn(colDataEventsIdent,
                               SMIConstants.SYNTAX_OCTET_STRING,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsMsg] =
        moFactory.createColumn(colDataEventsMsg,
                               SMIConstants.SYNTAX_OCTET_STRING,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeYears] =
        moFactory.createColumn(colDataEventsTimeYears,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeMonths] =
        moFactory.createColumn(colDataEventsTimeMonths,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeWeeks] =
        moFactory.createColumn(colDataEventsTimeWeeks,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeDays] =
        moFactory.createColumn(colDataEventsTimeDays,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeHours] =
        moFactory.createColumn(colDataEventsTimeHours,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeMinutes] =
        moFactory.createColumn(colDataEventsTimeMinutes,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeDeleteYears] =
        moFactory.createColumn(colDataEventsTimeDeleteYears,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeDeleteMonths] =
        moFactory.createColumn(colDataEventsTimeDeleteMonths,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeDeleteWeeks] =
        moFactory.createColumn(colDataEventsTimeDeleteWeeks,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeDeleteDays] =
        moFactory.createColumn(colDataEventsTimeDeleteDays,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeDeleteHours] =
        moFactory.createColumn(colDataEventsTimeDeleteHours,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        dataEventsEntryColumns[idxDataEventsTimeDeleteMinutes] =
        moFactory.createColumn(colDataEventsTimeDeleteMinutes,
                               SMIConstants.SYNTAX_INTEGER,
                               moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
        // Table model
        dataEventsEntryModel =
            moFactory.createTableModel(oidDataEventsEntry,
                                       dataEventsEntryIndex,
                                       dataEventsEntryColumns);
        ((MOMutableTableModel<DataEventsEntryRow>)dataEventsEntryModel).setRowFactory(
            new DataEventsEntryRowFactory());
        dataEventsEntry =
            moFactory.createTable(oidDataEventsEntry,
                                  dataEventsEntryIndex,
                                  dataEventsEntryColumns,
                                  dataEventsEntryModel);
        // Add Rows(Eu adicionei isto)
        int i = 1;
        MOMutableTableModel model = (MOMutableTableModel) dataEventsEntry.getModel();

        for (Variable[] variables : this.addMIB.getTableRows()) {
            adicionaLinhaTabela(i, model, variables);
            i++;
        }
  }

    public void adicionaLinhaTabela(int i, MOMutableTableModel model, Variable[] variables) {
        model.addRow(new DataEventsEntryRow(new OID(String.valueOf(i)),
                variables));
    }

    public void removeLinhaTabela(int i, MOMutableTableModel model){
      model.removeRow(new OID(String.valueOf(i)));
    }

    public void removeLinhasExcesso(int tamanhoEventos){
        MOMutableTableModel model = (MOMutableTableModel) this.getDataEventsEntry().getModel();
        int tamanhoTabela = model.getRowCount();
        int diferenca = tamanhoTabela - tamanhoEventos;
        for(int i = 0 ; i < diferenca ; i++){
            this.removeLinhaTabela(tamanhoTabela - i,model);
        }
    }


    public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException 
  {
    // Scalar Objects
    server.register(this.dataEventsTotal, context);
    server.register(this.dataEventsTotalPast, context);
    server.register(this.dataEventsTotalPresent, context);
    server.register(this.dataEventsTotalFuture, context);
    server.register(this.dataEventsEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.dataEventsTotal, context);
    server.unregister(this.dataEventsTotalPast, context);
    server.unregister(this.dataEventsTotalPresent, context);
    server.unregister(this.dataEventsTotalFuture, context);
    server.unregister(this.dataEventsEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications

  // Scalars

  // Value Validators


  // Rows and Factories

  public class DataEventsEntryRow extends DefaultMOMutableRow2PC {

    //--AgentGen BEGIN=dataEventsEntry::RowMembers
    //--AgentGen END

    public DataEventsEntryRow(OID index, Variable[] values) {
      super(index, values);
    //--AgentGen BEGIN=dataEventsEntry::RowConstructor
    //--AgentGen END
    }
    
    public Counter32 getDataEventsIndex() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsIndex
    //--AgentGen END
      return (Counter32) super.getValue(idxDataEventsIndex);
    }  
    
    public void setDataEventsIndex(Counter32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsIndex
    //--AgentGen END
      super.setValue(idxDataEventsIndex, newColValue);
    }
    
    public OctetString getDataEventsIdent() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsIdent
    //--AgentGen END
      return (OctetString) super.getValue(idxDataEventsIdent);
    }  
    
    public void setDataEventsIdent(OctetString newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsIdent
    //--AgentGen END
      super.setValue(idxDataEventsIdent, newColValue);
    }
    
    public OctetString getDataEventsMsg() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsMsg
    //--AgentGen END
      return (OctetString) super.getValue(idxDataEventsMsg);
    }  
    
    public void setDataEventsMsg(OctetString newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsMsg
    //--AgentGen END
      super.setValue(idxDataEventsMsg, newColValue);
    }
    
    public Integer32 getDataEventsTimeYears() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeYears
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeYears);
    }  
    
    public void setDataEventsTimeYears(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeYears
    //--AgentGen END
      super.setValue(idxDataEventsTimeYears, newColValue);
    }
    
    public Integer32 getDataEventsTimeMonths() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeMonths
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeMonths);
    }  
    
    public void setDataEventsTimeMonths(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeMonths
    //--AgentGen END
      super.setValue(idxDataEventsTimeMonths, newColValue);
    }
    
    public Integer32 getDataEventsTimeWeeks() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeWeeks
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeWeeks);
    }  
    
    public void setDataEventsTimeWeeks(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeWeeks
    //--AgentGen END
      super.setValue(idxDataEventsTimeWeeks, newColValue);
    }
    
    public Integer32 getDataEventsTimeDays() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeDays
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeDays);
    }  
    
    public void setDataEventsTimeDays(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeDays
    //--AgentGen END
      super.setValue(idxDataEventsTimeDays, newColValue);
    }
    
    public Integer32 getDataEventsTimeHours() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeHours
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeHours);
    }  
    
    public void setDataEventsTimeHours(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeHours
    //--AgentGen END
      super.setValue(idxDataEventsTimeHours, newColValue);
    }
    
    public Integer32 getDataEventsTimeMinutes() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeMinutes
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeMinutes);
    }  
    
    public void setDataEventsTimeMinutes(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeMinutes
    //--AgentGen END
      super.setValue(idxDataEventsTimeMinutes, newColValue);
    }
    
    public Integer32 getDataEventsTimeDeleteYears() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeDeleteYears
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeDeleteYears);
    }  
    
    public void setDataEventsTimeDeleteYears(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeDeleteYears
    //--AgentGen END
      super.setValue(idxDataEventsTimeDeleteYears, newColValue);
    }
    
    public Integer32 getDataEventsTimeDeleteMonths() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeDeleteMonths
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeDeleteMonths);
    }  
    
    public void setDataEventsTimeDeleteMonths(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeDeleteMonths
    //--AgentGen END
      super.setValue(idxDataEventsTimeDeleteMonths, newColValue);
    }
    
    public Integer32 getDataEventsTimeDeleteWeeks() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeDeleteWeeks
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeDeleteWeeks);
    }  
    
    public void setDataEventsTimeDeleteWeeks(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeDeleteWeeks
    //--AgentGen END
      super.setValue(idxDataEventsTimeDeleteWeeks, newColValue);
    }
    
    public Integer32 getDataEventsTimeDeleteDays() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeDeleteDays
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeDeleteDays);
    }  
    
    public void setDataEventsTimeDeleteDays(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeDeleteDays
    //--AgentGen END
      super.setValue(idxDataEventsTimeDeleteDays, newColValue);
    }
    
    public Integer32 getDataEventsTimeDeleteHours() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeDeleteHours
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeDeleteHours);
    }  
    
    public void setDataEventsTimeDeleteHours(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeDeleteHours
    //--AgentGen END
      super.setValue(idxDataEventsTimeDeleteHours, newColValue);
    }
    
    public Integer32 getDataEventsTimeDeleteMinutes() {
    //--AgentGen BEGIN=dataEventsEntry::getDataEventsTimeDeleteMinutes
    //--AgentGen END
      return (Integer32) super.getValue(idxDataEventsTimeDeleteMinutes);
    }  
    
    public void setDataEventsTimeDeleteMinutes(Integer32 newColValue) {
    //--AgentGen BEGIN=dataEventsEntry::setDataEventsTimeDeleteMinutes
    //--AgentGen END
      super.setValue(idxDataEventsTimeDeleteMinutes, newColValue);
    }
    
    public Variable getValue(int column) {
    //--AgentGen BEGIN=dataEventsEntry::RowGetValue
    //--AgentGen END
        switch(column) {
            case idxDataEventsIndex:
        	    return getDataEventsIndex();
            case idxDataEventsIdent:
        	    return getDataEventsIdent();
            case idxDataEventsMsg:
        	    return getDataEventsMsg();
            case idxDataEventsTimeYears:
        	    return getDataEventsTimeYears();
            case idxDataEventsTimeMonths:
        	    return getDataEventsTimeMonths();
            case idxDataEventsTimeWeeks:
        	    return getDataEventsTimeWeeks();
            case idxDataEventsTimeDays:
        	    return getDataEventsTimeDays();
            case idxDataEventsTimeHours:
        	    return getDataEventsTimeHours();
            case idxDataEventsTimeMinutes:
        	    return getDataEventsTimeMinutes();
            case idxDataEventsTimeDeleteYears:
        	    return getDataEventsTimeDeleteYears();
            case idxDataEventsTimeDeleteMonths:
        	    return getDataEventsTimeDeleteMonths();
            case idxDataEventsTimeDeleteWeeks:
        	    return getDataEventsTimeDeleteWeeks();
            case idxDataEventsTimeDeleteDays:
        	    return getDataEventsTimeDeleteDays();
            case idxDataEventsTimeDeleteHours:
        	    return getDataEventsTimeDeleteHours();
            case idxDataEventsTimeDeleteMinutes:
        	    return getDataEventsTimeDeleteMinutes();
            default:
                return super.getValue(column);
        }
    }
    
    public void setValue(int column, Variable value) {
    //--AgentGen BEGIN=dataEventsEntry::RowSetValue
    //--AgentGen END
        switch(column) {
            case idxDataEventsIndex:
        	    setDataEventsIndex((Counter32)value);
        	    break;
            case idxDataEventsIdent:
        	    setDataEventsIdent((OctetString)value);
        	    break;
            case idxDataEventsMsg:
        	    setDataEventsMsg((OctetString)value);
        	    break;
            case idxDataEventsTimeYears:
        	    setDataEventsTimeYears((Integer32)value);
        	    break;
            case idxDataEventsTimeMonths:
        	    setDataEventsTimeMonths((Integer32)value);
        	    break;
            case idxDataEventsTimeWeeks:
        	    setDataEventsTimeWeeks((Integer32)value);
        	    break;
            case idxDataEventsTimeDays:
        	    setDataEventsTimeDays((Integer32)value);
        	    break;
            case idxDataEventsTimeHours:
        	    setDataEventsTimeHours((Integer32)value);
        	    break;
            case idxDataEventsTimeMinutes:
        	    setDataEventsTimeMinutes((Integer32)value);
        	    break;
            case idxDataEventsTimeDeleteYears:
        	    setDataEventsTimeDeleteYears((Integer32)value);
        	    break;
            case idxDataEventsTimeDeleteMonths:
        	    setDataEventsTimeDeleteMonths((Integer32)value);
        	    break;
            case idxDataEventsTimeDeleteWeeks:
        	    setDataEventsTimeDeleteWeeks((Integer32)value);
        	    break;
            case idxDataEventsTimeDeleteDays:
        	    setDataEventsTimeDeleteDays((Integer32)value);
        	    break;
            case idxDataEventsTimeDeleteHours:
        	    setDataEventsTimeDeleteHours((Integer32)value);
        	    break;
            case idxDataEventsTimeDeleteMinutes:
        	    setDataEventsTimeDeleteMinutes((Integer32)value);
        	    break;
            default:
                super.setValue(column, value);
            }
        }

    //--AgentGen BEGIN=dataEventsEntry::Row
    //--AgentGen END
    }
  
    public class DataEventsEntryRowFactory implements MOTableRowFactory<DataEventsEntryRow>
    {
        public synchronized DataEventsEntryRow createRow(OID index, Variable[] values)
            throws UnsupportedOperationException
        {
            DataEventsEntryRow row = new DataEventsEntryRow(index, values);
    //--AgentGen BEGIN=dataEventsEntry::createRow
    //--AgentGen END
            return row;
        }
    
        public synchronized void freeRow(DataEventsEntryRow row) {
    //--AgentGen BEGIN=dataEventsEntry::freeRow
    //--AgentGen END
        }

    //--AgentGen BEGIN=dataEventsEntry::RowFactory
    //--AgentGen END
    }


//--AgentGen BEGIN=_METHODS
//--AgentGen END

  // Textual Definitions of MIB module DataEventsMib
  protected void addTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
//--AgentGen END

  // Textual Definitions of other MIB modules
  public void addImportedTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_END
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


