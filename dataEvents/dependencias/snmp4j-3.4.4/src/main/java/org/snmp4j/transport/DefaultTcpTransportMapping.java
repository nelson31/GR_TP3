/*_############################################################################
  _## 
  _##  SNMP4J - DefaultTcpTransportMapping.java  
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

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import org.snmp4j.TransportStateReference;
import org.snmp4j.asn1.*;
import org.snmp4j.asn1.BER.*;
import org.snmp4j.log.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.*;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.util.WorkerTask;
import org.snmp4j.util.CommonTimer;

/**
 * The {@code DefaultTcpTransportMapping} implements a TCP transport
 * mapping with the Java 1.4 new IO API.
 * <p>
 * It uses a single thread for processing incoming and outgoing messages.
 * The thread is started when the {@code listen} method is called, or
 * when an outgoing request is sent using the {@code sendMessage} method.
 *
 * @author Frank Fock
 * @version 3.0
 */
public class DefaultTcpTransportMapping extends TcpTransportMapping<DefaultTcpTransportMapping.SocketEntry> {

    /**
     * The maximum number of loops trying to read data from an incoming port but no data has been received.
     * A value of 0 or less disables the check.
     */
    public static final int DEFAULT_MAX_BUSY_LOOPS = 100;

    private static final LogAdapter logger = LogFactory.getLogger(DefaultTcpTransportMapping.class);

    protected ServerThread serverThread;

    private boolean serverEnabled = false;

    private static final int MIN_SNMP_HEADER_LENGTH = 6;
    protected MessageLengthDecoder messageLengthDecoder = new SnmpMesssageLengthDecoder();
    private int maxBusyLoops = DEFAULT_MAX_BUSY_LOOPS;

    /**
     * Creates a default TCP transport mapping with the server for incoming
     * messages disabled.
     *
     * @throws IOException
     *         on failure of binding a local port.
     */
    public DefaultTcpTransportMapping() throws IOException {
        super(new TcpAddress(InetAddress.getLocalHost(), 0));
    }

    /**
     * Creates a default TCP transport mapping that binds to the given address
     * (interface) on the local host.
     *
     * @param serverAddress
     *         the TcpAddress instance that describes the server address to listen
     *         on incoming connection requests.
     *
     * @throws IOException
     *         if the given address cannot be bound.
     */
    public DefaultTcpTransportMapping(TcpAddress serverAddress) throws IOException {
        super(serverAddress);
        this.serverEnabled = true;
    }

    /**
     * Listen for incoming and outgoing requests. If the {@code serverEnabled}
     * member is {@code false} the server for incoming requests is not
     * started. This starts the internal server thread that processes messages.
     *
     * @throws SocketException
     *         when the transport is already listening for incoming/outgoing messages.
     * @throws IOException
     *         if the listen port could not be bound to the server thread.
     */
    public synchronized void listen() throws java.io.IOException {
        if (server != null) {
            throw new SocketException("Port already listening");
        }
        serverThread = new ServerThread();
        if (logger.isInfoEnabled()) {
            logger.info("TCP address " + getListenAddress() + " bound successfully");
        }
        server = SNMP4JSettings.getThreadFactory().createWorkerThread(
                "DefaultTCPTransportMapping_" + getAddress(), serverThread, true);
        if (getConnectionTimeout() > 0) {
            // run as daemon
            socketCleaner = SNMP4JSettings.getTimerFactory().createTimer();
        }
        server.run();
    }

    /**
     * Gets the {@link TransportType} this {@code TransportMapping} supports depending on {@link #isServerEnabled()}.
     *
     * @return {@link TransportType#any} if {@link #isServerEnabled()} is {@code true} and
     * {@link TransportType#sender} otherwise.
     * @since 3.2.0
     */
    @Override
    public TransportType getSupportedTransportType() {
        return (serverEnabled ? TransportType.any : TransportType.sender);
    }

    /**
     * Changes the priority of the server thread for this TCP transport mapping.
     * This method has no effect, if called before {@link #listen()} has been
     * called for this transport mapping or if SNMP4J is configured to use
     * a non-default thread factory.
     *
     * @param newPriority
     *         the new priority.
     *
     * @see Thread#setPriority
     * @since 1.2.2
     */
    public void setPriority(int newPriority) {
        WorkerTask st = server;
        if (st instanceof Thread) {
            ((Thread) st).setPriority(newPriority);
        }
    }

