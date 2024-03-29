/*_############################################################################
  _## 
  _##  SNMP4J - TreeUtils.java  
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
package org.snmp4j.util;

import java.io.*;
import java.util.*;

import org.snmp4j.*;
import org.snmp4j.event.*;
import org.snmp4j.log.*;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;

public class TreeUtils extends AbstractSnmpUtility {

    private static final LogAdapter logger = LogFactory.getLogger(TreeUtils.class);

    private int maxRepetitions = 10;
    private boolean ignoreLexicographicOrder;

    /**
     * Creates a {@code TreeUtils} instance. The created instance is thread safe as long as the supplied
     * {@code Session} and
     * {@code PDUFactory} are thread safe.
     *
     * @param snmpSession
     *         a SNMP {@code Session} instance.
     * @param pduFactory
     *         a {@code PDUFactory} instance that creates the PDU that are used by this instance to retrieve MIB
     *         tree data using GETBULK/GETNEXT operations.
     */
    public TreeUtils(Session snmpSession, PDUFactory pduFactory) {
        super(snmpSession, pduFactory);
    }

    /**
     * Gets a subtree with GETNEXT (SNMPv1) or GETBULK (SNMP2c, SNMPv3) operations from the specified target
     * synchronously.
     *
     * @param target
     *         a {@code Target} that specifies the target command responder including its network transport
     *         address.
     * @param rootOID
     *         the OID that specifies the root of the sub-tree to retrieve (not included).
     *
     * @return a possibly empty List of {@code TreeEvent} instances where each instance carries zero or more values
     * (or an error condition) in depth-first-order.
     */
    public List<TreeEvent> getSubtree(Target<?> target, OID rootOID) {
        List<TreeEvent> l = new LinkedList<>();
        TreeListener listener = new InternalTreeListener(l);
        synchronized (listener) {
            OID[] rootOIDs = new OID[]{rootOID};
            walk(target, rootOIDs, null, listener);
            try {
                if (!listener.isFinished()) {
                    listener.wait();
                }
            } catch (InterruptedException ex) {
                logger.warn("Tree retrieval interrupted: " + ex.getMessage());
            }
        }
        return l;
    }

    /**
     * Walks a subtree with GETNEXT (SNMPv1) or GETBULK (SNMP2c, SNMPv3) operations from the specified target
     * asynchronously.
     *
     * @param target
     *         a {@code Target} that specifies the target command responder including its network transport
     *         address.
     * @param rootOIDs
     *         the OIDs which specify the subtrees to walk. Each OID defines a sub-tree that is walked. The walk ends if
     *         (a) an SNMP error occurs, (b) all returned variable bindings for an iteration contain an exception value
     *         (i.e., {@link Null#endOfMibView}) or for each rootOIDs element, the returned VariableBinding's OID has
     *         not the same prefix, (c) a VariableBinding out of lexicographic order is returned.
     *
     * @return a possibly empty List of {@code TreeEvent} instances where each instance carries zero or
     * {@code rootOIDs.length} values.
     * @since 2.1
     */
    public List<TreeEvent> walk(Target<?> target, OID[] rootOIDs) {
        List<TreeEvent> l = new LinkedList<>();
        TreeListener listener = new InternalTreeListener(l);
        synchronized (listener) {
            walk(target, rootOIDs, null, listener);
            try {
                if (!listener.isFinished()) {
                    listener.wait();
                }
            } catch (InterruptedException ex) {
                logger.warn("Tree retrieval interrupted: " + ex.getMessage());
            }
        }
        return l;
    }


    /**
     * Gets a subtree with GETNEXT (SNMPv1) or GETBULK (SNMP2c, SNMPv3) operations from the specified target
     * asynchronously.
     *
     * @param target
     *         a {@code Target} that specifies the target command responder including its network transport
     *         address.
     * @param rootOID
     *         the OID that specifies the root of the sub-tree to retrieve (not included).
     * @param userObject
     *         an optional user object that will be transparently handed over to the supplied
     *         {@code TreeListener}.
     * @param listener
     *         the {@code TreeListener} that processes the {@link TreeEvent}s generated by this method. Each event
     *         object may carry zero or more object instances from the sub-tree in depth-first-order.
     */
    public void getSubtree(Target<?> target, OID rootOID, Object userObject, TreeListener listener) {
        walk(target, new OID[]{rootOID}, userObject, listener);
    }

    /**
     * Walks a subtree with GETNEXT (SNMPv1) or GETBULK (SNMP2c, SNMPv3) operations from the specified target
     * asynchronously.
     *
     * @param target
     *         a {@code Target} that specifies the target command responder including its network transport
     *         address.
     * @param rootOIDs
     *         the OIDs which specify the subtrees to walk. Each OID defines a sub-tree that is walked. The walk ends if
     *         (a) an SNMP error occurs, (b) all returned variable bindings for an iteration contain an exception value
     *         (i.e., {@link Null#endOfMibView}) or for each rootOIDs element, the returned VariableBinding's OID has
     *         not the same prefix, (c) a VariableBinding out of lexicographic order is returned.
     * @param userObject
     *         an optional user object that will be transparently handed over to the supplied
     *         {@code TreeListener}.
     * @param listener
     *         the {@code TreeListener} that processes the {@link TreeEvent}s generated by this method. Each event
     *         object may carry zero or more object instances from the sub-tree in depth-first-order if rootOIDs has a
     *         single element. If it has more than one element, then each {@link TreeEvent} contains the variable
     *         bindings of each iteration.
     *
     * @since 2.1
     */
    public void walk(Target<?> target, OID[] rootOIDs, Object userObject, TreeListener listener) {
        PDU request = pduFactory.createPDU(target);
        for (OID oid : rootOIDs) {
            request.add(new VariableBinding(oid));
        }
        if (target.getVersion() == SnmpConstants.version1) {
            request.setType(PDU.GETNEXT);
        } else if (request.getType() != PDU.GETNEXT) {
            request.setType(PDU.GETBULK);
            request.setMaxRepetitions(maxRepetitions);
        }
        TreeRequest treeRequest =
                new TreeRequest(listener, rootOIDs, target, userObject, request);
        treeRequest.send();
    }

    /**
     * Sets the maximum number of the variable bindings per {@code TreeEvent} returned by this instance.
     *
     * @param maxRepetitions
     *         the maximum repetitions used for GETBULK requests. For SNMPv1 this values has no effect (it is then
     *         implicitly one).
     */
    public void setMaxRepetitions(int maxRepetitions) {
        this.maxRepetitions = maxRepetitions;
    }

    /**
     * Set the ignore lexicographic order errors flage value.
     *
     * @param ignoreLexicographicOrder
     *         {@code true} to ignore lexicographic order errors,
     *         {@code false} otherwise (default).
     *
     * @since 1.10.1
     */
    public void setIgnoreLexicographicOrder(boolean ignoreLexicographicOrder) {
        this.ignoreLexicographicOrder = ignoreLexicographicOrder;
    }

    /**
     * Gets the maximum number of the variable bindings per {@code TreeEvent} returned by this instance.
     *
     * @return the maximum repetitions used for GETBULK requests. For SNMPv1 this values has no effect (it is then
     * implicitly one).
     */
    public int getMaxRepetitions() {
        return maxRepetitions;
    }

    /**
     * Return the ignore lexicographic order errors flage value.
     *
     * @return {@code true} if lexicographic order errors are ignored,
     * {@code false} otherwise (default).
     * @since 1.10.1
     */
    public boolean isIgnoreLexicographicOrder() {
        return ignoreLexicographicOrder;
    }

    class TreeRequest implements ResponseListener {

        private TreeListener listener;
        private Object userObject;
        private PDU request;
        private OID[] rootOIDs;
        private Target<?> target;

        public TreeRequest(TreeListener listener, OID[] rootOIDs, Target<?> target, Object userObject, PDU request) {
            this.listener = listener;
            this.userObject = userObject;
            this.request = request;
            this.rootOIDs = rootOIDs;
            this.target = target;
        }

        public void send() {
            try {
                session.send(request, target, null, this);
            } catch (IOException iox) {
                listener.finished(new TreeEvent(this, userObject, iox));
            }
        }

        public <A extends Address> void onResponse(ResponseEvent<A> event) {
            session.cancel(event.getRequest(), this);
            PDU respPDU = event.getResponse();
            if (respPDU == null) {
                listener.finished(new TreeEvent(this, userObject,
                        RetrievalEvent.STATUS_TIMEOUT));
            } else if (respPDU.getErrorStatus() != 0) {
                if (target.getVersion() == SnmpConstants.version1 && respPDU.getErrorStatus() == PDU.noSuchName) {
                    listener.finished(new TreeEvent(this, userObject, new VariableBinding[0]));
                }
                listener.finished(new TreeEvent(this, userObject,
                        respPDU.getErrorStatus()));
            } else if (respPDU.getType() == PDU.REPORT) {
                listener.finished(new TreeEvent(this, userObject, respPDU));
            } else {
                List<VariableBinding> l = new ArrayList<VariableBinding>(respPDU.size());
                List<OID> lastOIDs = null;
                if (!ignoreLexicographicOrder) {
                    lastOIDs = new ArrayList<OID>(request.size());
                    for (int i = 0; i < request.size(); i++) {
                        lastOIDs.add(request.get(i).getOid());
                    }
                }
                boolean finished = false;
                for (int i = 0; ((!finished) || (i % rootOIDs.length > 0)) && (i < respPDU.size()); i++) {
                    int r = i % rootOIDs.length;
                    VariableBinding vb = respPDU.get(i);
                    if ((vb.getOid() == null) ||
                            (vb.getOid().size() < rootOIDs[r].size()) ||
                            (rootOIDs[r].leftMostCompare(rootOIDs[r].size(), vb.getOid()) != 0)) {
                        finished = true;
                    } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
                        finished = true;
                    } else if (!ignoreLexicographicOrder && (lastOIDs != null) &&
                            (vb.getOid().compareTo(lastOIDs.get(r)) <= 0)) {
                        listener.finished(new TreeEvent(this, userObject,
                                RetrievalEvent.STATUS_WRONG_ORDER));
                        finished = true;
                        break;
                    } else {
                        finished = false;
                        if (lastOIDs != null) {
                            lastOIDs.set(r, vb.getOid());
                        }
                        l.add(vb);
                    }
                    if ((rootOIDs.length > 1) && (i + 1) % rootOIDs.length == 0) {
                        // next "row"
                        VariableBinding[] vbs = l.toArray(new VariableBinding[0]);
                        listener.next(new TreeEvent(this, userObject, vbs));
                        l.clear();
                    }
                }
                if (respPDU.size() == 0) {
                    finished = true;
                }
                VariableBinding[] vbs = l.toArray(new VariableBinding[0]);
                if (finished) {
                    listener.finished(new TreeEvent(this, userObject, vbs));
                } else {
                    if (listener.next(new TreeEvent(this, userObject, vbs))) {
                        int lastRowIndex = ((respPDU.size() / rootOIDs.length) - 1) * rootOIDs.length;
                        request.clear();
                        for (int i = Math.max(0, lastRowIndex); i < lastRowIndex + rootOIDs.length; i++) {
                            VariableBinding next = (VariableBinding) respPDU.get(i).clone();
                            next.setVariable(new Null());
                            request.add(next);
                        }
                        if (request.size() > 0) {
                            send();
                        } else {
                            listener.finished(new TreeEvent(this, userObject, new VariableBinding[0]));
                        }
                    } else {
                        listener.finished(new TreeEvent(this, userObject, vbs));
                    }
                }
            }
        }
    }

    class InternalTreeListener implements TreeListener {

        private List<TreeEvent> collectedEvents;
        private volatile boolean finished = false;

        public InternalTreeListener(List<TreeEvent> eventList) {
            collectedEvents = eventList;
        }

        public synchronized boolean next(TreeEvent event) {
            collectedEvents.add(event);
            return true;
        }

        public synchronized void finished(TreeEvent event) {
            collectedEvents.add(event);
            finished = true;
            notify();
        }

        public List<TreeEvent> getCollectedEvents() {
            return collectedEvents;
        }

        public boolean isFinished() {
            return finished;
        }
    }
}
