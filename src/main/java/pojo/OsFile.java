package pojo;

import java.io.Serializable;

/**
 * 通用文件类
 */
public class OsFile implements Serializable {
    public String filename;//文件名
    public INode iNode;//INode结点，保存了文件的详细信息

    public OsFile(String filename, INode iNode) {
        this.filename = filename;
        this.iNode = iNode;
    }

    @Override
    public String toString() {
        return filename;
    }

}