    /**
     * Returns the priority of the internal listen thread.
     *
     * @return a value between {@link Thread#MIN_PRIORITY} and
     * {@link Thread#MAX_PRIORITY}.
     * @since 1.2.2
     */
    public int getPriority() {
        WorkerTask st = server;
        if (st instanceof Thread) {
            return ((Thread) st).getPriority();
        } else {
            return Thread.NORM_PRIORITY;
        }
    }

    /**
     * Sets the name of the listen thread for this UDP transport mapping.
     * This method has no effect, if called before {@link #listen()} has been
     * called for this transport mapping.
     *
     * @param name
     *         the new thread name.
     *
     * @since 1.6
     */
    public void setThreadName(String name) {
        WorkerTask st = server;
        if (st instanceof Thread) {
            ((Thread) st).setName(name);
        }
    }

    /**
     * Returns the name of the listen thread.
     *
     * @return the thread name if in listening mode, otherwise {@code null}.
     * @since 1.6
     */
    public String getThreadName() {
        WorkerTask st = server;
        if (st != null) {
            return ((Thread) st).getName();
        } else {
            return null;
        }
    }

    /**
     * Closes all open sockets and stops the internal server thread that
     * processes messages and removes all queued requests and socket entries.
     */
    public void close() {
        WorkerTask st = server;
        if (st != null) {
            server = null;
            st.terminate();
            st.interrupt();
            try {
                st.join();
            } catch (InterruptedException ex) {
                logger.warn(ex);
            }
            closeSockets(sockets);
            sockets.clear();
            if (socketCleaner != null) {
                socketCleaner.cancel();
            }
            socketCleaner = null;
        }
    }

    /**
     * Sends a SNMP message to the supplied address.
     *
     * @param address
     *         an {@code TcpAddress}. A {@code ClassCastException} is thrown
     *         if {@code address} is not a {@code TcpAddress} instance.
     * @param message
     *         byte[]
     *         the message to sent.
     * @param tmStateReference
     *         the (optional) transport model state reference as defined by
     *         RFC 5590 section 6.1.
     *
     * @throws IOException
     *         if an IO exception occurs while trying to send the message.
     */
    public void sendMessage(TcpAddress address, byte[] message,
                            TransportStateReference tmStateReference, long timeoutMillis, int maxRetries)
            throws java.io.IOException {
        if (server == null || serverThread == null) {
            if (isOpenSocketOnSending()) {
                listen();
            }
            else {
                handleDroppedMessageToSend(address, message, tmStateReference, timeoutMillis, maxRetries);
            }
        }
        if (serverThread != null) {
            if ((suspendedAddresses.size() > 0) && suspendedAddresses.contains(address)) {
                handleDroppedMessageToSend(address, message, tmStateReference, timeoutMillis, maxRetries);
            }
            else {
                serverThread.sendMessage(address, message, tmStateReference);
            }
        }
    }

    /**
     * Checks whether a server for incoming requests is enabled.
     *
     * @return boolean
     */
    public boolean isServerEnabled() {
        return serverEnabled;
    }

    public MessageLengthDecoder getMessageLengthDecoder() {
        return messageLengthDecoder;
    }

    /**
     * Sets whether a server for incoming requests should be created when
     * the transport is set into listen state. Setting this value has no effect
     * until the {@link #listen()} method is called (if the transport is already
     * listening, {@link #close()} has to be called before).
     *
     * @param serverEnabled
     *         if {@code true} if the transport will listens for incoming
     *         requests after {@link #listen()} has been called.
     */
    public void setServerEnabled(boolean serverEnabled) {
        this.serverEnabled = serverEnabled;
    }

    /**
     * Sets the message length decoder. Default message length decoder is the
     * {@link SnmpMesssageLengthDecoder}. The message length decoder must be
     * able to decode the total length of a message for this transport mapping
     * protocol(s).
     *
     * @param messageLengthDecoder
     *         a {@code MessageLengthDecoder} instance.
     */
    public void setMessageLengthDecoder(MessageLengthDecoder messageLengthDecoder) {
        if (messageLengthDecoder == null) {
            throw new NullPointerException();
        }
        this.messageLengthDecoder = messageLengthDecoder;
    }

