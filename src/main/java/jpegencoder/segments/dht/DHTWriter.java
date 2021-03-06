package jpegencoder.segments.dht;

import jpegencoder.encoding.huffman.CodeWord;
import jpegencoder.segments.SegmentWriter;
import jpegencoder.streams.BitOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Long Bui on 12.12.16.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class DHTWriter extends SegmentWriter
{
    private static final int DHT_MARKER = 0xFFC4;

    private List<HuffmanTable> tables;

    public DHTWriter(BitOutputStream os, List<HuffmanTable> tables)
    {
        super(os);
        this.tables = tables;
    }

    public int getLength()
    {
        int sum = 0;
        for (HuffmanTable table : tables)
        {
            sum += (17 + table.getCodebookSize());
        }
        return 2 + sum;
    }

    public void writeSegment() throws IOException
    {
        os.writeMarker(DHT_MARKER);
        os.writeBits(getLength(), 16);
        for (HuffmanTable table : tables)
        {
            table.write(os);
        }
    }
}
