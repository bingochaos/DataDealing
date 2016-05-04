import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by bingoc on 16/4/25.
 */
public class DataDealingEncoder extends MessageToByteEncoder {
    int BYTES_INT = 4;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(msg instanceof File)
        {
            File file = (File) msg;
            String filename = file.getName();
            byte[] content = readFile(file);
            out.writeInt(BYTES_INT + BYTES_INT + filename.getBytes().length + BYTES_INT + content.length);
            out.writeInt(file.getName().getBytes().length);
            out.writeBytes(file.getName().getBytes());

            out.writeInt(content.length);
            out.writeBytes(content);
            System.out.println(content.length);
        }
    }

    private byte[] readFile(File file) {
        FileChannel fileChannel = null;
        try {
            fileChannel = new RandomAccessFile(file, "r").getChannel();
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
           // System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int)fileChannel.size()];
            if(byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());

            }
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileChannel.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
