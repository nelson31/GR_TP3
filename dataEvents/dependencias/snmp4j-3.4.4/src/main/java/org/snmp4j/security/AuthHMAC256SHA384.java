/*_############################################################################
  _## 
  _##  SNMP4J - AuthHMAC256SHA384.java  
  _## 
  _##  Copyright (C) 2003-2020  Frank Fock and Jochen Katz (SNMP4J.org)
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
package org.snmp4j.security;

import org.snmp4j.smi.OID;

/**
 * The class {@code AuthHMAC256SHA384} implements the usmHMAC256SHA3846AuthProtocol defined by RFC 7630.
 *
 * @author Frank Fock
 * @since 2.4.0
 */
public class AuthHMAC256SHA384 extends AuthSHA2 {

    public static final OID ID = new OID(new int[]{1, 3, 6, 1, 6, 3, 10, 1, 1, 6});
    private static final int DIGEST_LENGTH = 48;
    private static final int AUTH_CODE_LENGTH = 32;
    private static final int HMAC_BLOCK_SIZE = 128;
    private static final long serialVersionUID = 8005506685761767874L;

    /**
     * Creates an usmHMAC192SHA256AuthProtocol implementation.
     */
    public AuthHMAC256SHA384() {
        super("SHA-384", ID,
                DIGEST_LENGTH, AUTH_CODE_LENGTH, HMAC_BLOCK_SIZE);
    }

}
