package pojo;

import java.io.Serializable;

public class OsFile implements Serializable {
    public String filename;
    public INode iNode;

    public OsFile(String filename, INode iNode) {
        this.filename = filename;
        this.iNode = iNode;
    }

    @Override
    public String toString() {
        return filename;
    }


}


