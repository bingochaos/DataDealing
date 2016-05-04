import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by bingoc on 16/4/25.
 */
public class DataDealingDecoder extends ByteToMessageDecoder {

//    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
//    private int maxFrameLength = 1024*10;
//    private int lengthFieldLength = 4;
//    private int initialBytesToStrip = 0;
//    private long tooLongFrameLength;
//    private long bytesToDiscard;
//    private boolean failFast = true;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        Object decoded = decode(ctx, in);
        if(decoded != null) {
            ByteBuf byteBuf = (ByteBuf)decoded;
            int nameSize = byteBuf.readInt();
            String name = new String(byteBuf.readBytes(nameSize).array(), "UTF-8");
            int fileSize = byteBuf.readInt();
            File result = new File(name);
            FileOutputStream fileOutputStream = new FileOutputStream(result);
            fileOutputStream.write(byteBuf.readBytes(fileSize).array());
            out.add(result);
            in.discardReadBytes();
        }


//        int writeSize = 0;
//        if(in.isReadable())
//        {
//            int nameSize = in.readInt();
//            String name = String.valueOf(in.readBytes(nameSize));
//            File file = new File(name);
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            int fileSize = in.readInt();
//            writeSize = fileSize;
//            if(in.readableBytes() < writeSize)
//            {
//                writeSize -=in.readableBytes();
//                byte[] content = in.readBytes(in.readableBytes()).array();
//                fileOutputStream.write(content);
//
//            } else {
//                byte[] content = in.readBytes(writeSize).array();
//            }
//
//            fileOutputStream.close();
//
//            out.add(file);
//        }

    }

    boolean discardingTooLongFrame;
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        int frameLength = (int) in.getUnsignedInt(0);
        if(in.readableBytes() < frameLength)
        {
            return null;
        }
        in.skipBytes(4);
        int index =  in.readerIndex();
        ByteBuf frame = in.slice(index, frameLength).retain();

        in.readerIndex(frameLength);
        return  frame;
    }

}
