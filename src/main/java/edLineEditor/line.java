package edLineEditor;

import java.util.ArrayList;
/*
/为了更好地实现k命令，建立了Line类。每一个line实例都应该具有本行的内容、本行持有的标记两个不同的属性
 */
public class line {
    //构造方法（两种实现方式）
    public line(){
        linecontent = null;
        mark = new ArrayList();
    }

    public line(String t){
        linecontent = t;
    }

    //行的内容属性
    public String linecontent;

    //返回文件指定行的内容
    public String getLinecontent(){
        return this.linecontent;
    }

    //设置文行的内容
    public void setLinecontent(String linecontent) {
        this.linecontent = linecontent;
    }

    //行的标记属性
    private ArrayList mark;

    //为行添加标记
    public void addmark(String a){
        mark.add(a);
    }

    //返回行目前持有的标记
    public ArrayList getMark() {
        return mark;
    }
}
