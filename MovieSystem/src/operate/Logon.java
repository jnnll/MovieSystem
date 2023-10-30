package operate;

import UI.*;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import domain.*;
import excel.ExcelRoute;

import java.util.List;
import java.util.Scanner;

import java.util.InputMismatchException;

public class Logon {
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

    static Scanner input = new Scanner(System.in);


    //开始界面
    public static void start(){

        menu.logon();
        try{
            switch (input.nextInt()){
                case 1:{
                    log();
                    break;
                }

                case 2:{
                    AccountManagement.registerAccount();
                    break;
                }
                case 3: {
                    forgetPassword();
                    break;
                }
                default:{
                    System.out.println("输入的数字不在范围内！程序结束");
                    break;
                }
            }
        }
        catch (InputMismatchException e){
            System.out.println("输入的数字不是整数！程序结束");
        }

    }
    //登陆系统
    public static void log(){
        String flag;
        AccountManagement accountManagement =new AccountManagement();
        Logon logg=new Logon();
        do {
            System.out.println("--------------登录系统--------------------+");
            System.out.println("请输入账号：");
            accountManagement.setID(input.next());
            System.out.println("请输入密码：");
            accountManagement.setPassword(input.next());
            if(check(accountManagement))
                break;
            System.out.println("是否重新输入账号和密码：y/n？");
            flag=input.next();
        } while ("y".equals(flag));
    }

//检查账号与密码是否匹配
    public static boolean check(AccountManagement accountManagement){

        ExcelRoute excelRoute=new ExcelRoute();
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());
        String flag="n";
        for (int i = 1; i < excelReader.getRowCount(); i++) {

            List objects = excelReader.readRow(i);            //依次读取每一行的数据
            String ID = objects.get(1).toString();          //读取该行第二列的数据

            if (ID.equals(accountManagement.getID())) {              //账号正确
                String password = objects.get(2).toString();
                if (password.equals(accountManagement.getPassword())) {    //密码正确
                    System.out.println("密码正确，登录成功！");
                    excelReader.close();
                    matchingSystem(objects.get(0).toString(),ID);  //登陆到对应的系统
                    return true;
                }
                System.out.println("密码错误，登录失败！");    //密码错误
                excelReader.close();
                return false;
                }
            }
        System.out.println("账号不存在！");
        excelReader.close();
        return false;
    }
    //将账号密码匹配到对应的系统
    public static void matchingSystem(String identity,String ID){
        if(identity.equals("管理员")){
            Administrator administrator=new Administrator(ID);
            AdministratorFunction administratorFunction=new AdministratorFunction(administrator);
            administratorFunction.showMenu();

        }
        else if(identity.equals("经理")){
            ManagerFunction managerFunction=new ManagerFunction();
            managerFunction.showMenu();
        }
        else if (identity.equals("前台")) {
            ReceptionistFunction receptionistFunction=new ReceptionistFunction();
            receptionistFunction.showMenu();
        }
        else{
            User user=new User(ID);
            UserFunction userFunction=new UserFunction(user);
            userFunction.showMenu();
        }
    }
    //忘记密码重置
    public static void forgetPassword(){

        int i;
        String flag="n";
        String inputID;
        while(true){
            System.out.println("清输入要重置密码的账号ID：");
            inputID=input.next();
            i= AccountManagement.findLocation(inputID);

            if(i==-1){
                System.out.println("该账号不存在！");
                System.out.println("是否重新输入y/n");
                flag=input.next();
                if(flag.equals("n")){
                    return;
                }
            }
            else break;
        }
        System.out.println("用户ID存在！开始重置密码！");
        AccountManagement.changePassword(inputID);
        System.out.println("重置密码成功！系统退出");
    }
}
