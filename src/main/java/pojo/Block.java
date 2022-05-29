package pojo;

import java.io.Serializable;

public class Block implements Serializable {
    public byte[] data = new byte[512];//每个块的容量
    public Block next; //使用隐式链接分配：在每个块中有指向下一个块的指针
    public int blockSize = 512;
}
