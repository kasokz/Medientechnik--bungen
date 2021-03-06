package jpegencoder.encoding.huffman.model;

import jpegencoder.encoding.huffman.CodeWord;

import java.util.List;

/**
 * Created by Long Bui on 10.12.16.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class HuffmanTreeNullLeaf extends HuffmanTreeLeaf
{

    public HuffmanTreeNullLeaf()
    {
        super(Integer.MIN_VALUE, 0.0);
    }

    public void printCode(String currentCode)
    {
    }

    public void fillCodeBook(List<CodeWord> codeWords, int currentCode, int currentLength)
    {
    }

    public String toString()
    {
        return "null";
    }


}
