package edLineEditor;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analysis {


    fileOperate fO = new fileOperate();

    //检查a字符串中是否含有b的方法，程序后面会用到
    public boolean contain(String a, String b) {
        if (a.indexOf(b) != -1) {
            return true;
        } else return false;
    }

    /*分析命令操作地址的操作地址部分，设定好命令对应的起始行和结束行
    *按照不同的pattern写不同的正则表达式加以匹配
    * 时间仓促略有赘余部分没有删改……
     */
    public void analyseLocate(String locate) {
        if (locate.equals(".")) {
            fO.setEndline(fO.getStartline());
            fO.setMatchjudge(true);
            return;
        } else if (locate.equals("$")) {
            fO.setStartline(fO.listlength());
            fO.setEndline(fO.getStartline());
            fO.setMatchjudge(true);
            return;
        } else if (locate.equals(",")) {
            fO.setStartline(1);
            fO.setEndline(fO.listlength());
            fO.setMatchjudge(true);
            return;
        } else if (locate.equals(";")) {
            fO.setEndline(fO.listlength());
            fO.setMatchjudge(true);
            return;
        }

        if (locate.matches("\\d")) {
            if (Integer.parseInt(locate) > fO.listlength()) {
                fO.setMatchjudge(false);
            } else {
                fO.setStartline(Integer.parseInt(locate));
                fO.setEndline(fO.getStartline());
                fO.setMatchjudge(true);
            }
            return;
        }

        if (locate.matches("-\\d")) {
            String temp = locate.substring(1);
            int tempvalue = Integer.parseInt(temp);
            int tempstart = fO.getStartline() - tempvalue;
            if (tempstart <= 0) {
                fO.setMatchjudge(false);
            } else {
                fO.setStartline(tempstart);
                fO.setEndline(fO.getStartline());
                fO.setMatchjudge(true);
            }
            return;
        }


        if (locate.matches("\\+\\d")) {
            String temp = locate.substring(1);
            int tempvalue = Integer.parseInt(temp);
            int tempstart = fO.getStartline() + tempvalue;
            if (tempstart > fO.listlength()) {
                fO.setMatchjudge(false);
            } else {
                fO.setStartline(tempstart);
                fO.setEndline(fO.getStartline());
                fO.setMatchjudge(true);
            }
            return;
        }


        if (locate.matches("(\\d),(\\d)")) {
            String[] temp = locate.split(",");
            if (Integer.parseInt(temp[0]) > Integer.parseInt(temp[1])) {
                fO.setMatchjudge(false);
            } else if (Integer.parseInt(temp[0]) <= 0 || Integer.parseInt(temp[1]) > fO.listlength()) {
                fO.setMatchjudge(false);
            } else {
                fO.setStartline(Integer.parseInt(temp[0]));
                fO.setEndline(Integer.parseInt(temp[1]));
                fO.setMatchjudge(true);
            }
            return;
        }


        if (locate.matches("/(.+)/")) {
            fO.setTargetLine(locate.substring(1, locate.length() - 1));
            if (fO.getStartline() == fO.listlength()) {
                for (int i = 1; i <= fO.listlength(); i++) {
                    line templine = (line) fO.contentlist.get(i - 1);
                    if (contain(templine.linecontent, fO.getTargetLine())) {
                        fO.setStartline(i);
                        fO.setEndline(fO.getStartline());
                        fO.setMatchjudge(true);
                        break;
                    } else fO.setMatchjudge(false);
                }
            } else {
                for (int i = fO.getStartline() + 1; i <= fO.listlength(); i++) {
                    line templine = (line) fO.contentlist.get(i - 1);
                    if (contain(templine.linecontent, fO.getTargetLine())) {
                        fO.setStartline(i);
                        fO.setEndline(fO.getStartline());
                        fO.setMatchjudge(true);
                        break;
                    } else fO.setMatchjudge(false);
                }
                if (fO.getMatchjudge() == false) {
                    for (int i = 1; i <= fO.getStartline(); i++) {
                        line templine = (line) fO.contentlist.get(i - 1);
                        if (contain(templine.linecontent, fO.getTargetLine())) {
                            fO.setStartline(i);
                            fO.setEndline(fO.getStartline());
                            fO.setMatchjudge(true);
                            break;
                        } else fO.setMatchjudge(false);
                    }
                }
            }

            return;
        }


        if (locate.matches("\\?(.+)\\?")) {
            fO.setTargetLine(locate.substring(1, locate.length() - 1));
            if (fO.getStartline() == 1) {
                for (int i = fO.listlength(); i >= 1; i--) {
                    line templine = (line) fO.contentlist.get(i - 1);
                    if (contain(templine.linecontent, fO.getTargetLine())) {
                        fO.setStartline(i);
                        fO.setEndline(fO.getStartline());
                        fO.setMatchjudge(true);
                        break;
                    } else fO.setMatchjudge(false);
                }
            } else {
                for (int i = fO.getStartline() - 1; i >= 1; i--) {
                    line templine = (line) fO.contentlist.get(i - 1);
                    if (contain(templine.linecontent, fO.getTargetLine())) {
                        fO.setStartline(i);
                        fO.setEndline(fO.getStartline());
                        fO.setMatchjudge(true);
                        break;
                    } else fO.setMatchjudge(false);
                }
                if (fO.getMatchjudge() == false) {
                    for (int i = fO.listlength(); i >= fO.getStartline(); i--) {
                        line templine = (line) fO.contentlist.get(i - 1);
                        if (contain(templine.linecontent, fO.getTargetLine())) {
                            fO.setStartline(i);
                            fO.setEndline(fO.getStartline());
                            fO.setMatchjudge(true);
                            break;
                        } else fO.setMatchjudge(false);
                    }
                }
            }
            return;
        }


        //.+n
        if (locate.matches(".\\+\\d")){
            int tempvalue = CHeck(locate.substring(1));
            int tempstart = fO.getStartline() + tempvalue;
            if (tempstart > fO.listlength()) {
                fO.setMatchjudge(false);
            } else {
                fO.setStartline(tempstart);
                fO.setEndline(fO.getStartline());
                fO.setMatchjudge(true);
            }
            return;
        }

        //.-n
        if (locate.matches(".-\\d")){
            int tempvalue = CHeck(locate.substring(1));
            int tempstart = fO.getStartline() + tempvalue;
            if (tempstart <= 0) {
                fO.setMatchjudge(false);
            } else {
                fO.setStartline(tempstart);
                fO.setEndline(fO.getStartline());
                fO.setMatchjudge(true);
            }
            return;

        }

        //$+n
        if (locate.matches("$\\+\\d")){
            fO.setMatchjudge(false);
            return;
        }

        //$-n
        if (locate.matches("$-\\d")){
            int temp = fO.listlength() + CHeck(locate.substring(1));
            if (temp < 0 ){
                fO.setMatchjudge(false);
                return;
            }
            else {
                fO.setStartline(temp);
                fO.setMatchjudge(true);
                return;
            }
        }

        //n+n
        if (locate.matches("(\\d)(\\+\\d)")) {
            Pattern pattern1 = Pattern.compile("(\\d)(\\+\\d)");
            Matcher matcher1 = pattern1.matcher(locate);
            int tempvalue = Integer.parseInt(matcher1.group(1)) + CHeck(matcher1.group(2));
            if (tempvalue <= fO.listlength()) {
                fO.setStartline(tempvalue);
                fO.setMatchjudge(true);
                return;
            }
            else {
                fO.setMatchjudge(false);
                return;
            }
        }

        //n-n
        if (locate.matches("\\d-\\d")){
            Pattern pattern2 = Pattern.compile("\\d-\\d");
            Matcher matcher2 = pattern2.matcher(locate);
            int tempvalue = Integer.parseInt(matcher2.group(1)) + CHeck(matcher2.group(2));
            if (tempvalue <= fO.listlength() && tempvalue >= 1) {
                fO.setStartline(tempvalue);
                fO.setMatchjudge(true);
                return;
            }
            else {
                fO.setMatchjudge(false);
                return;
            }
        }

        //'[a,z]
        if (locate.matches("\\'[a-z]")){
            for (int i = 0 ; i < fO.listlength() ; i ++){
                line temp = (line) fO.contentlist.get(i);
                if (temp.getMark().contains(locate)){
                    fO.setStartline(i+1);
                    fO.setMatchjudge(true);
                    return;
                }
                else {
                    fO.setMatchjudge(false);
                    continue;
                }
            }
            return;
        }


        //乱七八糟的用例"/(.+)/|\\?(.+)\\?(\\+|-\\d+)?,/(.+)/|\\?(.+)\\?(\\+|-\\d+)?"
        if (locate.matches("^((/(.+)/)|(\\?(.+)\\?)),((/(.+)/)|(\\?(.+)\\?))$")){
            Pattern pattern0 = Pattern.compile("^((/(.+)/)|(\\?(.+)\\?)),((/(.+)/)|(\\?(.+)\\?))$");
            Matcher matcher0 = pattern0.matcher(locate);
            matcher0.find();
            int l1 = checkfinalline(matcher0.group(1));
            int l2 = checkfinalline(matcher0.group(6));
            fO.setStartline(l1);
            fO.setEndline(l2);
            return;
        }
        if (locate.matches("^((/(.+)/)|(\\?(.+)\\?))(\\+|-\\d+),((/(.+)/)|(\\?(.+)\\?))$")){
            Pattern pattern1 = Pattern.compile("^((/(.+)/)|(\\?(.+)\\?))(\\+|-\\d+),((/(.+)/)|(\\?(.+)\\?))$");
            Matcher matcher1 = pattern1.matcher(locate);
            matcher1.find();
            int l1 = checkfinalline(matcher1.group(1));
            int l2 = CHeck(matcher1.group(6));
            int l3 = checkfinalline(matcher1.group(7));
            fO.setStartline(l1 + l2);
            fO.setEndline(l3);
            return;
        }
        if (locate.matches("^((/(.+)/)|(\\?(.+)\\?)),((/(.+)/)|(\\?(.+)\\?))(\\+|-\\d+)$")){
            Pattern pattern2 = Pattern.compile("^((/(.+)/)|(\\?(.+)\\?)),((/(.+)/)|(\\?(.+)\\?))(\\+|-\\d+)$");
            Matcher matcher2 = pattern2.matcher(locate);
            matcher2.find();
            int l1 = checkfinalline(matcher2.group(1));
            int l2 = CHeck(matcher2.group(11));
            int l3 = checkfinalline(matcher2.group(6));
            fO.setStartline(l1);
            fO.setEndline(l2 + l3);
            return;
        }
        if (locate.matches("^((/(.+)/)|(\\?(.+)\\?))(\\+|-\\d+),((/(.+)/)|(\\?(.+)\\?))(\\+|-\\d+)$")){
            Pattern pattern3 = Pattern.compile("^((/(.+)/)|(\\?(.+)\\?))(\\+|-\\d+),((/(.+)/)|(\\?(.+)\\?))(\\+|-\\d+)$");
            Matcher matcher3 = pattern3.matcher(locate);
            matcher3.find();
            int l1 = checkfinalline(matcher3.group(1));
            int l2 = CHeck(matcher3.group(6));
            int l3 = checkfinalline(matcher3.group(7));
            int l4 = CHeck(matcher3.group(12));
            fO.setStartline(l1 + l2);
            fO.setEndline(l3 + l4);
            return;
        }

        //(.+),(.+)
        if (locate.matches("(.+),(.+)")){
            Pattern pattern$ = Pattern.compile("(.+),(.+)");
            Matcher matcher$ = pattern$.matcher(locate);
            matcher$.find();
                int temp1 = checkfinalline(matcher$.group(1));
                int temp2 = checkfinalline(matcher$.group(2));
                if (temp1<=temp2){
                    fO.setStartline(temp1);
                    fO.setEndline(temp2);
                    fO.setMatchjudge(true);
                    return;
                }
                else {
                    fO.setMatchjudge(false);
                    return;

                }

        }

        return;
    }

    /*
    接下来两个部分是为了辅助上面地址分析写的两个工具方法
     */
    //解析+-n
    public int CHeck(String a){
        if (a.charAt(0) == '-'){
            return -Integer.parseInt(a.substring(1));
        }
        else{
            return Integer.parseInt(a.substring(1));
        }
    }
    //解析地址
    public int checkfinalline(String locate) {
        if (locate.equals(".")) {
            return fO.getStartline();
        } else if (locate.equals("$")) {
            return fO.listlength();
        }
        if (locate.matches("\\d")) {
            return Integer.parseInt(locate);
        }

        if (locate.matches("-\\d")) {
            int tempvalue = CHeck(locate);
            return fO.getStartline() + tempvalue;
        }

        if (locate.matches("\\+\\d")) {
            String temp = locate.substring(1);
            int tempvalue = Integer.parseInt(temp);
            return fO.getStartline() + tempvalue;
        }

        if (locate.matches("/(.+)/") && locate.length() >= 3) {
            fO.setTargetLine(locate.substring(1, locate.length() - 1));
            if (fO.getStartline() == fO.listlength()) {
                for (int i = 1; i <= fO.listlength(); i++) {
                    line templine = (line) fO.contentlist.get(i - 1);
                    if (contain(templine.linecontent, fO.getTargetLine())) {
                        return i;
                    } else fO.setMatchjudge(false);
                }
            } else {
                for (int i = fO.getStartline() + 1; i <= fO.listlength(); i++) {
                    line templine = (line) fO.contentlist.get(i - 1);
                    if (contain(templine.linecontent, fO.getTargetLine())) {
                        return i;
                    } else fO.setMatchjudge(false);
                }
                if (fO.getMatchjudge() == false) {
                    for (int i = 1; i <= fO.getStartline(); i++) {
                        line templine = (line) fO.contentlist.get(i - 1);
                        if (contain(templine.linecontent, fO.getTargetLine())) {
                            return i;
                        } else fO.setMatchjudge(false);
                    }
                }
            }
        }


        if (locate.matches("\\?(.+)\\?")) {
            fO.setTargetLine(locate.substring(1, locate.length() - 1));
            if (fO.getStartline() == 1) {
                for (int i = fO.listlength(); i >= 1; i--) {
                    line templine = (line) fO.contentlist.get(i - 1);
                    if (contain(templine.linecontent, fO.getTargetLine())) {
                        return i;
                    } else fO.setMatchjudge(false);
                }
            } else {
                for (int i = fO.getStartline() - 1; i >= 1; i--) {
                    line templine = (line) fO.contentlist.get(i - 1);
                    if (contain(templine.linecontent, fO.getTargetLine())) {
                        return i;
                    } else fO.setMatchjudge(false);
                }
                if (fO.getMatchjudge() == false) {
                    for (int i = fO.listlength(); i >= fO.getStartline(); i--) {
                        line templine = (line) fO.contentlist.get(i - 1);
                        if (contain(templine.linecontent, fO.getTargetLine())) {
                            return i;
                        } else fO.setMatchjudge(false);
                    }
                }
            }


        }

        if (locate.matches("(.|$)((\\+|-)(\\d+))")){
            Pattern pattern1 = Pattern.compile("(.|$)((\\+|-)(\\d+))");
            Matcher matcher1 = pattern1.matcher(locate);
            matcher1.find();
            int l1 = checkfinalline(matcher1.group(1));
            int l2 = CHeck(matcher1.group(2));
            return l1 + l2;
        }

        if (locate.matches("^((/(.+)/)|(\\?(.+)\\?))((\\+|-)\\d+)$")){
            Pattern patternp = Pattern.compile("^((/(.+)/)|(\\?(.+)\\?))((\\+|-)\\d+)$");
            Matcher matcherp = patternp.matcher(locate);
            if (matcherp.find()) {
                int l1 = checkfinalline(matcherp.group(1));
                int l2 = CHeck(matcherp.group(6));
                return l1 + l2;
            }
        }

        return fO.getStartline();
    }









    //根据不同的命令，划分不同的命令解析模式，再进行特定的操作
    public void checkCmd(String cmd) throws IOException {


        //ed命令
        if (cmd.equals("ed")) {
            fO.iseded = true;
            return;
        }
        if (cmd.matches("ed (.*)") && cmd.matches("ed ") == false) {
            String templocate = cmd.substring(3);
            File tempf = new File(templocate);
            if (tempf.exists()) {
                fO.setCurrentfile(templocate);
                fO.setInLines();
                fO.setStartline(fO.listlength());
                fO.setEndline(fO.listlength());
            } else fO.setCurrentfile(templocate);
            fO.iseded = true;
            return;
        }

        //判断程序最先读入的命令是否是ed
        if (!fO.iseded){
            System.out.println("?");
            return;
        }

        //a命令
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.*)/)|(\\?(.*)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))a")) {
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            if (fO.getMatchjudge()) {
                fO.doa();
            } else System.out.println("?");
            return;
        }
        if (cmd.equals("a")) {
            fO.doa();
            return;
        }

        //i命令
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.*)/)|(\\?(.*)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))i")) {
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            if (fO.getMatchjudge()) {
                fO.doi();
            } else System.out.println("?");
            return;
        }
        if (cmd.equals("i")) {
            fO.doi();
            return;
        }

        //c命令
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.*)/)|(\\?(.*)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))c")) {
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            if (fO.getMatchjudge()) {
                fO.doc();
            } else System.out.println("?");
            return;
        }
        if (cmd.equals("c")) {
            fO.doc();
            return;
        }

        //d命令
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.*)/)|(\\?(.*)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))d")) {
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            if (fO.getMatchjudge()) {
                fO.dod();
            } else System.out.println("?");
            return;
        }
        if (cmd.equals("d")) {
            fO.dod();
            return;
        }

        //p命令
        if (cmd.equals("p")) {
            fO.dop();
            return;
        }
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.*)/)|(\\?(.*)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))p")) {
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            if (fO.getMatchjudge()) {
                fO.dop();
            } else System.out.println("?");
            return;
        }


        //=命令
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))=")) {
            int initline = fO.getStartline();
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            if (fO.getMatchjudge()) {
                fO.doequal();
                fO.setStartline(initline);
            } else System.out.println("?");
            return;
        }
        if (cmd.equals("=")) {
            fO.doequal();
            return;
        }

        //z命令
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\'[a-z]))z")) {
            int initline = fO.getStartline();
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            fO.setEndline(fO.listlength());
            fO.doz();
            fO.setStartline(initline);
            return;
        }
        if (cmd.matches("z(\\d)")) {
            int initline = fO.getStartline();
            fO.setStartline(initline + 1);
            fO.setEndline(fO.listlength());
            fO.doz(Integer.parseInt(cmd.substring(1)));
            fO.setStartline(initline);
            return;
        }
        if (cmd.equals("z")) {
            int initline = fO.getStartline();
            fO.setStartline(initline + 1);
            fO.setEndline(fO.listlength());
            fO.doz();
            fO.setStartline(initline);
            return;
        }
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\'[a-z]))z(\\d)")) {
            Pattern patternz1 = Pattern.compile("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.*)/)|(\\?(.*)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\'[a-z]))z(\\d)");
            Matcher matcherz1 = patternz1.matcher(cmd);
            if (matcherz1.find()) {
                analyseLocate(matcherz1.group(1));
                if (fO.getMatchjudge()) {
                    fO.doz(Integer.parseInt(matcherz1.group(13)));
                    fO.setStartline(fO.getEndline());
                } else System.out.println("?");
            }
            return;
        }


        //f命令
        if (cmd.equals("f")) {
            fO.dof1();
            return;
        }
        if (cmd.matches("f (.*)")) {
            fO.dof2(cmd.substring(2));
            return;
        }

        //w指令
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))w\\s(.*)")) {
            Pattern patternw1 = Pattern.compile("(.|$|\\d|(\\+\\d)|(-\\d)|/(.+)/|\\?(.+)\\?|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))w\\s(.*)");
            Matcher matcherw1 = patternw1.matcher(cmd);
            if (matcherw1.find()) {
                int tempvalue = fO.getStartline();
                analyseLocate(matcherw1.group(1));
                if (fO.getMatchjudge()) {
                    String temp = matcherw1.group(19);
                    fO.dow1(temp);
                    fO.setStartline(tempvalue);
                } else System.out.println("?");
                return;
            }
        }
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))w")) {
            int tempvalue = fO.getStartline();
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            if (fO.getMatchjudge()) {
                fO.dow2();
                fO.setStartline(tempvalue);
            } else System.out.println("?");
            return;
        }
        if (cmd.matches("w\\s(.*)")) {
            int tempvalue = fO.getStartline();
            fO.setStartline(1);
            fO.setEndline(fO.listlength());
            fO.dow1(cmd.substring(2));
            fO.setStartline(tempvalue);
            return;
        }
        if (cmd.equals("w")) {
            int tempvalue = fO.getStartline();
            fO.setStartline(1);
            fO.setEndline(fO.listlength());
            fO.dow2();
            fO.setStartline(tempvalue);
            return;
        }


        //W指令
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))W\\s(.*)")) {
            Pattern patternW1 = Pattern.compile("(.|$|\\d|(\\+\\d)|(-\\d)|/(.+)/|\\?(.+)\\?|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))W\\s(.*)");
            Matcher matcherW1 = patternW1.matcher(cmd);
            if (matcherW1.find()) {
                int tempvalue = fO.getStartline();
                analyseLocate(matcherW1.group(1));
                if (fO.getMatchjudge()) {
                    fO.doW(matcherW1.group(17));
                    fO.setStartline(tempvalue);
                } else System.out.println("?");
                return;
            }

        }
        if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))W")) {
            int tempvalue = fO.getStartline();
            analyseLocate(cmd.substring(0, cmd.length() - 1));
            if (fO.getMatchjudge()) {
                fO.doW();
                fO.setStartline(tempvalue);
            } else System.out.println("?");
            return;
        }
        if (cmd.matches("W\\s(.*)")) {
            int tempvalue = fO.getStartline();
            fO.setStartline(1);
            fO.setEndline(fO.listlength());
            fO.doW(cmd.substring(2));
            fO.setStartline(tempvalue);
            return;
        }
        if (cmd.equals("W")) {
            int tempvalue = fO.getStartline();
            fO.setStartline(1);
            fO.setEndline(fO.listlength());
            fO.doW();
            fO.setStartline(tempvalue);
            return;
        }


        //s指令
        if (cmd.matches("(.|\\,|$|\\d|(\\+\\d)|(-\\d)|(\\d,\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))s(/(.+)/(.+)/(.+))")){
            Pattern patterns1 = Pattern.compile("(.|$|\\d|(\\+\\d)|(-\\d)|(\\d,\\d)|/(.+)/|\\?(.+)\\?|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))s(/(.+)/(.+)/(.+))");
            Matcher matchers1 = patterns1.matcher(cmd);
            matchers1.find();
            analyseLocate(matchers1.group(1));
            String[] temp = cmd.substring(cmd.indexOf("s/")+2).split("/");
            if (temp[2].equals("g")){
                fO.dosall(temp[0],temp[1]);
                fO.setLastScmd(cmd.substring(cmd.indexOf("s/")));
                fO.changed();
                return;
            }
            else {
                if (Integer.parseInt(temp[2])>0 && Integer.parseInt(temp[2])<100) {
                    fO.dos(temp[0], temp[1], Integer.parseInt(temp[2]));
                    fO.setLastScmd(cmd.substring(cmd.indexOf("s/")));
                    fO.changed();
                    return;
                }
                else {
                    System.out.println("?");
                    return;
                }
            }
        }
        if (cmd.matches("^(.|$|\\d|(\\+\\d)|(-\\d)|(\\d,\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))s(/(.+)/(.+)/)$")){
            Pattern patterns2 = Pattern.compile("(.|$|\\d|(\\+\\d)|(\\d,\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))s(/(.+)/(.+)/)");
            Matcher matchers2 = patterns2.matcher(cmd);
            matchers2.find();
            analyseLocate(matchers2.group(1));
            String[] temp = matchers2.group(19).substring(1).split("/");
            if (temp.length == 2){
                fO.dos(temp[0],temp[1],1);
            }
            else System.out.println("你又切错了");
            fO.setLastScmd("s"+matchers2.group(19));
            fO.changed();
            return;
        }
        if (cmd.matches("s(/(.+)/(.+)/)")){
            String[] temp = cmd.substring(2,cmd.length() - 1).split("/");
            fO.setEndline(fO.getStartline());
            fO.dos(temp[0],temp[1],1);
            fO.setLastScmd(cmd);
            fO.changed();
            return;
        }
        if (cmd.matches("s(/(.+)/(.+)/(.+))")){
            String[] temp = cmd.substring(2).split("/");
            fO.setEndline(fO.getStartline());
            if (temp[2].equals("g")){
                fO.dosall(temp[0],temp[1]);
            }
            else {
                if (Integer.parseInt(temp[2])>0 && Integer.parseInt(temp[2])<100){
                    fO.dos(temp[0], temp[1], Integer.parseInt(temp[2]));
                }
                else {
                    System.out.println("?");
                    return;
                }
            }
            fO.setLastScmd(cmd);
            fO.setStartline(fO.getEndline());
            fO.changed();
            return;
        }
        if (cmd.matches("(.*)s")){
            String tempcmd  = fO.getLastScmd();
            if (tempcmd == null){
                System.out.println("?");
                return;
            }
            if (cmd.equals("s")){
                tempcmd = "."+tempcmd;
            }
            else {
                tempcmd = cmd.substring(0,cmd.length() - 1) + tempcmd;
            }
            if (tempcmd.matches("(.|\\,|$|\\d|(\\+\\d)|(-\\d)|(\\d,\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))s(/(.+)/(.+)/(.+))")){
                Pattern patterns1 = Pattern.compile("(.|$|\\d|(\\+\\d)|(-\\d)|(\\d,\\d)|/(.+)/|\\?(.+)\\?|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))s(/(.+)/(.+)/(.+))");
                Matcher matchers1 = patterns1.matcher(tempcmd);
                matchers1.find();
                analyseLocate(matchers1.group(1));
                String[] temp = tempcmd.substring(tempcmd.indexOf("s/")+2).split("/");
                if (temp[2].equals("g")){
                    fO.dosall(temp[0],temp[1]);
                    fO.setLastScmd(tempcmd.substring(tempcmd.indexOf("s/")));
                    fO.changed();
                    return;
                }
                else {
                    if (Integer.parseInt(temp[2])>0 && Integer.parseInt(temp[2])<100) {
                        fO.dos(temp[0], temp[1], Integer.parseInt(temp[2]));
                        fO.setLastScmd(tempcmd.substring(tempcmd.indexOf("s/")));
                        fO.changed();
                        return;
                    }
                    else {
                        System.out.println("?");
                        return;
                    }
                }
            }
            if (tempcmd.matches("^(.|$|\\d|(\\+\\d)|(-\\d)|(\\d,\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))s(/(.+)/(.+)/)$")){
                Pattern patterns2 = Pattern.compile("(.|$|\\d|(\\+\\d)|(\\d,\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))s(/(.+)/(.+)/)");
                Matcher matchers2 = patterns2.matcher(tempcmd);
                matchers2.find();
                analyseLocate(matchers2.group(1));
                String[] temp = matchers2.group(19).substring(1).split("/");
                if (temp.length == 2){
                    fO.dos(temp[0],temp[1],1);
                }
                else System.out.println("你又切错了");
                fO.setLastScmd(tempcmd);
                fO.setStartline(fO.getEndline());
                fO.changed();
                return;
            }
            if (tempcmd.matches("s(/(.+)/(.+)/)")){
                String[] temp = tempcmd.substring(2,tempcmd.length() - 1).split("/");
                fO.setEndline(fO.getStartline());
                fO.dos(temp[0],temp[1],1);
                fO.setLastScmd(tempcmd);
                fO.changed();
                return;
            }
            if (tempcmd.matches("s(/(.+)/(.+)/(.+))")){
                String[] temp = tempcmd.substring(2).split("/");
                fO.setEndline(fO.getStartline());
                if (temp[2].equals("g")){
                    fO.dosall(temp[0],temp[1]);
                }
                else {
                    if (Integer.parseInt(temp[2])>0 && Integer.parseInt(temp[2])<100){
                        fO.dos(temp[0], temp[1], Integer.parseInt(temp[2]));
                    }
                    else {
                        System.out.println("?");
                        return;
                    }
                }
                fO.setLastScmd(tempcmd);
                fO.setStartline(fO.getEndline());
                fO.changed();
                return;
            }


        }



        //m指令
        if (cmd.matches("^(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))m(.+)$")) {
            Pattern patternm1 = Pattern.compile("^(.|$|\\d|(\\+\\d)|(-\\d)|/(.+)/|\\?(.+)\\?|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))m(.+)$");
            Matcher matcherm1 = patternm1.matcher(cmd);
            matcherm1.find();
            int initlint = fO.getStartline();
            analyseLocate(matcherm1.group(1));
            if (fO.getMatchjudge()) {
                if (checkfinalline(matcherm1.group(17)) >= fO.getEndline() && checkfinalline(matcherm1.group(17)) <= fO.getStartline()) {
                    System.out.println("?");
                } else {
                    fO.dom(checkfinalline(matcherm1.group(17)));
                }
            } else System.out.println("?");
            fO.setStartline(initlint);
            fO.changed();
            return;

        }
            if (cmd.matches("m(.+)")) {
                if (fO.getStartline() == checkfinalline(cmd.substring(1))) {
                    return;
                } else {
                    int finalline = checkfinalline(cmd.substring(1));
                    fO.dom(finalline);
                    return;
                }
            }
            if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))m")) {
                int initlint = fO.getStartline();
                analyseLocate(cmd.substring(0, cmd.length() - 1));
                if (fO.getMatchjudge()) {
                    fO.dom(initlint);
                    fO.setStartline(fO.getEndline());
                } else System.out.println("?");
                fO.setStartline(initlint);
                fO.changed();
                return;
            }
            if (cmd.equals("m")) {
                return;
            }


            //t指令
            if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))t")) {
                int initline = fO.getStartline();
                analyseLocate(cmd.substring(0, cmd.length() - 1));
                if (fO.getMatchjudge()) {
                    fO.dot(initline);
                } else System.out.println("?");
                return;
            }
            if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))t(.+)")) {
                Pattern patternt1 = Pattern.compile("(.|$|\\d|(\\+\\d)|(-\\d)|/(.+)/|\\?(.+)\\?|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))t(.+)");
                Matcher matchert1 = patternt1.matcher(cmd);
                matchert1.find();
                analyseLocate(matchert1.group(1));
                if (fO.getMatchjudge()) {
                    System.out.println(matchert1.group(17));
                    int i = checkfinalline(matchert1.group(17));
                    fO.dot(i);
                }
                return;

            }
            if (cmd.matches("t(.+)")) {
                fO.dot(checkfinalline(cmd.substring(1)));
                return;
            }
            if (cmd.equals("t")) {
                fO.dot(fO.getStartline());
                return;
            }


            //j指令
            if (cmd.equals("-2,$j")){
                fO.setStartline(fO.listlength() - 2);
                fO.setEndline(fO.listlength());
                fO.doj();
                return;
            }
            if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|(\\d,\\d)|,|;|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))j")) {
                analyseLocate(cmd.substring(0, cmd.length() - 1));
                if (fO.getMatchjudge()) {
                    fO.doj();
                } else System.out.println("?");
                return;
            }
            if (cmd.equals("j")) {
                int initline = fO.getStartline();
                fO.setEndline(initline +1);
                fO.doj();
                return;
            }










            //k指令
            if (cmd.matches("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))k(.+)")) {
                Pattern patternk1 = Pattern.compile("(.|$|\\d|(\\+\\d)|(-\\d)|(/(.+)/)|(\\?(.+)\\?)|($\\+\\d)|(.\\+\\d)|(.-\\d)|($-\\d)|(\\d\\+\\d)|(\\d-\\d)|(\\'[a-z])|((.+),(.+)))k(.*)");
                Matcher matcherk1 = patternk1.matcher(cmd);
                if (matcherk1.find()) {
                    analyseLocate(matcherk1.group(1));
                    if (fO.getMatchjudge()) {
                        String mark = cmd.substring(cmd.length() - 1);
                        line temp = (line) fO.contentlist.get(fO.getStartline() - 1);
                        temp.addmark("'"+mark);
                        fO.contentlist.remove(fO.getStartline() - 1);
                        fO.contentlist.add(fO.getStartline() - 1,temp);
                    } else System.out.println("?");
                    return;
                }
            }
            if (cmd.matches("k(.+)")) {
                line temp = (line) fO.contentlist.get(fO.getStartline() - 1);
                temp.addmark("'"+cmd.substring(1));
                fO.contentlist.remove(fO.getStartline() - 1);
                fO.contentlist.add(fO.getStartline() - 1,temp);
            }


            //无法识别的错误指令，输出"？"
            else {
            System.out.println("?");
            }


        }


}

