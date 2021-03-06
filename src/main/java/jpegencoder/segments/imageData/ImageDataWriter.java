package jpegencoder.segments.imageData;

import jpegencoder.encoding.Util;
import jpegencoder.encoding.acdc.ACCategoryEncodedPair;
import jpegencoder.encoding.acdc.ACRunlengthEncodedPair;
import jpegencoder.encoding.acdc.AcDcEncoder;
import jpegencoder.encoding.acdc.DCCategoryEncodedPair;
import jpegencoder.encoding.huffman.CodeWord;
import jpegencoder.image.Image;
import jpegencoder.image.colors.ColorChannel;
import jpegencoder.segments.SegmentWriter;
import jpegencoder.streams.BitOutputStream;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Long Bui on 17.01.17.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class ImageDataWriter extends SegmentWriter
{
    private Image image;
    private Map<Integer, CodeWord> dcYCodeBook;
    private Map<Integer, CodeWord> acYCodeBook;
    private Map<Integer, CodeWord> dcCbCrCodeBook;
    private Map<Integer, CodeWord> acCbCrCodeBook;
    private int subSampling;

    public ImageDataWriter(BitOutputStream os, Image image,
                           Map<Integer, CodeWord> dcYCodeBook,
                           Map<Integer, CodeWord> acYCodeBook,
                           Map<Integer, CodeWord> dcCbCrCodeBook,
                           Map<Integer, CodeWord> acCbCrCodeBook)
    {
        super(os);
        this.subSampling = image.getSubSampling();
        this.image = image;
        this.dcYCodeBook = dcYCodeBook;
        this.acYCodeBook = acYCodeBook;
        this.dcCbCrCodeBook = dcCbCrCodeBook;
        this.acCbCrCodeBook = acCbCrCodeBook;
    }

    public void writeSegment() throws IOException
    {
        if (image.getSubSampling() == 1)
        {
            writeSegmentWithoutSubsampling();
        }
        else
        {
            writeSegmentWithSubsampling();
        }
    }

    public void writeSegmentWithoutSubsampling() throws IOException
    {
        for (int currentY = 0; currentY < image.getChannel1().getHeightInBlocks(); currentY++)
        {
            for (int currentX = 0; currentX < image.getChannel1().getWidthInBlocks(); currentX++)
            {
                writeAcDcEncodedBlock(image.getChannel1(), currentX, currentY, dcYCodeBook, acYCodeBook);
                writeAcDcEncodedBlock(image.getChannel2(), currentX, currentY, dcCbCrCodeBook, acCbCrCodeBook);
                writeAcDcEncodedBlock(image.getChannel3(), currentX, currentY, dcCbCrCodeBook, acCbCrCodeBook);
            }
        }
        os.flush();
        os.writeByte(0xFF);
    }

    public void writeSegmentWithSubsampling() throws IOException
    {
        for (int currentY = 0; currentY < image.getChannel2().getHeightInBlocks(); currentY++)
        {
            for (int currentX = 0; currentX < image.getChannel2().getWidthInBlocks(); currentX++)
            {
                for (int currentYLuminance = currentY * subSampling;
                     currentYLuminance < currentY * subSampling + subSampling;
                     currentYLuminance++)
                {
                    for (int currentXLuminance = currentX * subSampling;
                         currentXLuminance < currentX * subSampling + subSampling;
                         currentXLuminance++)
                    {
                        writeAcDcEncodedBlock(image.getChannel1(),
                                              currentXLuminance,
                                              currentYLuminance,
                                              dcYCodeBook,
                                              acYCodeBook);
                    }
                }
                writeAcDcEncodedBlock(image.getChannel2(), currentX, currentY, dcCbCrCodeBook, acCbCrCodeBook);
                writeAcDcEncodedBlock(image.getChannel3(), currentX, currentY, dcCbCrCodeBook, acCbCrCodeBook);
            }
        }
        os.flush();
        os.writeByte(0xFF);
    }

    private void writeAcDcEncodedBlock(ColorChannel channel, int xOfChannel, int yOfChannel,
                                       Map<Integer, CodeWord> dcCodeBook,
                                       Map<Integer, CodeWord> acCodeBook)
            throws IOException
    {
        DCCategoryEncodedPair dc = AcDcEncoder.calculateDifferenceDC(channel,
                                                                     channel
                                                                             .getPlainIndexOfBlock(
                                                                                     xOfChannel,
                                                                                     yOfChannel));

        List<ACRunlengthEncodedPair> acRunlengthEncodedPairs =
                AcDcEncoder.encodeRunlength(Util.zigzagSort(channel.getBlock(xOfChannel,
                                                                             yOfChannel)));
        List<ACCategoryEncodedPair> acCategoryEncodedPairs = AcDcEncoder.encodeCategoriesAC(
                acRunlengthEncodedPairs);
        AcDcEncoder.writeDCCoefficient(os, dc, dcCodeBook);
        AcDcEncoder.writeACCoefficients(os, acCategoryEncodedPairs, acCodeBook);
    }
}
