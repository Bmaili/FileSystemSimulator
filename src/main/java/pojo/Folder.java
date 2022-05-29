package pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 目录文件类，继承通用文件类
 */
public class Folder extends OsFile implements Serializable {
    //目录下的文件
    public ArrayList<OsFile> fileList = new ArrayList<>();

    public Folder(String filename, INode iNode) {
        super(filename, iNode);
    }
}
