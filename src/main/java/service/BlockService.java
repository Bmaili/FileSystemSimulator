package service;

import pojo.Block;
import pojo.SuperBlock;

/**
 * 块服务类，对块的相关操作
 */
public class BlockService {
    private static SuperBlock superBlock = SuperBlock.superBlock;

    /**
     * 从空闲链表申请一个块
     *
     * @param
     * @return
     * @date 17:31 2022/5/29
     */
    public static Block getOneBlock() {
        if (superBlock.freeBlocks.size() < 1) {
            System.out.println("磁盘块不足，获取失败！");
            return null;
        }
        return superBlock.freeBlocks.remove();
    }

    /**
     * 释放一个块，将该块放回空闲链表
     *
     * @param
     * @return
     * @date 17:31 2022/5/29
     */
    public static void freeOneBlock(Block block) {
        block.next = null;
        superBlock.freeBlocks.add(block);
    }
}
