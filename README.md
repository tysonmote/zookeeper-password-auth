Zookeeper PasswordAuthenticationProvider
========================================

PasswordAuthenticationProvider is a [Zookeeper AuthenticationProvider][zk_auth]
that allows connection-level authentication of Zookeeper clients with a single
string password. Unlike the built-in DigestAuthenticationProvider,
PasswordAuthenticationProvider authenticates clients at the time that they
connect, not only when they send read / write commands to Zookeeper.

Only one password is allowed. The base 64 encoded SHA1 digest of the password
should be set as a Java system property as
"zookeeper.PasswordAuthenticationProvider.passwordDigest".

Currently only compatible with Zookeeper 3.3.x.

[zk_auth]: http://zookeeper.apache.org/doc/r3.3.5/zookeeperAdmin.html#sc_authOptions

