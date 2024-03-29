/*_############################################################################
  _## 
  _##  SNMP4J - TreeListener.java  
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

import java.util.EventListener;

/**
 * The {@code TreeListener} interface is implemented by objects
 * listening for tree events.
 *
 * @author Frank Fock
 * @version 1.10
 * @since 1.8
 * @see TreeUtils
 */
public interface TreeListener extends EventListener {

    /**
     * Consumes the next table event, which is typically the next row in a
     * table retrieval operation.
     *
     * @param event
     *    a {@code TableEvent} instance.
     * @return
     *    {@code true} if this listener wants to receive more events,
     *    otherwise return {@code false}. For example, a
     *    {@code TreeListener} can return {@code false} to stop
     *    tree retrieval.
     */
    boolean next(TreeEvent event);

    /**
     * Indicates in a series of tree events that no more events will follow.
     * @param event
     *    a {@code TreeEvent} instance that will either indicate an error
     *    ({@link TreeEvent#isError()} returns {@code true}) or success
     *    of the tree retrieval operation.
     */
    void finished(TreeEvent event);

    /**
     * Indicates whether the tree walk is complete or not.
     * @return
     *    {@code true} if it is complete, {@code false} otherwise.
     * @since 1.10
     */
    boolean isFinished();
}
