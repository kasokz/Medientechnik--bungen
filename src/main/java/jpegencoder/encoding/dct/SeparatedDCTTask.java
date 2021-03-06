package jpegencoder.encoding.dct;

import org.jblas.DoubleMatrix;

import java.util.List;

/**
 * Created by Long Bui on 20.12.16.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class SeparatedDCTTask implements Runnable
{
    private List<DoubleMatrix> blocks;

    public SeparatedDCTTask(List<DoubleMatrix> blocks)
    {
        this.blocks = blocks;
    }

    public void run()
    {
        for (DoubleMatrix block : blocks)
        {
            CosineTransformation.separated(block);
        }
    }
}
