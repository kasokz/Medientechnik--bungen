package jpegencoder.encoding.huffman;

import jpegencoder.encoding.CodeWord;
import jpegencoder.encoding.huffman.model.HuffmanTree;
import jpegencoder.encoding.huffman.model.HuffmanTreeComponent;
import jpegencoder.encoding.huffman.model.HuffmanTreeLeaf;
import jpegencoder.encoding.huffman.model.HuffmanTreeNode;

import java.util.*;

/**
 * Created by Long Bui on 29.11.16.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class HuffmanEncoder
{
    private HuffmanTree huffmanTree;

    HuffmanEncoder()
    {
    }

    public static HuffmanEncoder encode(int[] symbols)
    {
        HuffmanEncoder encoder = new HuffmanEncoder();
        encoder.huffmanTree = encoder.createHuffmanTree(encoder.huffmanInit(symbols));
        return encoder;
    }

    public HuffmanEncoder forJpeg()
    {
        return canonical().withoutFullOnes().withLengthRestriction(16);
    }

    public HuffmanEncoder canonical()
    {
        this.huffmanTree.makeCanonical();
        return this;
    }

    public HuffmanEncoder withoutFullOnes()
    {
        this.huffmanTree.replaceMostRight();
        return this;
    }

    public HuffmanEncoder withLengthRestriction(int lengthRestriction)
    {
        this.huffmanTree.restrictToLength(lengthRestriction);
        return this;
    }

    public Map<Integer, CodeWord> getCodebookAsMap()
    {
        return this.huffmanTree.getCodeBookAsMap();
    }

    List<HuffmanTreeComponent> huffmanInit(int[] symbols)
    {
        Map<Integer, Integer> frequencies = new HashMap<Integer, Integer>();
        int totalSymbols = symbols.length;
        for (int symbol : symbols)
        {
            if (frequencies.containsKey(symbol))
            {
                frequencies.put(symbol, frequencies.get(symbol) + 1);
            }
            else
            {
                frequencies.put(symbol, 1);
            }
        }
        List<HuffmanTreeComponent> leafs = new ArrayList<HuffmanTreeComponent>();
        for (Map.Entry<Integer, Integer> frequency : frequencies.entrySet())
        {
            leafs.add(new HuffmanTreeLeaf(frequency.getKey(), frequency.getValue() / (double) totalSymbols));
        }
        Collections.sort(leafs);
        return leafs;
    }

    HuffmanTree createHuffmanTree(List<HuffmanTreeComponent> nodes)
    {
        List<HuffmanTreeLeaf> symbols = new ArrayList<HuffmanTreeLeaf>();
        for (HuffmanTreeComponent node : nodes)
        {
            symbols.add((HuffmanTreeLeaf) node);
        }
        createHuffmanTreeNodes(nodes);
        return new HuffmanTree(nodes.get(0), symbols);
    }

    private void createHuffmanTreeNodes(List<HuffmanTreeComponent> nodes)
    {
        if (nodes.size() > 1)
        {
            HuffmanTreeNode huffmanTreeNode = new HuffmanTreeNode(nodes.get(1), nodes.get(0));
            nodes.remove(0);
            nodes.remove(0);
            nodes.add(huffmanTreeNode);
            Collections.sort(nodes);
            createHuffmanTreeNodes(nodes);
        }
    }
}