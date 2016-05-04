import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by bingoc on 16/4/25.
 */
public class DataDealingClientChannelHandler extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline().addLast(new MsgPackDataDealingCodec());
        //ch.pipeline().addLast(new DataDealingDecoder());
        //ch.pipeline().addLast(new DataDealingEncoder());
        //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024, 0, 4));
        ch.pipeline().addLast(new DataDealingClientHandler());
    }
}