    /**
     * Gets the {@link CommonTimer} that controls socket cleanup operations.
     *
     * @return a socket cleaner timer.
     * @since 3.0
     */
    @Override
    public CommonTimer getSocketCleaner() {
        return socketCleaner;
    }

    /**
     * Gets the inbound buffer size for incoming requests. When SNMP packets are
     * received that are longer than this maximum size, the messages will be
     * silently dropped and the connection will be closed.
     *
     * @return the maximum inbound buffer size in bytes.
     */
    public int getMaxInboundMessageSize() {
        return super.getMaxInboundMessageSize();
    }

    /**
     * Sets the maximum buffer size for incoming requests. When SNMP packets are
     * received that are longer than this maximum size, the messages will be
     * silently dropped and the connection will be closed.
     *
     * @param maxInboundMessageSize
     *         the length of the inbound buffer in bytes.
     */
    public void setMaxInboundMessageSize(int maxInboundMessageSize) {
        this.maxInboundMessageSize = maxInboundMessageSize;
    }


    public boolean isListening() {
        return (server != null);
    }

    protected int getMaxBusyLoops() {
        return maxBusyLoops;
    }

    protected void setMaxBusyLoops(int maxBusyLoops) {
        this.maxBusyLoops = maxBusyLoops;
    }

    protected class SocketEntry extends AbstractSocketEntry {
        private ByteBuffer readBuffer = null;

        public SocketEntry(TcpAddress address, Socket socket) {
            super(address, socket);
        }

        public void closeSession() {
            // nothing to do
        }

        public void setReadBuffer(ByteBuffer byteBuffer) {
            this.readBuffer = byteBuffer;
        }

        public ByteBuffer getReadBuffer() {
            return readBuffer;
        }

        @Override
        public String toString() {
            return "SocketEntry[peerAddress=" + getPeerAddress() +
                    ",socket=" + socket + ",lastUse=" + new Date(getLastUse() / SnmpConstants.MILLISECOND_TO_NANOSECOND) +
                    ",readBufferPosition=" + ((readBuffer == null) ? -1 : readBuffer.position()) + ",socketTimeout=" + getSocketTimeout() +
                    "]";
        }

    }

    public static class SnmpMesssageLengthDecoder implements MessageLengthDecoder {
        public int getMinHeaderLength() {
            return MIN_SNMP_HEADER_LENGTH;
        }

        public MessageLength getMessageLength(ByteBuffer buf) throws IOException {
            MutableByte type = new MutableByte();
            BERInputStream is = new BERInputStream(buf);
            int ml = BER.decodeHeader(is, type, false);
            int hl = (int) is.getPosition();
            MessageLength messageLength = new MessageLength(hl, ml);
            return messageLength;
        }
    }

