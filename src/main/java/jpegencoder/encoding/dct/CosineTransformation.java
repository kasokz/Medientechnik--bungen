package jpegencoder.encoding.dct;

import org.jblas.DoubleMatrix;

/**
 * Created by Long Bui on 14.12.16.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class CosineTransformation
{
    public static void arai(DoubleMatrix x)
    {
        for (int i = 0; i < 8; i++)
        {
            x.putRow(i, Arai.calc(x.getRow(i)));
        }
        for (int i = 0; i < 8; i++)
        {
            x.putColumn(i, Arai.calc(x.getColumn(i)));
        }
    }

    public static DoubleMatrix direct(DoubleMatrix X)
    {
        return DCT.direct(X);
    }

    public static void separated(DoubleMatrix X)
    {
        DCT.separated(X);
    }

    public static DoubleMatrix invert(DoubleMatrix Y)
    {
        return DCT.invert(Y);
    }

    public static void quantize(DoubleMatrix transformedMatrix, DoubleMatrix quantizationMatrix)
    {
        transformedMatrix.divi(quantizationMatrix, transformedMatrix);
    }
}
