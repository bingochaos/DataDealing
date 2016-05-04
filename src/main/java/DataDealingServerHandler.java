import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by bingoc on 16/4/25.
 */
public class DataDealingServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof File)
        {
            File file = (File)msg;
            File newFile = new File("diagram.png.zip");
            file.renameTo(newFile);
            System.out.println(newFile.getName() + " " + newFile.length());

            ctx.writeAndFlush(newFile);
        }


//        if(msg instanceof ByteBuf)
//        {
//            ByteBuf byteBuf = (ByteBuf)msg;
//            int nameSize = byteBuf.readInt();
//            String name = new String(byteBuf.readBytes(nameSize).array(), "UTF-8");
//            int fileSize = byteBuf.readInt();
//            FileOutputStream fileOutputStream = new FileOutputStream(new File(name));
//            fileOutputStream.write(byteBuf.readBytes(fileSize).array());
//            System.out.println(name + " " + fileSize);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
