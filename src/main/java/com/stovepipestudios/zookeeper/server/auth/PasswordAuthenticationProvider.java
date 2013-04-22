package com.stovepipestudios.zookeeper.server.auth;

import org.apache.zookeeper.server.auth.AuthenticationProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.ServerCnxn;

public class PasswordAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOG = Logger.getLogger( PasswordAuthenticationProvider.class );

    // Specify a command line property with key of
    // "zookeeper.PasswordAuthenticationProvider.passwordDigest" and value of
    // `base64encoded( SHA1( password ) )`
    private final static String masterDigest = System.getProperty("zookeeper.PasswordAuthenticationProvider.passwordDigest");

    public String getScheme() {
        return "password";
    }

    static public String generateDigest( String password ) throws NoSuchAlgorithmException {
        byte digest[] = MessageDigest.getInstance( "SHA1" ).digest( password.getBytes() );
        return new String( Base64.encodeBase64( digest ) );
    }

    public KeeperException.Code handleAuthentication( ServerCnxn cnxn, byte[] password ) {
        String givenPassword = new String( password );
        try {
            String givenDigest = generateDigest( givenPassword );
            if ( givenDigest.equals( masterDigest ) ) {
              cnxn.getAuthInfo().add( new Id( getScheme(), givenDigest ) );
              return KeeperException.Code.OK;
            }
        } catch ( NoSuchAlgorithmException e ) {
            LOG.error( "Missing algorithm", e );
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
        return id.equals( aclExpr );
    }
}

