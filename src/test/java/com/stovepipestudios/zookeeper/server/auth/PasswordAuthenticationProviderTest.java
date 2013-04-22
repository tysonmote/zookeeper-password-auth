package com.stovepipestudios.zookeeper.server.auth;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

public class PasswordAuthenticationProviderTest extends TestCase {
    public PasswordAuthenticationProviderTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( PasswordAuthenticationProviderTest.class );
    }

    public void testPasswordAuthenticationProvider() {
      try {
            Assert.assertEquals( "iEPX+SQWIR3p67lj/0zigSWTKHg=", PasswordAuthenticationProvider.generateDigest( "foobar" ) );
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException");
            assertTrue( false );
        }
    }
}
