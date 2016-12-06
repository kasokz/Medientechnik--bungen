package Blatt3.HuffmanTree;

/**
 * Created by Long Bui on 29.11.16.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class HuffmanTreeNode extends HuffmanTreeComponent
{
    private HuffmanTreeComponent left;
    private HuffmanTreeComponent right;

    public HuffmanTreeNode()
    {
        left = null;
        right = null;
    }

    public HuffmanTreeNode(HuffmanTreeComponent left, HuffmanTreeComponent right)
    {
        this.left = left;
        this.right = right;
    }

    public double getFrequency()
    {
        return ((left != null) ? left.getFrequency() : 0) +
                ((right != null) ? right.getFrequency() : 0);
    }

    public void setLeft(HuffmanTreeComponent newLeft)
    {
        this.left = newLeft;
    }

    public void setRight(HuffmanTreeComponent newRight)
    {
        this.right = newRight;
    }

    public HuffmanTreeComponent getLeft()
    {
        if (left != null)
        {
            return left;
        }
        return null;
    }

    public HuffmanTreeComponent getRight()
    {
        if (right != null)
        {
            return right;
        }
        return null;
    }

    public int getDepth(int currentDepth)
    {
        return Math.max((left != null) ? left.getDepth(currentDepth) : currentDepth,
                        (right != null) ? right.getDepth(currentDepth) : currentDepth) + 1;
    }

    public void printCode(String currentCode)
    {
        left.printCode(currentCode + "0");
        if (right != null)
        {
            right.printCode(currentCode + "1");
        }
    }

    public String toString()
    {
        String result = "Node(";
        result += (left != null) ? left.toString() + "," : "null";
        result += (right != null) ? right.toString() : "null";
        return result + ")";
    }
}
