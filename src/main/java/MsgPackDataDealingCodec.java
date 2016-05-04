import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by bingoc on 16/5/4.
 */
public class MsgPackDataDealingCodec extends ByteToMessageCodec {

    protected MessageBufferPacker messageBufferPacker = MessagePack.newDefaultBufferPacker();
    private MessageUnpacker messageUnpacker;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        if(msg instanceof File)
        {
            File file = (File)msg;
            String fileName = file.getName();
            byte[] content = readFile(file);
            messageBufferPacker.packString(fileName);
            messageBufferPacker.packRawStringHeader(content.length);
            messageBufferPacker.writePayload(content);
            out.writeInt(messageBufferPacker.toByteArray().length);
            out.writeBytes(this.messageBufferPacker.toByteArray());
        }

    }

    private byte[] readFile(File file) {
        FileChannel fileChannel = null;
        try {
            fileChannel = new RandomAccessFile(file, "r").getChannel();
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
            byte[] result = new byte[(int) fileChannel.size()];

            if(byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }

            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {

        if(in.readableBytes() < 4)
            return;

        int contentLength = in.getInt(0);
        if (in.readableBytes() < 4 + contentLength)
            return;

        in.skipBytes(4);
        byte[] content = new byte[in.readableBytes()];
        in.readBytes(content);
        messageUnpacker = MessagePack.newDefaultUnpacker(content);
        String fileName = messageUnpacker.unpackString();
        File file = new File(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[messageUnpacker.unpackRawStringHeader()];
        messageUnpacker.readPayload(bytes);
        fileOutputStream.write(bytes);

        fileOutputStream.close();
        out.add(file);
    }
}
