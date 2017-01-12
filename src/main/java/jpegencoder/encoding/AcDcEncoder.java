package jpegencoder.encoding;

import jpegencoder.encoding.acdc.ACRuntimeEncodedPair;
import jpegencoder.encoding.acdc.ACCategoryEncodedPair;
import jpegencoder.streams.BitOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Long Bui on 12.01.17.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class AcDcEncoder
{
    public static int encodeDc(int[] zigzagedCurrent, int[] zigzagedPrev)
    {
        return zigzagedCurrent[0] - zigzagedPrev[0];
    }

    // Weiterer Schritt wo anders, z.B. Hauptprogramm

    public static void writeDC(BitOutputStream bos, int deltaDc, Map<Integer, CodeWord> codebook) throws IOException
    {
        int category = (int) Math.round(Math.log(Math.abs(deltaDc)) /
                                                Math.log(2) + 0.5);
        CodeWord codeWord = codebook.get(category);
        bos.writeBits(codeWord.getCode(), codeWord.getLength());
        bos.writeBits(ACCategoryEncodedPair.encodeCategory(deltaDc), category);
    }

    public static List<ACRuntimeEncodedPair> encodeAc(int[] zigzaged)
    {
        List<ACRuntimeEncodedPair> resultList = new ArrayList<ACRuntimeEncodedPair>();
        // loop starts at index 1 because index 0 is DC
        int zeroCount = 0;
        for (int i = 1; i < zigzaged.length; i++)
        {
            if (zigzaged[i] != 0 || zeroCount == 15)
            {
                resultList.add(new ACRuntimeEncodedPair(zeroCount, zigzaged[i]));
                zeroCount = 0;
            }
            else
            {
                zeroCount++;
            }
        }
        // EOB
        if (zeroCount != 0 || resultList.get(resultList.size() - 1).getEntry() == 0)
        {
            while (resultList.get(resultList.size() - 1).getEntry() == 0)
            {
                resultList.remove(resultList.size() - 1);
            }
            resultList.add(new ACRuntimeEncodedPair(0, 0));
        }
        return resultList;
    }

    public static List<ACCategoryEncodedPair> encodeCategories(List<ACRuntimeEncodedPair> acRuntimeEncodedPairs)
    {
        List<ACCategoryEncodedPair> resultList = new ArrayList<ACCategoryEncodedPair>();
        for (ACRuntimeEncodedPair acRuntimeEncodedPair : acRuntimeEncodedPairs)
        {
            int category = (int) Math.round(Math.log(Math.abs(acRuntimeEncodedPair.getEntry())) /
                                                    Math.log(2) + 0.5);
            int pair = (acRuntimeEncodedPair.getZeroCount() << 4) +
                    category;
            int categoryEncoded = ACCategoryEncodedPair.encodeCategory(acRuntimeEncodedPair.getEntry());
            resultList.add(new ACCategoryEncodedPair(pair, categoryEncoded));
        }
        return resultList;
    }

    // Weiterer Schritt wo anders, z.B. Hauptprogramm

    public static void writeACTable(BitOutputStream bos, List<ACCategoryEncodedPair> acEncoding,
                                    Map<Integer, CodeWord> codebook) throws IOException
    {
        for (ACCategoryEncodedPair pair : acEncoding)
        {
            CodeWord codeWord = codebook.get(pair.getPair());
            bos.writeBits(codeWord.getCode(), codeWord.getLength());
            bos.writeBits(pair.getEntryCategoryEncoded(), pair.getCategory());
        }
    }
}
