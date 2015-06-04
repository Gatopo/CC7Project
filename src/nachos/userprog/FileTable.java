package nachos.userprog;

import java.util.Hashtable;

/**
 * Created by steven on 3/06/15.
 */
public class FileTable {

    public FileTable(){
        this.informationFileTable = new Hashtable<String, FileAdditionalInfo>();
    }

    public int addFile(String name) {
        FileAdditionalInfo fai = informationFileTable.get(name);
        if (fai != null){
            if(fai.unlink)
                return -1;
            else{
                fai.addProcessUsingFile();
                return 0;
            }
        }else{
            fai = new FileAdditionalInfo();
            informationFileTable.put(name, fai);
            return 0;
        }
    }

    public int closeFile(String name){
        FileAdditionalInfo fai = informationFileTable.get(name);
        if(fai != null){
            int puf = fai.removeProcessUsingFile();
            if (!fai.unlink && puf == 0)
                informationFileTable.remove(name);
        }
        return -1;
    }

    public boolean unlinkFile(String name){
        FileAdditionalInfo fai = informationFileTable.get(name);
        if(fai != null){
            if(fai.processesUsingFile == 0){
                informationFileTable.remove(name);
                return true;
            }else{
                fai.unlink = true;
                fai.isBlocked = true;
                return false;
            }
        }
        return true;
    }

    public boolean isFileBlocked(String name){
        if (name == null)
            return true;
        FileAdditionalInfo fai = informationFileTable.get(name);
        if (fai != null) {
            return fai.isBlocked;
        }
        return false;
    }

    public boolean isPossibleUnlinkFile(String name){
        FileAdditionalInfo fai = informationFileTable.get(name);
        if(fai != null && fai.processesUsingFile == 0){
            return fai.unlink;
        }
        return false;
    }

    private class FileAdditionalInfo
    {
        FileAdditionalInfo(){
            this.unlink = false;
            this.processesUsingFile = 1;
            this.isBlocked = false;
        }

        int addProcessUsingFile(){
            processesUsingFile += 1;
            return processesUsingFile;
        }

        int removeProcessUsingFile(){
            processesUsingFile -= 1;
            return processesUsingFile;
        }

        private boolean unlink;
        private int processesUsingFile;
        protected boolean isBlocked;
    }

    private Hashtable<String, FileAdditionalInfo> informationFileTable;
}
