package UI;

import operate.AccountManagement;
import domain.Administrator;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AdministratorFunction implements Function {
    Administrator administrator=new Administrator();
    public AdministratorFunction(){}

    public AdministratorFunction(Administrator administrator){
        this.administrator=administrator;
    }

    Scanner input = new Scanner(System.in);
    AccountManagement account=new AccountManagement();

    //显示主菜单

    public void showMenu(){
        String flag;
        int sign;
        do {

            menu.showAdminOperation();

            while (true) {
                try {
                    sign=input.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("输入的数只能为整数,请重新输入！");
                    input.nextLine(); // 清空输入缓冲区
                }
            }

            if(sign==6){
                System.out.println("系统已成功退出！");
                break;
            }

            start(sign);

            System.out.println("返回操作菜单：y/n？");

            flag = input.next();

        } while ("y".equals(flag));
    }
    //开始选择操作
    public void start(int sign){
        switch (sign) {
            //修改自身密码
            case 1: {
                AccountManagement.changePassword(administrator.getID());
                break;
            }
            //重置用户密码
            case 2:{
                account.resetUserPassword();
                break;
            }
            //列出所有用户信息
            case 3:{
                account.showUserInformation();
                break;
            }
            //删除用户信息
            case 4:{
                account.removeUser();
                break;
            }
            //查询用户信息
            case 5:{
                account.SearchUserInformation();
                break;
            }
            default:{
                System.out.println("输入的数字不在范围内！");
                break;
            }
        }
    }


}
