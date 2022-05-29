package pojo;

import java.io.Serializable;

/**
 * Block块
 */
public class Block implements Serializable {
    public byte[] data = new byte[512];//每个块实际保存文件内容的字节数组
    public Block next; //使用隐式链接分配：在每个块中有指向下一个块的指针
    public int blockSize = 512;//容量
}
