import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by bingoc on 16/4/23.
 */
public class DataDealingClient {

    public static void main(String args[]) throws Exception {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new DataDealingClientChannelHandler());

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();


//            String name = "diagram.png";
//            FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/diagram.png"));
//            byte[] bytes = new byte[fileInputStream.available()];
//            fileInputStream.read(bytes);
//
//            ByteBuf byteBuf = Unpooled.buffer();
//
//            byteBuf.writeInt(4+ name.getBytes().length +4+ bytes.length);
//            byteBuf.writeInt("diagram.png".getBytes().length);
//            byteBuf.writeBytes("diagram.png".getBytes());
//
//            byteBuf.writeInt(bytes.length);
//            byteBuf.writeBytes(bytes);

            File file = new File("src/main/resources/diagram.png");
            channelFuture.channel().writeAndFlush(file);

            channelFuture.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}
