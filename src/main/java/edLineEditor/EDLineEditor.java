package edLineEditor;

import java.io.IOException;
import java.util.ArrayList;

public class EDLineEditor {
	
	/**
	 * 接收用户控制台的输入，解析命令，根据命令参数做出相应处理。
	 * 不需要任何提示输入，不要输出任何额外的内容。
	 * 输出换行时，使用System.out.println()。或者换行符使用System.getProperty("line.separator")。
	 * 
	 * 待测方法为public static void main(String[] args)方法。args不传递参数，所有输入通过命令行进行。
	 * 方便手动运行。
	 * 
	 * 说明：可以添加其他类和方法，但不要删除该文件，改动该方法名和参数，不要改动该文件包名和类名
	 */
    static Input I = new Input();

	public static void main(String[] args) {
	    try {
	        I.getScanner().hasNext();
        }catch (IllegalStateException e ){
	        I = new Input();
        }

        /**
         * 先进行命令的读入，然后在根据特定的命令进行解析与跳转
         * if-else
         */
        try {
        //Analysis类实例，持有一个fileOperater类的实例，用来对文件进行操作
        Analysis A = new Analysis();
        history H = new history();
        //最初的版本,版本0
        while (I.scanner.hasNext()){
            String cmd = I.readCommand();
            //检查是否进行撤销操作
            if (cmd.equals("u")){
                H.delversion();
                A.fO.contentlist = H.gethy();
            }
            //检查是否q
            else if (cmd.equals("q")){
                //若文本没有改动
                if (A.fO.getqchanged() == false){
                    I.scanner.close();
                    H.refersh();
                    break;
                }
                //若已经进行保存
                if (A.fO.issaved){
                    I.scanner.close();
                    H.refersh();
                    break;
                }
                //若按了两次q
                else if (A.fO.issaved == false && A.fO.qtimes != 0 ){
                    I.scanner.close();
                    H.refersh();
                    break;
                }
                else {
                    System.out.println("?");
                    A.fO.qtimes++;
                }

            }
            else if (cmd.equals("Q")){
                I.scanner.close();
                H.refersh();
                break;
            }
            else {
                //进入命令分析、内容操作入口
                A.checkCmd(cmd);

                //若是第一条ed命令，则对其内容进行初始化
                if (cmd.matches("ed (.+)")){
                    H.appendhistory(A.fO.contentlist);
                    A.fO.nonchange();
                }
                //如果内容发生改变，则更新历史库
                else if (A.fO.getischanged()){
                    H.appendhistory(A.fO.contentlist);
                    A.fO.nonchange();
                }
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
}
