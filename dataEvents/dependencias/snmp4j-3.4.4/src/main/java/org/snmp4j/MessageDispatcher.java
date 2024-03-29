/*_############################################################################
  _## 
  _##  SNMP4J - MessageDispatcher.java  
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

import org.snmp4j.event.CounterListener;
import org.snmp4j.smi.Address;
import org.snmp4j.mp.*;

import java.util.Collection;

import org.snmp4j.transport.TransportListener;
import org.snmp4j.transport.TransportType;

import java.nio.ByteBuffer;

/**
 * The {@code MessageDispatcher} interface defines common services of instances that process incoming SNMP messages and
 * dispatch them to interested {@link CommandResponder} instances. It also provides a service to send out outgoing SNMP
 * messages. A {@code MessageDispatcher} needs at least one {@link TransportMapping} and at least one {@link
 * MessageProcessingModel} in order to be able to process any messages.
 *
 * @author Frank Fock
 * @version 3.2.0
 */
public interface MessageDispatcher extends TransportListener {

    /**
     * Gets the next unique request ID. The returned ID is unique across the last 2^31-1 IDs generated by this message
     * dispatcher.
     *
     * @return an integer value in the range 1..2^31-1. The returned ID can be used to map responses to requests send
     * through this message dispatcher.
     * @since 1.1
     */
    int getNextRequestID();

    /**
     * Adds a {@link MessageProcessingModel} to the dispatcher. In order to support a specific SNMP protocol version,
     * the message dispatcher needs a message processing model to process messages before they can be dispatched.
     *
     * @param model
     *         a {@code MessageProcessingModel} instance.
     */
    void addMessageProcessingModel(MessageProcessingModel model);

    /**
     * Removes a previously added {@link MessageProcessingModel} from the dispatcher.
     *
     * @param model
     *         a {@code MessageProcessingModel} instance.
     */
    void removeMessageProcessingModel(MessageProcessingModel model);

    /**
     * Gets the {@code MessageProcessingModel} for the supplied message processing model ID.
     *
     * @param messageProcessingModel
     *         a message processing model ID (see {@link MessageProcessingModel#getID()}).
     *
     * @return a MessageProcessingModel instance if the ID is known, otherwise {@code null}
     */
    MessageProcessingModel getMessageProcessingModel(int messageProcessingModel);

    /**
     * Adds a {@link TransportMapping} to the dispatcher. The transport mapping is used to send and receive messages
     * to/from the network.
     *
     * @param transport
     *         a {@code TransportMapping} instance.
     */
    void addTransportMapping(TransportMapping<? extends Address> transport);

    /**
     * Removes a previously added {@link TransportMapping} from the dispatcher.
     *
     * @param transport
     *         a {@code TransportMapping} instance.
     *
     * @return the {@code TransportMapping} instance if it could be successfully removed, {@code null} otherwise.
     */
    TransportMapping<? extends Address> removeTransportMapping(TransportMapping<? extends Address> transport);

    /**
     * Adds a {@link CounterListener} to the dispatcher. The counter listener is informed about errors during message
     * dispatching.
     *
     * @param counterListener
     *         a {@code CounterListener} instance.
     *
     * @since 3.0
     */
    void addCounterListener(CounterListener counterListener);

    /**
     * Removes a previously added {@link CounterListener} from the dispatcher.
     *
     * @param counterListener
     *         a {@code CounterListener} instance.
     *
     * @return the {@code CounterListener} instance if it could be successfully removed, {@code null} otherwise.
     * @since 3.0
     */
    CounterListener removeCounterListener(CounterListener counterListener);


    /**
     * Gets the {@code Collection} of transport mappings in this message dispatcher.
     *
     * @return a Collection with the registered transport mappings.
     */
    Collection<TransportMapping<? extends Address>> getTransportMappings();

    /**
     * Returns a transport mapping that can handle the supplied address.
     *
     * @param destAddress
     *         an Address instance.
     * @param <A> the {@link Address} type to get a {@link TransportMapping} for.
     *
     * @return a {@code TransportMapping} instance that can be used to sent a SNMP message to {@code destAddress} or
     * {@code null} if such a transport mapping does not exists.
     * @since 1.6
     * @deprecated Use {@link #getTransport(Address, TransportType)} instead to make sure that the returned mapping
     * supports the intended SNMP application type usage (i.e. command/notification generator or command receiver).
     */
    @Deprecated
    <A extends Address> TransportMapping<? super A> getTransport(A destAddress);

    /**
     * Returns a transport mapping that can handle the supplied address.
     *
     * @param destAddress
     *         an Address instance.
     * @param <A> the {@link Address} type to get a {@link TransportMapping} for.
     * @param transportType
     *         specifies the SNMP application type that will use the returned transport mapping. {@code transportType}
     *         must be either {@link TransportType#receiver} or {@link TransportType#sender}.
     *         If {@link TransportType#any} is provided an {@link IllegalArgumentException} will be thrown.
     *
     * @return a {@code TransportMapping} instance that can be used to sent a SNMP message to {@code destAddress} or
     * {@code null} if such a transport mapping does not exists.
     * @since 1.6
     */
    <A extends Address> TransportMapping<? super A> getTransport(A destAddress, TransportType transportType);

    /**
     * Adds a {@link CommandResponder} instance to the message dispatcher. Successfully processed SNMP messages will be
     * presented to all command responder (in the order in which they have been added) until a responder uses the {@link
     * CommandResponderEvent#setProcessed(boolean processed)} to set the processed status of the event to {@code true}.
     *
     * @param listener
     *         a {@code CommandResponder} instance.
     */
    void addCommandResponder(CommandResponder listener);

    /**
     * Removes a previously added {@link CommandResponder} instance from the message dispatcher.
     *
     * @param listener
     *         a {@code CommandResponder} instance.
     */
    void removeCommandResponder(CommandResponder listener);

