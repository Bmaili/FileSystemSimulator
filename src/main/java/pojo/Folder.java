package pojo;

import java.io.Serializable;
import java.util.ArrayList;

//目录文件
public class Folder extends OsFile implements Serializable {
    public ArrayList<OsFile> fileList = new ArrayList<>();

    public Folder(String filename, INode iNode) {
        super(filename, iNode);
    }
}
