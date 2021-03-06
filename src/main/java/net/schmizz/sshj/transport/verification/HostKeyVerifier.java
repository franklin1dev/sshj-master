/*
 * Copyright (C)2009 - SSHJ Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.schmizz.sshj.transport.verification;

import java.security.PublicKey;
import java.util.List;

/** Host key verification interface. */
public interface HostKeyVerifier {

    /**
     * This callback is invoked when the server's host key needs to be verified. The return value indicates to the
     * caller whether the SSH connection should proceed.
     * <p/>
     * <strong>Note</strong>: host key verification is the basis for security in SSH, therefore exercise due caution in
     * implementing!
     *
     * @param hostname remote hostname
     * @param port     remote port
     * @param key      host key of server
     *
     * @return {@code true} if key is acceptable, {@code false} otherwise
     */
    boolean verify(String hostname, int port, PublicKey key);

    /**
     * It is necessary to connect with the type of algorithm that matches an existing know_host entry.
     * This will allow a match when we later verify with the negotiated key {@code HostKeyVerifier.verify}
     * @param hostname remote hostname
     * @param port     remote port
     * @return existing key types or empty list if no keys known for hostname
     */
    List<String> findExistingAlgorithms(String hostname, int port);
}
