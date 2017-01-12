package jpegencoder.encoding;

import jpegencoder.encoding.huffman.HuffmanTree;
import jpegencoder.encoding.huffman.HuffmanTreeComponent;
import jpegencoder.encoding.huffman.HuffmanTreeLeaf;
import jpegencoder.encoding.huffman.HuffmanTreeNode;

import java.util.*;

/**
 * Created by Long Bui on 29.11.16.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class HuffmanEncoder
{
    private Map<Integer, Integer> frequencies;

    public HuffmanEncoder()
    {
        frequencies = new HashMap<Integer, Integer>();
    }

    List<HuffmanTreeComponent> huffmanInit(int[] symbols)
    {
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

    public HuffmanTree createHuffmanTree(List<HuffmanTreeComponent> nodes)
    {
        List<HuffmanTreeLeaf> symbols = new ArrayList<HuffmanTreeLeaf>();
        for(HuffmanTreeComponent node: nodes)
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