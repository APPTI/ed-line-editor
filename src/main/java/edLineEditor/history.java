package edLineEditor;

import java.util.ArrayList;

public class history {
    //用来存放以往的contentlist的版本
    private static ArrayList<ArrayList> hy = new ArrayList<>();

    private int version = 0;
    //向版本组里面添加新的版本
    public void appendhistory(ArrayList k){
        hy.add(new ArrayList(k));
        this.version++;
    }
    public void delversion(){
        int a = hy.size();
        hy.remove(a - 1);
        this.version--;
    }


    public int getVersion(){
        return this.version;
    }
    public ArrayList gethy(){
        return hy.get(getVersion() - 1);
    }


    public void refersh(){
        version = 0;
        hy.clear();
    }

}
