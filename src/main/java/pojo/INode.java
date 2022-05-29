package pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * INode类，用来保存文件的详细信息
 */
public class INode implements Serializable {
    //pojo.Block:文件的数据块
    public Block firstBlock;//第一个block块
    public Block lastBlock;//最后一个block块
    public int blockCount;//使用的块的数量

    public User owner;//文件所有者（创建者）
    public int fileType; //文件类型(0普通文件，1目录文件，2特殊文件)
    public int fileSize; //文件尺寸，单位：字节
    public Date createTime;//创建时间
    public Date modifyTime;//修改时间

    // 存取控制表（Access Control List，ACL）
    // 1代表执行权限，2代表写入权限，4代表读取权限
    public HashMap<User, Integer> userACL = new HashMap<>();
    public HashMap<UserGroup, Integer> groupACL = new HashMap<>();

    public Folder father;//父目录结点
    public String path;//路径

}
