/*_############################################################################
  _## 
  _##  SNMP4J - TransportMappings.java  
  _## 
  _##  Copyright (C) 2003-2020  Frank Fock (SNMP4J.org)
  _##  
  _##  Licensed under the Apache License, Version 2.0 (the "License");
  _##  you may not use this file except in compliance with the License.
  _##  You may obtain a copy of the License at
  _##  
  _##      http://www.apache.org/licenses/LICENSE-2.0
  _##  
  _##  Unless required by applicable law or agreed to in writing, software
  _##  distributed under the License is distributed on an "AS IS" BASIS,
  _##  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  _##  See the License for the specific language governing permissions and
  _##  limitations under the License.
  _##  
  _##########################################################################*/
package org.snmp4j.transport;

import org.snmp4j.smi.*;
import org.snmp4j.TransportMapping;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Enumeration;

import org.snmp4j.log.*;

import java.lang.reflect.Constructor;

import org.snmp4j.SNMP4JSettings;

import java.lang.reflect.InvocationTargetException;

/**
 * The {@code TransportMappings} factory can be used to create a transport
 * mapping for an address class.
 *
 * @author Frank Fock
 * @version 2.4.1
 * @since 1.1
 */
public class TransportMappings {

    private static final LogAdapter logger = LogFactory.getLogger(TransportMappings.class);

    public static final String TRANSPORT_MAPPINGS =
            "org.snmp4j.transportMappings";
    private static final String TRANSPORT_MAPPINGS_DEFAULT =
            "transports.properties";

    private static TransportMappings instance = null;
    private Hashtable<String, Class<? extends TransportMapping<?>>> transportMappings = null;

    protected TransportMappings() {
    }

    /**
     * Returns the {@code TransportMappings} singleton.
     *
     * @return the {@code TransportMappings} instance.
     */
    public static TransportMappings getInstance() {
        if (instance == null) {
            instance = new TransportMappings();
        }
        return instance;
    }

    /**
     * Returns a {@code TransportMapping} instance that is initialized with
     * the supplied transport address.
     * If no such mapping exists, {@code null} is returned. To register
     * third party transport mappings, please set the system property
     * {@link #TRANSPORT_MAPPINGS} to a transport mappings registration file,
     * before calling this method for the first time.
     *
     * @param transportAddress
     *         an {@code Address} instance that the transport mapping to lookup
     *         has to support.
     * @param <A> an {@link Address} class that is the super class for the addresses supported by this transport mapping.
     *
     * @return a {@code TransportMapping} that supports the specified
     * {@code transportAddress} or {@code null} if such a mapping
     * cannot be found.
     */
    @SuppressWarnings("unchecked")
    public <A extends Address> TransportMapping<A> createTransportMapping(A transportAddress) {
        if (transportMappings == null) {
            registerTransportMappings();
        }
        Class<? extends TransportMapping<A>> c =
                (Class<? extends TransportMapping<A>>) transportMappings.get(transportAddress.getClass().getName());
        if (c == null) {
            return null;
        }
        Class<?>[] params = new Class<?>[1];
        params[0] = transportAddress.getClass();
        Constructor<? extends TransportMapping<A>> constructor;
        try {
            try {
                constructor = c.getConstructor(params);
                return constructor.newInstance(transportAddress);
            } catch (NoSuchMethodException nsme) {
                Constructor<? extends TransportMapping<A>>[] cs =
                        (Constructor<? extends TransportMapping<A>>[]) c.getConstructors();
                for (Constructor<? extends TransportMapping<A>> cons : cs) {
                    Class<?>[] params2 = cons.getParameterTypes();
                    if ((params2.length == 1) && (params2[0].isAssignableFrom(params[0]))) {
                        return cons.newInstance(transportAddress);
                    }
                }
                logger.error("NoSuchMethodException while instantiating " + c.getName(), nsme);
                return null;
            }
        } catch (InvocationTargetException ite) {
            if (logger.isDebugEnabled()) {
                ite.printStackTrace();
            }
            logger.error(ite);
            throw new RuntimeException(ite.getTargetException());
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                ex.printStackTrace();
            }
            logger.error(ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected synchronized void registerTransportMappings() {
        if (SNMP4JSettings.isExtensibilityEnabled()) {
            String transports =
                    System.getProperty(TRANSPORT_MAPPINGS, TRANSPORT_MAPPINGS_DEFAULT);
            InputStream is = TransportMappings.class.getResourceAsStream(transports);
            if (is == null) {
                throw new InternalError("Could not read '" + transports +
                        "' from classpath!");
            }
            Properties props = new Properties();
            try {
                props.load(is);
                Hashtable<String, Class<? extends TransportMapping<?>>> t = new Hashtable<>(props.size());
                for (String addressClassName : props.stringPropertyNames()) {
                    String className = props.getProperty(addressClassName);
                    try {
                        Class<? extends TransportMapping<?>> c =
                                (Class<? extends TransportMapping<?>>) Class.forName(className);
                        t.put(addressClassName, c);
                    } catch (ClassNotFoundException cnfe) {
                        logger.error(cnfe);
                    }
                }
                // atomic syntax registration
                transportMappings = t;
            } catch (IOException iox) {
                String txt = "Could not read '" + transports + "': " +
                        iox.getMessage();
                logger.error(txt);
                throw new InternalError(txt);
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    logger.warn(ex);
                }
            }
        } else {
            Hashtable<String, Class<? extends TransportMapping<?>>> t = new Hashtable<>(2);
            t.put(UdpAddress.class.getName(), DefaultUdpTransportMapping.class);
            t.put(TcpAddress.class.getName(), DefaultTcpTransportMapping.class);
            t.put(TlsAddress.class.getName(), TLSTM.class);
            t.put(DtlsAddress.class.getName(), DTLSTM.class);
            transportMappings = t;
        }
    }

}
