package UI;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import operate.AccountManagement;
import domain.User;
import excel.ExcelRoute;
import operate.MovieArrangingManagement;
import operate.Ticketing;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserFunction implements Function {
    Scanner input = new Scanner(System.in);
    ExcelRoute excelRoute=new ExcelRoute();
    User user=new User();
    public UserFunction(){

    }
    public UserFunction(User user){
        this.user=user;
    }
    MovieArrangingManagement movieArrangingManagement=new MovieArrangingManagement();

    // 显示主菜单//
    public void showMenu(){
        String flag;
        int sign;
        do {

            menu.showUserOperation();

            while (true) {
                try {
                    sign=input.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("输入的数只能为整数,请重新输入！");
                    input.nextLine(); // 清空输入缓冲区
                }
            }

            if(sign==7){
                break;
            }

            start(sign);

            System.out.println("返回操作菜单：y/n？");

            flag = input.next();

        } while ("y".equals(flag));

        System.out.println("系统已退出！");

    }
    public void start(int sign){
        switch(sign){
            //修改自身密码
            case 1:{
                AccountManagement.changePassword(user.getID());
                break;
            }
            //查看所有电影放映信息
            case 2:{
                movieArrangingManagement.showInformation();
                break;
            }
            //查看指定电影放映信息
            case 3:{
                movieArrangingManagement.showSpecifiedInformation();
                break;
            }
            //购票
            case 4:{
                Ticketing.ticketing(user.getID());
                break;
            }
            //取票
            case 5:{
                printTicket();
                break;
            }
            //查看购票历史
            case 6:{
                purchaseHistory();
                break;
            }
            default:{
                System.out.println("输入的数字不在范围内！");
                break;
            }
        }
    }

    //查看购票历史
    public void purchaseHistory(){
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());
        int rowCount= AccountManagement.findLocation(user.getID());

        List objects=excelReader.readRow(rowCount);

        int n=Integer.parseInt(objects.get(6).toString());

        System.out.println("用户ID为"+user.getID()+"的用户消费次数为"+n+"次");

        System.out.println("历史观看电影\t观看电影日期\t观看电影的放映厅\t取票状态");

        for(int i=rowCount;i<rowCount+n;i++){

            objects=excelReader.readRow(i);

            System.out.println(objects.get(7).toString()+"\t"+"\t"+"\t"+objects.get(8).toString()+"\t"+"\t"+objects.get(9).toString()+"\t"+"\t"+objects.get(10).toString());
        }
        excelReader.close();

    }

    //取票
    public void printTicket(){
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());
        //代码有错误，为空无法转为整数判断
        List objects;
        int flag=0;
        while (true){
            System.out.println("请输入要取票的电子ID：");
            String ticketID=input.next();

            for(int i=1;i<excelReader.getRowCount();i++){
                objects=excelReader.readRow(i);
                if(objects.get(11).toString().equals(ticketID)){
                    //电影票ID存在
                    flag=1;
                    if(objects.get(10).toString().equals("未取票")){
                        System.out.println("查询到该电影票！取票成功！");
                        ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getAccountExcelPath());
                        excelWriter.writeCellValue(10,i,"已取票");
                        excelWriter.flush();
                        excelWriter.close();
                        excelReader.close();
                        return;
                    }
                    System.out.println("查询到该电影票！已被取出，无法重复取票！");
                    break;
                }
            }
            if (flag==0) {
                System.out.println("输入的电子ID不存在！");
            }
            System.out.println("是否重新输入：y/n");
            if(!input.next().equals("y")){
                excelReader.close();
                return;
            }
        }

    }
}
