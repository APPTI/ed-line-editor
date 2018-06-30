package edLineEditor;

import java.io.*;
import java.util.ArrayList;

/**
 * 文件的操作类
 * 
 * 
 *
 */

public class fileOperate {
    //用来记录最后一次输入的S命令
    private String lastScmd;
    public void setLastScmd(String lastScmd) {
        this.lastScmd = lastScmd;
    }
    public String getLastScmd(){
        return lastScmd;
    }

    //用来记录程序在开始的时候是否已经调用了ed命令
    public boolean iseded = false;

    //用来查看命令中的参数是否非法（结束行在开始行之前），如果非法，则这个参数为false，在实现的时候将会对这个参数进行判断，如果为false则输出"？"
    private boolean Matchjudge;
    public void setMatchjudge(boolean a){
        this.Matchjudge = a;
    }
    public boolean getMatchjudge(){
        return this.Matchjudge;
    }

    //用来记载缓存区的内容是否被改变，关系到u命令的具体实现
    private boolean ischanged = false;
    public void changed(){
        this.ischanged = true;
    }
    public void nonchange(){
        this.ischanged = false;
    }
    public boolean getischanged(){
        return this.ischanged;
    }

    //用来记录缓存区的内容是否发生改动，涉及到q命令的实现
    private boolean qischanged = false;
    public void qchanged(){
        this.qischanged = true;
    }
    public boolean getqchanged(){
        return qischanged;
    }

    //状态量：是否保存，调用w或者W方法的时候会被设置为true
    Boolean issaved = false;
    //状态量：调用q命令的次数，关系到q命令的实现
    int qtimes = 0;

    //逐行保存文件内容,将文件的内容按照行逐行保存进一个list里面
    public ArrayList contentlist = new ArrayList();
    public Object getcontentlist(){
        return this.contentlist;
    }
    public void setInLines() throws IOException {
        File testfile = new File(getCurrentfile());
        if (testfile.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(getCurrentfile()));
            do {
                line l = new line();
                l.linecontent = bufferedReader.readLine();
                if (l.linecontent != null){
                    contentlist.add(l);
                }
                else break;
            }while (true);
            return;
        }
    }

    //一个用于统计文件目前行数的方法
    public int listlength(){
        return this.contentlist.size();
    }

    //操作缓存区的开始的那一行
    private int Startline = contentlist.size();
	public void setStartline(int a){
	    this.Startline = a;
    }
    public int getStartline(){
	    return Startline;
    }

    //操作缓存区的结束的那一行
    private int Endline = getStartline()+1;
    public void setEndline(int endline) {
        Endline = endline;
    }
    public int getEndline(){
	    return this.Endline;
    }

    //目标行,用来匹配特定行内容是否相等
    private String TargetLine = null;
    public void setTargetLine(String s){
        this.TargetLine = s;
    }
    public String getTargetLine(){
        return this.TargetLine;
    }

	//程序默认要操作的文件的名称
    private String currentfile = null;
    public void setCurrentfile(String a){
        this.currentfile = a;
        return;
    }
    public String getCurrentfile(){
        return this.currentfile;
    }

    //程序上一次执行的s指令
    private String lastscommend;
    public void setLastscommend(String s){
        lastScmd = s;
    }








