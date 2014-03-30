package io.rabid.weasel.client.nettybased;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.rabid.weasel.client.api.SimpleNetworkClient;

public class DefaultSimpleNetworkClient extends SimpleNetworkClient {
    public DefaultSimpleNetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean connect(String username, String password) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        ChannelFuture channelFuture = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                     .channel(NioSocketChannel.class)
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel channel) throws Exception {

                         }
            });
            channelFuture = bootstrap.connect(host, port).sync();
        } catch (Exception e) {
            eventLoopGroup.shutdownGracefully();
        }
        return channelFuture != null;
    }
}