    @Override
    public TcpAddress getListenAddress() {
        int port = tcpAddress.getPort();
        ServerThread serverThreadCopy = serverThread;
        try {
            port = ((InetSocketAddress) serverThreadCopy.ssc.getLocalAddress()).getPort();
        } catch (NullPointerException npe) {
            // ignore
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TcpAddress(tcpAddress.getInetAddress(), port);
    }


    protected class ServerThread extends AbstractTcpServerThread<SocketEntry> {
        protected byte[] buf;
        private volatile boolean stop = false;
        private Throwable lastError = null;
        private ServerSocketChannel ssc;

        public ServerThread() throws IOException {
            super(DefaultTcpTransportMapping.this);
            buf = new byte[getMaxInboundMessageSize()];
            // Selector for incoming requests
            selector = Selector.open();

            if (serverEnabled) {
                // Create a new server socket and set to non blocking mode
                ssc = ServerSocketChannel.open();
                try {
                    ssc.configureBlocking(false);

                    // Bind the server socket
                    InetSocketAddress isa = new InetSocketAddress(tcpAddress.getInetAddress(),
                            tcpAddress.getPort());
                    setSocketOptions(ssc.socket());
                    ssc.socket().bind(isa);
                    // Register accepts on the server socket with the selector. This
                    // step tells the selector that the socket wants to be put on the
                    // ready list when accept operations occur, so allowing multiplexed
                    // non-blocking I/O to take place.
                    ssc.register(selector, SelectionKey.OP_ACCEPT);
                } catch (IOException iox) {
                    logger.warn("Socket bind failed for " + tcpAddress + ": " + iox.getMessage());
                    try {
                        ssc.close();
                    } catch (IOException ioxClose) {
                        logger.warn("Socket close failed after bind failure for " + tcpAddress + ": " + ioxClose.getMessage());
                    }
                    throw iox;
                }
            }
        }

        private void processPending() {
            synchronized (pending) {
                for (int i = 0; i < pending.size(); i++) {
                    SocketEntry entry = pending.get(i);
                    try {
                        // Register the channel with the selector, indicating
                        // interest in connection completion and attaching the
                        // target object so that we can get the target back
                        // after the key is added to the selector's
                        // selected-key set
                        if (entry.getSocket().isConnected()) {
                            entry.addRegistration(selector, SelectionKey.OP_WRITE);
                        } else {
                            entry.addRegistration(selector, SelectionKey.OP_CONNECT);
                        }
                    } catch (CancelledKeyException ckex) {
                        logger.warn(ckex);
                        pending.remove(entry);
                        i--;
                        try {
                            entry.getSocket().getChannel().close();
                            TransportStateEvent e =
                                    new TransportStateEvent(DefaultTcpTransportMapping.this,
                                            entry.getPeerAddress(),
                                            TransportStateEvent.STATE_CLOSED,
                                            null,
                                            entry.getMessages());
                            fireConnectionStateChanged(e);
                        } catch (IOException ex) {
                            logger.error(ex);
                        }
                    } catch (IOException iox) {
                        logger.error(iox);
                        pending.remove(entry);
                        // Something went wrong, so close the channel and
                        // record the failure
                        try {
                            entry.getSocket().getChannel().close();
                            TransportStateEvent e =
                                    new TransportStateEvent(DefaultTcpTransportMapping.this,
                                            entry.getPeerAddress(),
                                            TransportStateEvent.STATE_CLOSED,
                                            iox, entry.getMessages());
                            fireConnectionStateChanged(e);
                        } catch (IOException ex) {
                            logger.error(ex);
                        }
                        lastError = iox;
                        if (SNMP4JSettings.isForwardRuntimeExceptions()) {
                            throw new RuntimeException(iox);
                        }
                        break;
                    }
                }
            }
        }

        public Throwable getLastError() {
            return lastError;
        }

        public void sendMessage(Address address, byte[] message, TransportStateReference tmStateReference)
                throws java.io.IOException {
            Socket s = null;
            SocketEntry entry = sockets.get(address);
            if (logger.isDebugEnabled()) {
                logger.debug("Looking up connection for destination '" + address + "' returned: " + entry);
                logger.debug(sockets.toString());
            }
            if (entry != null) {
                synchronized (entry) {
                    entry.used();
                    s = entry.getSocket();
                }
            }
            if ((s == null) || (s.isClosed()) || (!s.isConnected())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Socket for address '" + address + "' is closed, opening it...");
                }
                synchronized (pending) {
                    pending.remove(entry);
                }
                SocketChannel sc = null;
                try {
                    InetSocketAddress targetAddress =
                            new InetSocketAddress(((TcpAddress) address).getInetAddress(),
                                    ((TcpAddress) address).getPort());
                    if ((s == null) || (s.isClosed())) {
                        // Open the channel, set it to non-blocking, initiate connect
                        sc = SocketChannel.open();
                        sc.configureBlocking(false);
                        sc.connect(targetAddress);
                    } else {
                        sc = s.getChannel();
                        sc.configureBlocking(false);
                        if (!sc.isConnectionPending()) {
                            sc.connect(targetAddress);
                        }
                    }
                    s = sc.socket();
                    entry = new SocketEntry((TcpAddress) address, s);
                    entry.addMessage(message);
                    connectSocketToSendMessage(address, message, s, entry, sockets);
                } catch (IOException iox) {
                    logger.error(iox);
                    throw iox;
                }
            } else {
                entry.addMessage(message);
                synchronized (pending) {
                    pending.add(entry);
                }
                logger.debug("Waking up selector for new message");
                selector.wakeup();
            }
        }


        public void run() {
            // Here's where everything happens. The select method will
            // return when any operations registered above have occurred, the
            // thread has been interrupted, etc.
            try {
                while (!stop) {
                    try {
                        if (selector.select() > 0) {
                            if (stop) {
                                break;
                            }
                            // Someone is ready for I/O, get the ready keys
                            Set<SelectionKey> readyKeys = selector.selectedKeys();
                            Iterator<SelectionKey> it = readyKeys.iterator();

                            // Walk through the ready keys collection and process date requests.
                            while (it.hasNext()) {
                                try {
                                    SelectionKey sk = it.next();
                                    it.remove();
                                    SocketChannel readChannel = null;
                                    TcpAddress incomingAddress = null;
                                    if (sk.isAcceptable()) {
                                        logger.debug("Key is acceptable");
                                        // The key indexes into the selector so you
                                        // can retrieve the socket that's ready for I/O
                                        ServerSocketChannel nextReady =
                                                (ServerSocketChannel) sk.channel();
                                        Socket s = nextReady.accept().socket();
                                        readChannel = s.getChannel();
                                        readChannel.configureBlocking(false);

                                        incomingAddress = createIncomingAddress(s);
                                        SocketEntry entry = new SocketEntry(incomingAddress, s);
                                        entry.addRegistration(selector, SelectionKey.OP_READ);
                                        sockets.put(incomingAddress, entry);
                                        timeoutSocket(entry);
                                        TransportStateEvent e =
                                                new TransportStateEvent(DefaultTcpTransportMapping.this,
                                                        incomingAddress,
                                                        TransportStateEvent.STATE_CONNECTED,
                                                        null);
                                        fireConnectionStateChanged(e);
                                        if (e.isCancelled()) {
                                            logger.warn("Incoming connection cancelled");
                                            s.close();
                                            removeSocketEntry(incomingAddress);
                                            readChannel = null;
                                        }
                                    } else if (sk.isWritable()) {
                                        logger.debug("Key is writable");
                                        incomingAddress = writeData(sk, incomingAddress);
                                    } else if (sk.isReadable()) {
                                        logger.debug("Key is readable");
                                        readChannel = (SocketChannel) sk.channel();
                                        incomingAddress = createIncomingAddress(readChannel.socket());
                                    } else if (sk.isConnectable()) {
                                        logger.debug("Key is connectable");
                                        connectChannel(sk, incomingAddress);
                                    }

                                    if (readChannel != null) {
                                        logger.debug("Key is reading");
                                        try {
                                            if (!readMessage(sk, readChannel, incomingAddress)) {
                                                SocketEntry entry = (SocketEntry) sk.attachment();
                                                if ((entry != null) && (getMaxBusyLoops() > 0)) {
                                                    int busyLoops = entry.nextBusyLoop();
                                                    if (busyLoops > getMaxBusyLoops()) {
                                                        if (logger.isDebugEnabled()) {
                                                            logger.debug("After " + busyLoops + " read key has been removed: " + entry);
                                                        }
                                                        entry.removeRegistration(selector, SelectionKey.OP_READ);
                                                        entry.resetBusyLoops();
                                                    }
                                                }
                                            }
                                        } catch (IOException iox) {
                                            // IO exception -> channel closed remotely
                                            socketClosedRemotely(sk, readChannel, incomingAddress);
                                        }
                                    }
                                } catch (CancelledKeyException ckex) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Selection key cancelled, skipping it");
                                    }
                                }
                            }
                        }
                    } catch (NullPointerException npex) {
                        // There seems to happen a NullPointerException within the select()
                        npex.printStackTrace();
                        logger.warn("NullPointerException within select()?");
                        stop = true;
                    }
                    if (!stop) {
                        processPending();
                    }
                }
                if (ssc != null) {
                    ssc.close();
                }
                if (selector != null) {
                    selector.close();
                }
            } catch (IOException iox) {
                logger.error(iox);
                lastError = iox;
            }
            if (!stop) {
                stop = true;
                synchronized (DefaultTcpTransportMapping.this) {
                    server = null;
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Worker task finished: " + getClass().getName());
            }
        }

