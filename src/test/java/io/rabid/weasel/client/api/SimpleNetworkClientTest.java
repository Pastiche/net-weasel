package io.rabid.weasel.client.api;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.rabid.weasel.client.nettybased.DefaultSimpleNetworkClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static junit.framework.Assert.assertTrue;

public class SimpleNetworkClientTest {
    private static ExecutorService executor;
    private SimpleNetworkClient simpleNetworkClient;


    @BeforeClass
    public static void init() {
        executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                TestServer.run(8990);
            }
        });
    }

    @Before
    public void setUp() {
        String host = "localhost";
        simpleNetworkClient = new DefaultSimpleNetworkClient(host, 8990);
    }

    @Test
    public void clientCanConnect() {
        assertTrue(simpleNetworkClient.connect("tele1", "passs1"));
    }

    private static class TestServer {

        public static void run(int port) {
            EventLoopGroup masterGroup = new NioEventLoopGroup();
            EventLoopGroup slaveGroup = new NioEventLoopGroup();

            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(masterGroup, slaveGroup).channel(NioServerSocketChannel.class);
                b.childHandler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                    @Override
                    protected void initChannel(io.netty.channel.socket.SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext context, String message)
                                    throws Exception {
                                context.write(message);
                                context.flush();
                            }
                        });
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                  .childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture futureChannel = b.bind(port).sync();
                futureChannel.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                masterGroup.shutdownGracefully();
                slaveGroup.shutdownGracefully();
            }
        }
    }

    @After
    public void tearDown() {
        executor.shutdown();
    }
}
