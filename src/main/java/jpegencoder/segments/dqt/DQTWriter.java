package jpegencoder.segments.dqt;

import jpegencoder.segments.SegmentWriter;
import jpegencoder.streams.BitOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long Bui on 10.01.17.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class DQTWriter extends SegmentWriter
{
    private static final int DQT_MARKER = 0xDB;
    private int length;
    private List<QuantizationTable> tables;

    public DQTWriter(BitOutputStream os)
    {
        super(os);
        setTables();
        length = 2 + tables.size() * 65;
    }

    private void setTables()
    {
        this.tables = new ArrayList<QuantizationTable>();
        tables.add(new QuantizationTable(0, QuantizationTable.QUANTIZATION_MATRIX_LUMINANCE));
        tables.add(new QuantizationTable(1, QuantizationTable.QUANTIZATION_MATRIX_CHROMINANCE));
    }

    public void writeSegment() throws IOException
    {
        os.writeByte(0xFF);
        os.writeByte(DQT_MARKER);
        os.writeByte((length & 0xFF00) >> 8);
        os.writeByte(length & 0xFF);
        for (QuantizationTable table : tables)
        {
            table.writeTable(os);
        }
    }


}
