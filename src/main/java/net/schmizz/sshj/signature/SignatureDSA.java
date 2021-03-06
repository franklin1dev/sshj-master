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
package net.schmizz.sshj.signature;

import net.schmizz.sshj.common.KeyType;
import net.schmizz.sshj.common.SSHRuntimeException;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

/**
 * DSA {@link Signature}
 */
public class SignatureDSA
        extends AbstractSignatureDSA {

    /**
     * A named factory for DSA signature
     */
    public static class Factory
            implements net.schmizz.sshj.common.Factory.Named<Signature> {

        @Override
        public Signature create() {
            return new SignatureDSA();
        }

        @Override
        public String getName() {
            return KeyType.DSA.toString();
        }

    }

    public SignatureDSA() {
        super("SHA1withDSA", KeyType.DSA.toString());
    }

    @Override
    public byte[] encode(byte[] sig) {
        // sig is in ASN.1
        // SEQUENCE::={ r INTEGER, s INTEGER }

        int rIndex = 3;
        int rLen = sig[rIndex++] & 0xff;
        byte[] r = new byte[rLen];
        System.arraycopy(sig, rIndex, r, 0, r.length);

        int sIndex = rIndex + rLen + 1;
        int sLen = sig[sIndex++] & 0xff;
        byte[] s = new byte[sLen];
        System.arraycopy(sig, sIndex, s, 0, s.length);

        byte[] result = new byte[40];

        // result must be 40 bytes, but length of r and s may not be 20 bytes

        int r_copylen = Math.min(r.length, 20);
        int s_copylen = Math.min(s.length, 20);

        System.arraycopy(r, r.length - r_copylen, result, 20 - r_copylen, r_copylen);
        System.arraycopy(s, s.length - s_copylen, result, 40 - s_copylen, s_copylen);

        return result;
    }

    @Override
    public boolean verify(byte[] sig) {
        try {
            byte[] sigBlob = extractSig(sig, "ssh-dss");
            BigInteger r = new BigInteger(1, Arrays.copyOfRange(sigBlob, 0, 20));
            BigInteger s = new BigInteger(1, Arrays.copyOfRange(sigBlob, 20, 40));

            return signature.verify(encodeAsnSignature(r, s));
        } catch (SignatureException e) {
            throw new SSHRuntimeException(e);
        } catch (IOException e) {
            throw new SSHRuntimeException(e);
        }
    }
}
