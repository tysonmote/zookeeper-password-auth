package com.stovepipestudios.zookeeper.server.auth;

import org.apache.zookeeper.server.auth.AuthenticationProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.ServerCnxn;

public class PasswordAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOG = Logger.getLogger(PasswordAuthenticationProvider.class);

    /** specify a command line property with key of
     * "zookeeper.PasswordAuthenticationProvider.passwordDigest" and value of
     * `base64encoded( SHA1( password ) )`
     */
    private final static String masterDigest = System.getProperty("zookeeper.PasswordAuthenticationProvider.passwordDigest");

    public String getScheme() {
        return "password";
    }

    static final private String base64Encode(byte b[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length;) {
            int pad = 0;
            int v = (b[i++] & 0xff) << 16;
            if (i < b.length) {
                v |= (b[i++] & 0xff) << 8;
            } else {
                pad++;
            }
            if (i < b.length) {
                v |= (b[i++] & 0xff);
            } else {
                pad++;
            }
            sb.append(encode(v >> 18));
            sb.append(encode(v >> 12));
            if (pad < 2) {
                sb.append(encode(v >> 6));
            } else {
                sb.append('=');
            }
            if (pad < 1) {
                sb.append(encode(v));
            } else {
                sb.append('=');
            }
        }
        return sb.toString();
    }

    static final private char encode(int i) {
        i &= 0x3f;
        if (i < 26) {
            return (char) ('A' + i);
        }
        if (i < 52) {
            return (char) ('a' + i - 26);
        }
        if (i < 62) {
            return (char) ('0' + i - 52);
        }
        return i == 62 ? '+' : '/';
    }

    static public String generateDigest(String password) throws NoSuchAlgorithmException {
        byte digest[] = MessageDigest.getInstance("SHA1").digest(password.getBytes());
        return base64Encode(digest);
    }

    public KeeperException.Code handleAuthentication(ServerCnxn cnxn, byte[] authData)
    {
        String givenPassword = new String(authData);
        try {
            String givenDigest = generateDigest(givenPassword);
            if (givenDigest.equals(masterDigest)) {
              cnxn.getAuthInfo().add(new Id(getScheme(), givenDigest));
              return KeeperException.Code.OK;
            } else {
              return KeeperException.Code.AUTHFAILED;
            }
        } catch (NoSuchAlgorithmException e) {
          LOG.error("Missing algorithm",e);
        }
        return KeeperException.Code.AUTHFAILED;
    }

    public boolean isAuthenticated() {
        return true;
    }

    public boolean isValid(String id) {
        return id.length() > 0;
    }

    public boolean matches(String id, String aclExpr) {
        return id.equals(aclExpr);
    }
}

