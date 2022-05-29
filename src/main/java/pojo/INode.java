package pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class INode implements Serializable {
    //pojo.Block:文件的数据块
    public Block firstBlock;
    public Block lastBlock;
    public User owner;
    public int blockCount;
    public int fileType; //文件类型(0普通文件，1目录文件，2特殊文件)
    public int fileSize; //文件尺寸，单位：字节
    public Date createTime;
    public Date modifyTime;

    public Folder father;
    public String path;
    // 存取控制表（Access Control List，ACL）
    // 1代表执行权限，2代表写入权限，4代表读取权限
    public HashMap<User, Integer> userACL = new HashMap<>();
    public HashMap<UserGroup, Integer> groupACL = new HashMap<>();


}
