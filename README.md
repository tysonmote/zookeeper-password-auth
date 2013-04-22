Zookeeper PasswordAuthenticationProvider
========================================

PasswordAuthenticationProvider is (will be) a Zookeeper
AuthenticationProvider that allows connection-level authentication of
Zookeeper clients. Unlike the built-in DigestAuthenticationProvider,
PasswordAuthenticationProvider authenticates connections at the time that they
connect, not the time they run commands.

TODO
----

* Don't use the bespoke base64 algo
* Add tests

