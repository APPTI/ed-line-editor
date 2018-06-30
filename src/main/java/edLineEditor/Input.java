package edLineEditor;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 输入命令的类
 * 用于从控制台读入命令
 */
public class Input {
    Scanner  scanner;

    //针对测试用例进行的优化 不这么写的话会出现两个scanner导致no line found
    public Scanner getScanner(){
        return scanner;
    }
     public Input() {
        scanner = new Scanner(System.in);
    }

	//这个方法用于命令模式输入命令
    public  String readCommand(){
        String command = scanner.nextLine();
        return command;
    }
    
    //这个方法用于出入模式读入输入的内容
    public  ArrayList readContent() {
        ArrayList list = new ArrayList();
    	do {
    	    line l = new line();
    	    l.linecontent= scanner.nextLine();
    	    System.err.println(l.linecontent);
    	    if (!l.linecontent.equals(".")){
    	        list.add(l);
            }
            else break;
        }while (scanner.hasNextLine());
        return list;
	}

}
