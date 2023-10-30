package operate;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import domain.User;
import excel.ExcelRoute;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
public class AccountManagement {

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
    ExcelRoute excelRoute=new ExcelRoute();

    //根据ID查找位置的方法
    public static int findLocation(String inputID){
        ExcelRoute excelRoute=new ExcelRoute();
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());
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
    public static boolean checkPassword(String inputPassword) {
        String password1 = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";
        if (inputPassword.matches(password1)) {
            System.out.println("成功！");
            return true;
        }
        System.out.println("失败！输入密码不符合规则，请重新输入！");
        System.out.println("重新输入密码：");
        return false;
    }
    //注册账号
    public static void registerAccount(){
        ExcelRoute excelRoute=new ExcelRoute();
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());
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
            flag=checkPassword(inputpassword);   //检查密码输入是否合法
        }while(!flag);
        user.setPassword(inputpassword);


        addAccount(user,rowCount);
        System.out.println("注册成功！");
    }
    //添加新账号
    public static void addAccount(User user,int rowCount){
        ExcelRoute excelRoute=new ExcelRoute();
        ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getAccountExcelPath());

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
        ExcelRoute excelRoute=new ExcelRoute();
        ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getAccountExcelPath());
        System.out.println("输入的密码长度应大于 8 个字符，且必须是大小写字母、数字和标点符号的组合");
        System.out.println("输入修改后的密码：");


        boolean sign;
        String inputpassword;

        do{
            inputpassword=input.next();
            sign=checkPassword(inputpassword);   //检查密码输入是否合法
        }while(!sign);

        int i=findLocation(inputID);
        excelWriter.writeCellValue(2, i, inputpassword);  //写入密码

        excelWriter.flush();
        excelWriter.close();
    }
    //管理员重置密码
    public void resetUserPassword(){
        System.out.println("输入需要重置密码的用户账号：");
        String inputID=input.next();

        int i=findLocation(inputID);
        if (i!=-1) {
            System.out.println("账号存在！");
            changePassword(inputID);
        }

        else {
            System.out.println("用户账号不存在！返回操作界面。");
        }

    }

    //管理员列出所有用户信息
    public void showUserInformation(){
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());
        int n;
        System.out.println("用户ID"+"\t"+"\t"+"  用户名"+ "\t"+ "\t"+ "\t"+"用户级别"+ "\t"+"\t"+"用户消费总金额"+"\t"+"\t"+"用户累计消费次数");
        for(int i=7;i<excelReader.getRowCount();){
            List objects = excelReader.readRow(i);            //依次读取每一行的数据
            n=Integer.parseInt(objects.get(6).toString());
            System.out.println(objects.get(1).toString()+ "\t"+"\t"+objects.get(3).toString()+ "\t"+"\t"+objects.get(4).toString()+ "\t"+"\t"+"\t"+objects.get(5).toString()+ "\t"+"\t"+"\t"+"\t"+objects.get(6).toString());
            if(n==1||n==0){
                i++;
            }
            else{
                i+=n;
            }
        }
        excelReader.close();
    }
    //管理员删除用户信息
    public void removeUser(){
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());

        System.out.println("请输入要删除的用户ID");

        String inputID=input.next();

        int i=findLocation(inputID);
        if(i!=-1){
            List objects = excelReader.readRow(i);

            System.out.println("已找到ID为"+inputID+"的用户！用户信息如下：");
            System.out.println("用户名："+objects.get(3).toString());
            System.out.println("用户级别："+objects.get(4).toString());
            System.out.println("用户消费总金额："+objects.get(5).toString());
            System.out.println("用户累计消费次数："+objects.get(6).toString());
            System.out.println("是否删除该用户y/n");
            if(input.next().equals("y")){
                ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getAccountExcelPath());

                Workbook workbook = excelWriter.getWorkbook();

                int j;
                int n=Integer.parseInt(objects.get(6).toString());

                Sheet sheet = workbook.getSheetAt(0);

                if(n==0&&i+1==excelReader.getRowCount()){   //账号所在行为最后一行且n为0

                    Row row = sheet.getRow(i);

                    sheet.removeRow(row);

                }

                else if(n!=0&&i==excelReader.getRowCount()-n){      //账号所在行为最后一行且n不为0

                    for(j=i;j<i+n-1;j++){

                        Row row = sheet.getRow(i);

                        sheet.removeRow(row);

                        sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);

                    }

                    Row row = sheet.getRow(i);

                    sheet.removeRow(row);

                }
                else {
                    for (j=i;j<i+n;j++) {

                        Row row = sheet.getRow(i);

                        sheet.removeRow(row);

                        sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                    }
                }
                System.out.println("删除成功！");
                excelWriter.flush();
                excelWriter.close();
                excelReader.close();
                return;
            }
            else{

                System.out.println("取消删除！");
                excelReader.close();
                return;
            }
        }

        System.out.println("未找到ID为"+inputID+"的用户！返回操作界面");
        excelReader.close();
    }
    //管理员查找指定ID的用户的信息
    public void SearchUserInformation(){
        System.out.println("--------------请选择--------------------+");

        System.out.println("|\t\t1.查询指定用户的信息\t\t\t");

        System.out.println("|\t\t2.查询所有用户的信息\t\t\t");
        int sign;
        while (true) {
            try {
                sign=input.nextInt();
                if(sign==1||sign==2){
                    break;
                }
                System.out.println("输入非法，请输入整数1或2，请重新输入！");
            } catch (InputMismatchException e) {
                System.out.println("输入非法，请输入整数1或2，请重新输入！");
                input.nextLine(); // 清空输入缓冲区
            }
        }

        //查询指定用户信息
        if (sign==1){
            ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());
            System.out.println("请输入要查询的用户的ID：");
            String inputID=input.next();
            int i=findLocation(inputID);
            if (i!=-1) {
                List objects = excelReader.readRow(i);
                System.out.println("已找到ID为" + inputID + "的用户！用户信息如下：");
                System.out.println("用户名：" + objects.get(3).toString());
                System.out.println("用户级别：" + objects.get(4).toString());
                System.out.println("用户消费总金额：" + objects.get(5).toString());
                System.out.println("用户累计消费次数：" + objects.get(6).toString());
                excelReader.close();
                return;
            }
            System.out.println("未找到ID为"+inputID+"的用户！返回操作界面");
            excelReader.close();
        }
        else {
            showUserInformation();
        }
    }
}