        @Override
        public SocketEntry removeSocketEntry(TcpAddress incomingAddress) {
            return sockets.remove(incomingAddress);
        }

        protected boolean readMessage(SelectionKey sk, SocketChannel readChannel,
                                      TcpAddress incomingAddress) throws IOException {
            SocketEntry entry = (SocketEntry) sk.attachment();
            if (entry == null) {
                // slow but in some cases needed:
                entry = sockets.get(incomingAddress);
            }
            if (entry != null) {
                // note that socket has been used
                entry.used();
                ByteBuffer readBuffer = entry.getReadBuffer();
                if (readBuffer != null) {
                    int bytesRead = readChannel.read(readBuffer);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Read " + bytesRead + " bytes from " + incomingAddress);
                    }
                    if ((bytesRead >= 0) &&
                            (readBuffer.hasRemaining() ||
                                    (readBuffer.position() < messageLengthDecoder.getMinHeaderLength()))) {
                        entry.addRegistration(selector, SelectionKey.OP_READ);
                    } else if (bytesRead < 0) {
                        socketClosedRemotely(sk, readChannel, incomingAddress);
                    } else {
                        readSnmpMessagePayload(readChannel, incomingAddress, entry, readBuffer);
                    }
                    if (bytesRead != 0) {
                        entry.resetBusyLoops();
                        return true;
                    }
                    return false;
                }
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
            byteBuffer.limit(messageLengthDecoder.getMinHeaderLength());
            if (!readChannel.isOpen()) {
                sk.cancel();
                if (logger.isDebugEnabled()) {
                    logger.debug("Read channel not open, no bytes read from " +
                            incomingAddress);
                }
                return false;
            }
            long bytesRead;
            try {
                bytesRead = readChannel.read(byteBuffer);
                if (logger.isDebugEnabled()) {
                    logger.debug("Reading header " + bytesRead + " bytes from " +
                            incomingAddress);
                }
            } catch (ClosedChannelException ccex) {
                sk.cancel();
                if (logger.isDebugEnabled()) {
                    logger.debug("Read channel not open, no bytes read from " +
                            incomingAddress);
                }
                return false;
            }
            if (byteBuffer.position() >= messageLengthDecoder.getMinHeaderLength()) {
                readSnmpMessagePayload(readChannel, incomingAddress, entry, byteBuffer);
            } else if (bytesRead < 0) {
                socketClosedRemotely(sk, readChannel, incomingAddress);
            } else if ((entry != null) && (bytesRead > 0)) {
                addBufferToReadBuffer(entry, byteBuffer);
                entry.addRegistration(selector, SelectionKey.OP_READ);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("No socket entry found for incoming address " + incomingAddress +
                            " for incomplete message with length " + bytesRead);
                }
            }
            if ((entry != null) && (bytesRead != 0)) {
                entry.resetBusyLoops();
                return true;
            }
            return false;
        }

        protected void readSnmpMessagePayload(SocketChannel readChannel, TcpAddress incomingAddress,
                                              SocketEntry entry, ByteBuffer byteBuffer) throws IOException {
            MessageLength messageLength =
                    messageLengthDecoder.getMessageLength(ByteBuffer.wrap(byteBuffer.array()));
            if (logger.isDebugEnabled()) {
                logger.debug("Message length is " + messageLength);
            }
            if ((messageLength.getMessageLength() > getMaxInboundMessageSize()) ||
                    (messageLength.getMessageLength() <= 0)) {
                logger.error("Received message length " + messageLength +
                        " is greater than inboundBufferSize " +
                        getMaxInboundMessageSize());
                if (entry != null) {
                    Socket s = entry.getSocket();
                    if (s != null) {
                        s.close();
                        logger.info("Socket to " + entry.getPeerAddress() +
                                " closed due to an error");
                    }
                }
            } else {
                int messageSize = messageLength.getMessageLength();
                if (byteBuffer.position() < messageSize) {
                    if (byteBuffer.capacity() < messageSize) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Extending message buffer size according to message length to " + messageSize);
                        }
                        // Enhance capacity to expected message size and replace existing (too short) read buffer
                        byte[] newBuffer = new byte[messageSize];
                        int len = byteBuffer.position();
                        byteBuffer.flip();
                        byteBuffer.get(newBuffer, 0, len);
                        byteBuffer = ByteBuffer.wrap(newBuffer);
                        byteBuffer.position(len);
                        if (entry != null) {
                            byteBuffer.limit(messageSize);
                            entry.setReadBuffer(byteBuffer);
                        }
                    } else {
                        byteBuffer.limit(messageSize);
                    }
                    readChannel.read(byteBuffer);
                }
                long bytesRead = byteBuffer.position();
                if (bytesRead >= messageSize) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Message completed with " + bytesRead + " bytes and " + byteBuffer.limit() + " buffer limit");
                    }
                    if (entry != null) {
                        entry.setReadBuffer(null);
                    }
                    dispatchMessage(incomingAddress, byteBuffer, bytesRead, entry);
                } else if ((entry != null) && (byteBuffer != entry.getReadBuffer())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Adding buffer content to read buffer of entry " + entry + ", buffer " + byteBuffer);
                    }
                    addBufferToReadBuffer(entry, byteBuffer);
                }
                if (entry != null) {
                    entry.addRegistration(selector, SelectionKey.OP_READ);
                }
            }
        }

        private void dispatchMessage(TcpAddress incomingAddress,
                                     ByteBuffer byteBuffer, long bytesRead,
                                     Object sessionID) {
            byteBuffer.flip();
            if (logger.isDebugEnabled()) {
                logger.debug("Received message from " + incomingAddress +
                        " with length " + bytesRead + ": " +
                        new OctetString(byteBuffer.array(), 0,
                                (int) bytesRead).toHexString());
            }
            ByteBuffer bis;
            if (isAsyncMsgProcessingSupported()) {
                byte[] bytes = new byte[(int) bytesRead];
                System.arraycopy(byteBuffer.array(), 0, bytes, 0, (int) bytesRead);
                bis = ByteBuffer.wrap(bytes);
            } else {
                bis = ByteBuffer.wrap(byteBuffer.array(),
                        0, (int) bytesRead);
            }
            TransportStateReference stateReference =
                    new TransportStateReference(DefaultTcpTransportMapping.this, incomingAddress, null,
                            SecurityLevel.undefined, SecurityLevel.undefined,
                            false, sessionID);
            fireProcessMessage(incomingAddress, bis, stateReference);
        }

        public void close() {
            stop = true;
            WorkerTask st = server;
            if (st != null) {
                st.terminate();
            }
        }

        public void terminate() {
            stop = true;
            if (logger.isDebugEnabled()) {
                logger.debug("Terminated worker task: " + getClass().getName());
            }
        }

        public void join() {
            if (logger.isDebugEnabled()) {
                logger.debug("Joining worker task: " + getClass().getName());
            }
        }

        public void interrupt() {
            stop = true;
            if (logger.isDebugEnabled()) {
                logger.debug("Interrupting worker task: " + getClass().getName());
            }
            selector.wakeup();
        }
    }

    protected TcpAddress createIncomingAddress(Socket s) {
        return new TcpAddress(s.getInetAddress(), s.getPort());
    }

    protected void addBufferToReadBuffer(SocketEntry entry, ByteBuffer byteBuffer) {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding data " + byteBuffer + " to read buffer " + entry.getReadBuffer());
        }
        int buflen = byteBuffer.position();
        if (entry.getReadBuffer() != null) {
            entry.getReadBuffer().put(byteBuffer.array(), 0, buflen);
        } else {
            byte[] message = new byte[byteBuffer.limit()];
            byteBuffer.flip();
            byteBuffer.get(message, 0, buflen);
            ByteBuffer newBuffer = ByteBuffer.wrap(message);
            newBuffer.position(buflen);
            entry.setReadBuffer(newBuffer);
        }
    }

    protected void socketClosedRemotely(SelectionKey sk, SocketChannel readChannel, TcpAddress incomingAddress)
            throws IOException {
        logger.debug("Socket closed remotely");
        sk.cancel();
        readChannel.close();
        TransportStateEvent e =
                new TransportStateEvent(DefaultTcpTransportMapping.this, incomingAddress,
                        TransportStateEvent.STATE_DISCONNECTED_REMOTELY, null);
        fireConnectionStateChanged(e);
        sockets.remove(incomingAddress);
    }

}
