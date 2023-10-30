package UI;

import operate.AccountManagement;
import operate.Movie;
import operate.MovieArrangingManagement;
import operate.Ticketing;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ReceptionistFunction implements Function{
    public ReceptionistFunction(){}
    Scanner input = new Scanner(System.in);
    MovieArrangingManagement movieArrangingManagement=new MovieArrangingManagement();
    // 显示主菜单//
    public void showMenu(){
        String flag;
        int sign;
        do {
            menu.showReceptionistOperation();
            while (true) {
                try {
                    sign=input.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("输入的数只能为整数,请重新输入！");
                    input.nextLine(); // 清空输入缓冲区
                }
            }

            if(sign==5){
                System.out.println("系统已成功退出！");
                break;
            }

            start(sign);

            System.out.println("返回操作菜单：y/n？");

            flag = input.next();

        } while ("y".equals(flag));
    }
    public void start(int sign){
        switch(sign){
            //列出所有正在上映影片的信息
            case 1:{
                Movie.ShowMovie();
                break;
            }
            //列出所有场次的信息
            case 2:{
                movieArrangingManagement.showInformation();
                break;
            }
            //列出指定电影和场次的信息
            case 3:{
                movieArrangingManagement.showSpecifiedInformation();
                break;
            }
            //售票
            case 4:{
                Ticketing();
                break;
            }
            default:{
                System.out.println("输入的数字不在范围内！");
                break;
            }
        }
    }
    //前台购票操作
    public void Ticketing(){
        System.out.println("请输入购票用户的账号ID：");
        String inputID;
        inputID=input.next();
        while(true){
            if(AccountManagement.findLocation(inputID)!=-1){
                break;
            }
            System.out.println("该用户不存在！是否输入用户ID：y/n？");
            if(!input.next().equals("y")){
                System.out.println("输入取消！");
                return;
            }
            System.out.println("请输入购票用户的账号ID：");
            inputID=input.next();
        }
        System.out.println("该用户存在！开始购票！");
        Ticketing.ticketing(inputID);
    }
}
