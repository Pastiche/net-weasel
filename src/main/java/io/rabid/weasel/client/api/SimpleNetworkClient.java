package io.rabid.weasel.client.api;

public abstract class SimpleNetworkClient {

    protected String host;
    protected int port;

    public abstract boolean connect(String username, String password);
}
