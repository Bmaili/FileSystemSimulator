package service;

import pojo.INode;
import pojo.SuperBlock;

import java.util.HashMap;

/**
 * INode结点服务类，定义一些Inode结点的操作
 */
public class INodeService {
    private static SuperBlock superBlock = SuperBlock.superBlock;

    /**
     * 释放一个Inode节点，加到空闲链表
     *
     * @param
     * @return
     * @date 17:43 2022/5/29
     */
    public static void freeOneINode(INode node) {
        //手动置为零值
        node.firstBlock = null;
        node.lastBlock = null;
        node.blockCount = 0;
        node.fileType = 0;
        node.owner = null;
        node.fileSize = 0;
        node.createTime = null;
        node.modifyTime = null;
        node.father = null;
        node.path = null;
        node.userACL = new HashMap<>();
        node.groupACL = new HashMap<>();

        //加到free链表
        superBlock.freeINodes.add(node);
    }

    /**
     * 从空闲链表获取一个Inode结点
     *
     * @param
     * @return
     * @date 17:43 2022/5/29
     */
    public static INode getOneINode() {
        if (superBlock.freeINodes.size() < 1) {
            System.out.println("INode不足，获取失败！");
            return null;
        }
        return superBlock.freeINodes.remove();
    }
}
