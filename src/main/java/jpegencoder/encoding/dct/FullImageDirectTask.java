package jpegencoder.encoding.dct;

import jpegencoder.image.colors.ColorChannel;
import org.jblas.DoubleMatrix;

import java.util.concurrent.Callable;

/**
 * Created by Long Bui on 16.01.17.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class FullImageDirectTask implements Callable<Integer>
{
    private ColorChannel channel;

    public FullImageDirectTask(ColorChannel channel)
    {
        this.channel = channel;
    }

    public Integer call() throws Exception
    {
        int count = 0;
        long start = System.currentTimeMillis();
        long end = start + 10000;
        while (System.currentTimeMillis() <= end)
        {
            for (DoubleMatrix block : channel.getBlocks(0, channel.getNumOfBlocks()))
            {
                CosineTransformation.direct(block);
            }
            count++;
        }
        return count;
    }
}
