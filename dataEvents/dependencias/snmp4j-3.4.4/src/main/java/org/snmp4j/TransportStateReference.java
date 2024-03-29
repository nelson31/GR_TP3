/*_############################################################################
  _## 
  _##  SNMP4J - TransportStateReference.java  
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

package org.snmp4j;

import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;

import java.io.Serializable;

/**
 * The {@code TransportStateReference} class holds information defined by RFC 5343 for the tmStateReference ASI
 * elements. Objects of this class are cached by security aware {@link TransportMapping}s and transport aware {@link
 * org.snmp4j.security.SecurityModel}s.
 *
 * @author Frank Fock
 * @version 2.0
 * @since 2.0
 */
public class TransportStateReference implements Serializable {

    private static final long serialVersionUID = 3903263675608991496L;

    private TransportMapping<?> transport;
    private Address address;
    private OctetString securityName;
    private SecurityLevel requestedSecurityLevel;
    private SecurityLevel transportSecurityLevel;
    private boolean sameSecurity;
    private Object sessionID;
    private Target<?> target;

    public TransportStateReference(TransportMapping<?> transport,
                                   Address address,
                                   OctetString securityName,
                                   SecurityLevel requestedSecurityLevel,
                                   SecurityLevel transportSecurityLevel,
                                   boolean sameSecurity,
                                   Object sessionID) {
        this.transport = transport;
        this.address = address;
        this.securityName = securityName;
        this.requestedSecurityLevel = requestedSecurityLevel;
        this.transportSecurityLevel = transportSecurityLevel;
        this.sameSecurity = sameSecurity;
        this.sessionID = sessionID;
    }

    public TransportStateReference(TransportMapping<?> transport,
                                   Address address,
                                   OctetString securityName,
                                   SecurityLevel requestedSecurityLevel,
                                   SecurityLevel transportSecurityLevel,
                                   boolean sameSecurity,
                                   Object sessionID,
                                   Target<?> target) {
        this(transport, address, securityName, requestedSecurityLevel, transportSecurityLevel,
                sameSecurity, sessionID);
        this.target = target;
    }

    public void setTransport(TransportMapping<?> transport) {
        this.transport = transport;
    }

    public TransportMapping<?> getTransport() {
        return transport;
    }

    public Address getAddress() {
        return address;
    }

    public OctetString getSecurityName() {
        return securityName;
    }

    public SecurityLevel getRequestedSecurityLevel() {
        return requestedSecurityLevel;
    }

    public SecurityLevel getTransportSecurityLevel() {
        return transportSecurityLevel;
    }

    public boolean isSameSecurity() {
        return sameSecurity;
    }

    public Object getSessionID() {
        return sessionID;
    }

    public void setSecurityName(OctetString securityName) {
        this.securityName = securityName;
    }

    public void setRequestedSecurityLevel(SecurityLevel requestedSecurityLevel) {
        this.requestedSecurityLevel = requestedSecurityLevel;
    }

    public void setTransportSecurityLevel(SecurityLevel transportSecurityLevel) {
        this.transportSecurityLevel = transportSecurityLevel;
    }

    public void setSameSecurity(boolean sameSecurity) {
        this.sameSecurity = sameSecurity;
    }

    /**
     * Get the {@link CertifiedIdentity} instance that contains additional security information necessary to send a
     * message using {@link org.snmp4j.transport.TLSTM}.
     * @return
     *    the certified identity instance.
     * @deprecated Use {@link #getTarget()} instead and cast to {@link CertifiedIdentity} if possible.
     */
    public CertifiedIdentity getCertifiedIdentity() {
        if (target instanceof CertifiedIdentity) {
            return (CertifiedIdentity) target;
        }
        return null;
    }

    /**
     * Check if the given {@link TransportStateReference} has {@link CertifiedIdentity} information.
     * @param transportStateReference
     *    a transport state reference instance.
     * @return
     *    {@code true} if {@code transportStateReference} is not {@code null} and its {@link #getTarget()} is an
     *    instance of {@link CertifiedIdentity}.
     * @since 3.4.0
     */
    public static boolean hasCertifiedIdentity(TransportStateReference transportStateReference) {
        return (transportStateReference != null) && (transportStateReference.getTarget() instanceof CertifiedIdentity);
    }

    /**
     * Get the target instance that contains additional security information necessary to send a message.
     * @return
     *    associated (optional) target instance which has been used to send a message.
     * @since 3.4.0
     */
    public Target<?> getTarget() {
        return target;
    }

    /**
     * Checks if transport, address, securityName and transportSecurityLevel are valid (not null).
     *
     * @return {@code true} if the above fields are not {@code null}.
     */
    public boolean isTransportSecurityValid() {
        return ((transport != null) && (address != null) && (securityName != null) &&
                (transportSecurityLevel != null));
    }

    @Override
    public String toString() {
        return "TransportStateReference[" +
                "transport=" + transport +
                ", address=" + address +
                ", securityName=" + securityName +
                ", requestedSecurityLevel=" + requestedSecurityLevel +
                ", transportSecurityLevel=" + transportSecurityLevel +
                ", sameSecurity=" + sameSecurity +
                ", sessionID=" + sessionID +
                ", target=" + target +
                ']';
    }

}
