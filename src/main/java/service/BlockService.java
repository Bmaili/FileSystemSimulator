package service;

import pojo.Block;
import pojo.SuperBlock;

public class BlockService {
    private static SuperBlock superBlock = SuperBlock.superBlock;

    public static Block getOneBlock() {
        if (superBlock.freeBlocks.size() < 1) {
            System.out.println("磁盘块不足，获取失败！");
            return null;
        }
        return superBlock.freeBlocks.remove();
    }

    public static void freeOneBlock(Block block) {
        block.next = null;
        superBlock.freeBlocks.add(block);
    }
}
