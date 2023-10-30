package domain;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.util.List;
import java.util.Scanner;
public class Account {

    private String ID;
    private String password;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    Scanner input = new Scanner(System.in);

    //根据ID查找位置的方法
    static public int findLocation(String inputID){
        ExcelReader excelReader = ExcelUtil.getReader("C:\\Users\\九年\\IdeaProjects\\MovieSystem\\账号信息.xlsx");
        List objects;

        for (int i = 1; i < excelReader.getRowCount(); i++) {

            objects = excelReader.readRow(i);            //依次读取每一行的数据
            if (inputID.equals(objects.get(1).toString())) {          //读取该行第二列的数据账号存在
                excelReader.close();
                return i;
            }
        }
        excelReader.close();
        return -1;
    }
    //检查密码是否符合规则
    public static boolean checkPassword(String inputpassword) {
        String password1 = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";
        if (inputpassword.matches(password1)) {
            System.out.println("成功！");
            return true;
        }
        System.out.println("失败！输入密码不符合规则，请重新输入！");
        System.out.println("重新输入密码：");
        return false;
    }
    //注册账号
    public static void registerAccount(){
        ExcelReader excelReader = ExcelUtil.getReader("C:\\Users\\九年\\IdeaProjects\\MovieSystem\\账号信息.xlsx");
        Scanner input = new Scanner(System.in);
        User user=new User();
        System.out.println("开始注册！");

        String inputID;
        boolean flag;
        List objects;
        do {
            flag=true;
            System.out.println("请输入您的ID：");
            inputID = input.next();

            for (int i = 1; i < excelReader.getRowCount(); i++) {
                objects = excelReader.readRow(i);            //依次读取每一行的数据
                if (inputID.equals(objects.get(1).toString())) {          //读取该行第二列的数据账号存在
                    System.out.println("用户ID已存在！请重新输入！");
                    flag=false;
                    break;
                }
            }
        } while(!flag);
        int rowCount=excelReader.getRowCount();
        excelReader.close();
        user.setID(inputID);


        System.out.println("请输入您的用户名：");
        user.setName(input.next());


        System.out.println("请输入您的密码：");
        String inputpassword;
        do{
            inputpassword=input.next();
            flag=Account.checkPassword(inputpassword);   //检查密码输入是否合法
        }while(!flag);
        user.setPassword(inputpassword);


        addAccount(user,rowCount);
        System.out.println("注册成功！");
    }
    //添加新账号
    public static void addAccount(User user,int rowCount){
        ExcelWriter excelWriter = ExcelUtil.getWriter("C:\\Users\\九年\\IdeaProjects\\MovieSystem\\账号信息.xlsx");

        excelWriter.writeCellValue(0, rowCount , "用户");
        excelWriter.writeCellValue(1, rowCount , user.getID());
        excelWriter.writeCellValue(2, rowCount , user.getPassword());
        excelWriter.writeCellValue(3, rowCount , user.getName());
        excelWriter.writeCellValue(4,rowCount,"铜牌用户");
        excelWriter.writeCellValue(5,rowCount,"0");
        excelWriter.writeCellValue(6,rowCount,"0");
        excelWriter.writeCellValue(7,rowCount,"NULL");
        excelWriter.writeCellValue(8,rowCount,"NULL");
        excelWriter.writeCellValue(9,rowCount,"NULL");
        excelWriter.writeCellValue(10,rowCount,"NULL");
        excelWriter.writeCellValue(11,rowCount,"NULL");

        excelWriter.flush();
        excelWriter.close();
    }

    //修改密码
    public static void changePassword(String inputID){

        Scanner input = new Scanner(System.in);
        ExcelWriter excelWriter = ExcelUtil.getWriter("C:\\Users\\九年\\IdeaProjects\\MovieSystem\\账号信息.xlsx");
        System.out.println("输入的密码长度应大于 8 个字符，且必须是大小写字母、数字和标点符号的组合");
        System.out.println("输入修改后的密码：");


        boolean sign;
        String inputpassword;

        do{
            inputpassword=input.next();
            sign=Account.checkPassword(inputpassword);   //检查密码输入是否合法
        }while(!sign);

        int i=Account.findLocation(inputID);
        excelWriter.writeCellValue(2, i, inputpassword);  //写入密码

        excelWriter.flush();
        excelWriter.close();
    }
}
