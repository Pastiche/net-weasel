package io.rabid.weasel.client.api;

public abstract class SimpleTCPClient {

    protected String host;
    protected int port;

    public abstract boolean connect(String username, String password);
}