    /**
     * Sends a PDU to the supplied transport address.
     *
     * @param transportMapping
     *         the {@code TransportMapping} to be used to send the PDU. If {@code transportMapping} is {@code null} the
     *         message dispatcher will determine the appropriate transport mapping for the given transport address.
     * @param target
     *         the target which identifies, transport address, message processing model, security model, security name
     *         and level.
     * @param pdu
     *         the SNMP Protocol Data Unit
     * @param expectResponse
     *         {@code true} if a response is expected and a state reference should be saved (if needed for the supplied
     *         message processing model).
     * @param <A> the {@link Address} type.
     *
     * @return an {@code PduHandle} that uniquely identifies this request.
     * @throws MessageException
     *         if sending of the PDU failed.
     */
    <A extends Address> PduHandle sendPdu(TransportMapping<? super A> transportMapping, Target<A> target, PDU pdu,
                                          boolean expectResponse) throws MessageException;

    /**
     * Sends a PDU to the supplied transport address and returns the {@code PduHandle} that uniquely identifies the
     * request as response
     * <em>after</em> the request has been sent and optional, if a
     * {@link PduHandleCallback} is given, it returns also the {@code PduHandle} just <em>before</em> the request is
     * sent through the the callback interface.
     *
     * @param transportMapping
     *         the {@code TransportMapping} to be used to send the PDU. If {@code transportMapping} is {@code null} the
     *         message dispatcher will determine the appropriate transport mapping for the given transport address.
     * @param target
     *         the target which identifies, transport address, message processing model, security model, security name
     *         and level.
     * @param pdu
     *         the SNMP Protocol Data Unit
     * @param expectResponse
     *         {@code true} if a response is expected and a state reference should be saved (if needed for the supplied
     *         message processing model).
     * @param callback
     *         an optional callback instance that is informed (if not {@code null}) about the newly assigned PduHandle
     *         just before the message is sent out.
     * @param <A> the target {@link Address} type.
     *
     * @return an {@code PduHandle} that uniquely identifies this request.
     * @throws MessageException
     *         if sending of the PDU failed.
     */
    <A extends Address> PduHandle sendPdu(TransportMapping<? super A> transportMapping, Target<A> target, PDU pdu,
                                    boolean expectResponse, PduHandleCallback<PDU> callback) throws MessageException;

    /**
     * Sends a PDU to the supplied transport address. This method behaves like a call to {@link
     * #sendPdu(TransportMapping transportMapping, Target target, PDU pdu, boolean expectResponse)} with {@code
     * transportMapping} set to {@code null}.
     *
     * @param target
     *         the target which identifies, transport address, message processing model, security model, security name
     *         and level.
     * @param pdu
     *         the SNMP Protocol Data Unit
     * @param expectResponse
     *         {@code true} if a response is expected and a state reference should be saved (if needed for the supplied
     *         message processing model).
     * @param <A> the target {@link Address} type.
     *
     * @return an {@code PduHandle} that uniquely identifies this request.
     * @throws MessageException
     *         if sending of the PDU failed.
     */
    <A extends Address> PduHandle sendPdu(Target<A> target, PDU pdu, boolean expectResponse) throws MessageException;

    /**
     * Returns a response PDU to the sender of the corresponding request PDU.
     *
     * @param messageProcessingModel
     *         the message processing model, see {@link MessageProcessingModel}
     * @param securityModel
     *         the security model to use, see {@link org.snmp4j.security.SecurityModel}
     * @param securityName
     *         the security name
     * @param securityLevel
     *         the security level for the message to be returned, see {@link org.snmp4j.security.SecurityLevel}
     * @param pdu
     *         the protocol data unit ({@link PDU}) to send.
     * @param maxSizeResponseScopedPDU
     *         the maximum size agreed for the response scoped PDU.
     * @param stateReference
     *         the state reference to be used.
     * @param statusInformation
     *         the status information to be used.
     * @param <A> the {@link Address} type.
     *
     * @return an MP error status or {@link SnmpConstants#SNMP_MP_OK} if the operation was successful.
     * @throws MessageException
     *         if message processing fails with a fatal error.
     */
    <A extends Address> int returnResponsePdu(int messageProcessingModel, int securityModel, byte[] securityName,
                                              int securityLevel, PDU pdu, int maxSizeResponseScopedPDU,
                                              StateReference<A> stateReference, StatusInformation statusInformation)
            throws MessageException;

    /**
     * Process an incoming SNMP message. The message is processed and dispatched according to the message's content, the
     * message processing models, and the command responder available to the dispatcher.
     *
     * @param sourceTransport
     *         a {@code TransportMapping} instance denoting the transport that received the message and that will be
     *         used to send any responses to this message. The {@code sourceTransport} has to support the {@code
     *         incomingAddress}'s implementation class.
     * @param incomingAddress
     *         the {@code Address} from which the message has been received.
     * @param wholeMessage
     *         an {@code ByteBuffer} containing the received SNMP message.
     * @param tmStateReference
     *         the transport model state reference as defined by RFC 5590.
     * @param <A> the {@link Address} type.
     */
    <A extends Address> void processMessage(TransportMapping<? super A> sourceTransport, A incomingAddress,
                                            ByteBuffer wholeMessage, TransportStateReference tmStateReference);


    /**
     * Release any state references associated with the supplied {@code PduHandle} in the specified message processing
     * model.
     *
     * @param messageProcessingModel
     *         a message processing model ID.
     * @param pduHandle
     *         the {@code PduHandle} that identifies a confirmed class message.
     *
     * @see MessageProcessingModel
     */
    void releaseStateReference(int messageProcessingModel, PduHandle pduHandle);
}