/*
/以下命令的具体实现，是直接基于缓存区对于缓存区的list的操作。主要的参数为开始行和结束行（具体操作从哪一行开始，到哪一行结束）
要调用的时候，要事先在ANALYSIS类里面根据命令的地址部分先解析出操作从哪一行开始到那一行结束，然后直接调用这里的方法。
 */




    //a命令
	public void doa() throws IOException {
	    //读入要加进来的内容
        ArrayList inputString = EDLineEditor.I.readContent();
        int i = getStartline();
        for (Object x : inputString) {
            contentlist.add(i, x);
            i++;
        }
        setStartline(i);
        setEndline(getStartline());
        changed();
        qchanged();
	}

	//i命令
    public void doi() throws IOException{
        //读入要加进来的内容
        ArrayList inputString = EDLineEditor.I.readContent();
        int i = getStartline() - 1;
        if (getStartline()== 0){
            i = 0;
        }
        for (Object x : inputString) {
            contentlist.add(i,x);
            i ++;
        }
        setStartline(i);
        setEndline(getStartline());
        changed();
        qchanged();
    }

    //c命令
    public void doc(){
        //读入新的输入内容
        ArrayList inputString = EDLineEditor.I.readContent();
        //删除指定行
        if (getEndline() > listlength()){
            System.out.println("?");
        }
        else if (contentlist.size() != 0) {
            for (int i = getEndline(); i >= getStartline(); i--) {
                contentlist.remove(i - 1);
            }
            //增加新内容
            int i = getStartline();
            for (Object x : inputString) {
                contentlist.add(i - 1, x);
                i++;
            }
            setStartline(i);
            setEndline(getStartline());
            changed();
            qchanged();
        }
        else {
            System.out.println("?");
        }

    }

    //d命令
    public void dod(){
        if (contentlist.size() == 0){
            System.out.println("?");
        }
        else if (getEndline() > listlength()){
            System.out.println("?");
        }
        else {
            //不是最后一行
            if (getEndline() != listlength()) {
                for (int i = getEndline(); i >= getStartline(); i--) {
                    contentlist.remove(i - 1);
                }
                setStartline(getStartline());
                setEndline(getStartline());
                changed();
                qchanged();
            } else {
                for (int i = getEndline(); i >= getStartline(); i--) {
                    contentlist.remove(i - 1);
                }
                setStartline(listlength());
                setEndline(getStartline());
                changed();
                qchanged();
            }
        }
    }

    //p指令
    public void dop(){
	    if (contentlist.size() != 0) {
	        if (getStartline() <= getEndline()) {
                for (int i = getStartline(); i <= getEndline(); i++) {
                    line temp = (line) contentlist.get(i - 1);
                    System.out.println(temp.linecontent);
                }
                //当前行设定为最后一行
                setStartline(getEndline());
            }
        }
        else System.out.println("?");
    }

    //=指令
    public void doequal(){
	    System.out.println(getStartline());
    }

    //z命令（两种实现方式，一种后面带参数，一种后面不带参数）
    public void doz(){
        for (int i = getStartline(); i <= getEndline(); i++) {
            line temp = (line) contentlist.get(i - 1);
            System.out.println(temp.linecontent);
        }
    }
    public void doz(int n){
        if (getStartline() + n >= listlength()){
            for (int i = getStartline(); i <= listlength(); i++) {
                line temp = (line) contentlist.get(i - 1);
                System.out.println(temp.linecontent);
            }
        }
        else {
            setEndline(getStartline() + n);
            for (int i = getStartline(); i <= getEndline(); i++) {
                line temp = (line) contentlist.get(i - 1);
                System.out.println(temp.linecontent);
            }
        }

    }

    //f命令（两种实现方式）
    public void dof1(){
	    if (getCurrentfile() == null){
	        System.out.println("?");
        }
        else {
	        System.out.println(getCurrentfile());
        }
        return;
    }
    public void dof2(String name){
	    this.setCurrentfile(name);
	    return;
    }

    //w命令（两种实现方式）
    public void dow1(String locate) throws IOException {
            FileWriter fw = new FileWriter(locate, false);
            for (int i = getStartline(); i <= getEndline(); i++) {
                line temp = (line) contentlist.get(i - 1) ;
                fw.write(temp.getLinecontent() + "\n");
                fw.flush();
            }
            issaved = true;
    }
    public void dow2() throws IOException {
	    if (getCurrentfile() != null) {
            FileWriter fw = new FileWriter(getCurrentfile());
            for (int i = getStartline(); i <= getEndline(); i++) {
                line temp = (line) contentlist.get(i - 1) ;
                fw.write(temp.getLinecontent() + "\n");
                fw.flush();
            }
            issaved = true;
        }
        else System.out.println("?");
    }

    //W命令（两种实现方式）
    public void doW(String locate) throws IOException {
            FileWriter fw = new FileWriter(locate, true);
            for (int i = getStartline(); i <= getEndline(); i++) {
                line temp = (line) contentlist.get(i - 1) ;
                fw.write(temp.getLinecontent() + "\n");
                fw.flush();
            }
            issaved = true;
    }
    public void doW() throws IOException {
	    if (getCurrentfile() != null) {
            FileWriter fileWriter = new FileWriter(getCurrentfile(), true);
            for (int i = getStartline(); i <= getEndline(); i++) {
                line temp = (line) contentlist.get(i - 1) ;
                fileWriter.write(temp.getLinecontent() + "\n");
                fileWriter.flush();
            }
            issaved = true;
        }else System.out.println("?");
    }

    //m
    public void dom(int finalline){
        ArrayList temp = new ArrayList();
        for (int i = getEndline() ; i >= getStartline() ; i --){
            temp.add(0,contentlist.get(i - 1));
        }
        int j = finalline;
        int k = 0 ;
        for (j = finalline; j < finalline + temp.size() ; j ++){
            contentlist.add(j,temp.get(k));
            k++;
        }
        for (int i = getEndline() ; i >= getStartline() ; i --){
            contentlist.remove(i - 1);
        }
        setStartline(j - temp.size());
        changed();
        qchanged();
    }

    //t
    public void dot(int finalline){
        ArrayList temp = new ArrayList();
        for (int i = getEndline() ; i >= getStartline() ; i --){
            temp.add(0,contentlist.get(i - 1));
        }
        int j = finalline;
        int k = 0 ;
        for (j = finalline; j < finalline + temp.size() ; j ++){
            contentlist.add(j,temp.get(k));
            k++;
        }
        setStartline(j);
        changed();
        qchanged();
    }

    //j
    public void doj(){
        if (getEndline() > listlength()){
            System.out.println("?");
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = getStartline(); i <= getEndline(); i++) {
                line templine = (line) contentlist.get(i - 1);
                String temp = templine.linecontent;
                sb.append(temp);
            }
            for (int i = getEndline(); i >= getStartline(); i--) {
                contentlist.remove(i - 1);
            }
            contentlist.add(getStartline() - 1, new line(sb.toString()));
            changed();
            qchanged();
        }
    }

    //s，传入参数
    public void dos(String str, String rstr , int count){
        boolean isremoved = false;
        int slastline = getStartline();
        for (int i = getStartline() ; i <= getEndline() ; i ++){
            line templine = (line) contentlist.get(i - 1);
            String templinecontent = templine.getLinecontent();
            if (INDEXOF(templinecontent,str) >= count ) {
                    String laststring = doseachline(templinecontent, str, rstr, count);
                    templine.setLinecontent(laststring);
                    contentlist.remove(i - 1);
                    contentlist.add(i - 1, templine);
                    isremoved = true;
                    slastline = i;
            }
            setStartline(slastline);
        }
        if (!isremoved){
            System.out.println("?");
        }
        changed();
        qchanged();
    }


    /*
    /下面三个方法是为了帮助s命令的实现而写的方法
     */
    //替换一句中所有的字符串
    public void dosall(String str, String rstr ){
        boolean isremoved = false;
        int lastsline = getStartline();
        for (int i = getStartline() ; i <= getEndline() ; i ++){
            line templine = (line) contentlist.get(i - 1);
            String templinecontent = templine.getLinecontent();
            if (templinecontent.indexOf(str) != -1) {
                String laststring = templinecontent.replaceAll(str, rstr);
                templine.setLinecontent(laststring);
                contentlist.remove(i - 1);
                contentlist.add(i - 1, templine);
                isremoved = true;
                lastsline = i;
            }
        }
        setStartline(lastsline);
    }
    //在指定行替换第几个字符串
    public String doseachline(String initstring ,String str1, String str2, int count) {
        //str1是要被替换的字符串，str2是用来替换的字符串
        int ind = initstring.indexOf(str1);
        int i = 1;
        while (i < count) {
            ind = ind + str1.length();
            ind = initstring.indexOf(str1,ind);
            if (ind != -1) {
                i++;
            }
            else break;
        }
        String sb0 = initstring.substring(0,ind);
        String sb1 = initstring.substring(ind, ind+str1.length());
        String sb2 = initstring.substring(ind+str1.length());
        String sb3 = sb0 + sb1.replace(str1,str2)+sb2;
        return sb3;
    }
    //计算一个句子里特定字符串出线的次数
    public int INDEXOF(String str1, String str2){
        int result = 0;
        int ind = str1.indexOf(str2);
        while (ind != -1){
            ind = ind + str2.length();
            ind = str1.indexOf(str2,ind);
            result ++;
        }
        return result;
    }



}

