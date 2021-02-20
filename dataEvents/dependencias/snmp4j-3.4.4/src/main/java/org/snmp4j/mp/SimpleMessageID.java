/*_############################################################################
  _## 
  _##  SNMP4J - SimpleMessageID.java  
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
package org.snmp4j.mp;

import java.io.Serializable;

/**
 * The <code>SimpleMessageID</code> implements the simplest possible {@link MessageID} with
 * a minimum memory footprint.
 *
 * @author Frank Fock
 * @since 2.4.3
 */
public class SimpleMessageID implements MessageID, Serializable {

  private static final long serialVersionUID = 6301818691474165283L;

  private int messageID;

  public SimpleMessageID(int messageID) {
    this.messageID = messageID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SimpleMessageID that = (SimpleMessageID) o;

    if (messageID != that.messageID) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return messageID;
  }

  @Override
  public int getID() {
    return messageID;
  }

  @Override
  public String toString() {
    return Integer.toString(messageID);
  }
}
