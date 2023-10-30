package operate;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import domain.User;
import excel.ExcelRoute;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Ticketing {
    //为指定ID的用户进行购票
    public static int ticketing(String inputID){
        Scanner input=new Scanner(System.in);
        ExcelRoute excelRoute=new ExcelRoute();
        int rowCount= AccountManagement.findLocation(inputID);
        int sign;

        MovieArrangingManagement movieArrangingManagement=new MovieArrangingManagement();

        System.out.println("以下是所有排片信息：");
        movieArrangingManagement.showInformation();

        while (true){
            System.out.println("请输入要购票的电影名：");
            movieArrangingManagement.setName(input.next());

            System.out.println("请输入要购票的日期：");
            movieArrangingManagement.setShowDate(input.next());

            System.out.println("请输入要购买的放映厅：");
            movieArrangingManagement.setScreeningRoom(input.next());

            sign=movieArrangingManagement.searchInformation();

            if(sign==-1){
                System.out.println("查询失败！" + movieArrangingManagement.getShowDate() + movieArrangingManagement.getScreeningRoom() + "已有其他电影的排片！请重新输入！");

            }
            else if(sign==-2) {
                System.out.println("查询失败！"+movieArrangingManagement.getShowDate() + "没有在" + movieArrangingManagement.getScreeningRoom() + "的排片！请重新输入！");
            }
            else if (sign==-3){
                System.out.println("查询失败！"+movieArrangingManagement.getShowDate()+"没有排片！请重新输入！");
            }
            else {
                System.out.println("查找成功！" + movieArrangingManagement.getShowDate() + movieArrangingManagement.getScreeningRoom() + "有" + movieArrangingManagement.getName() + "的排片！");
                break;
            }
        }

        //展示选座界面
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieArrangingExcelPath());
        System.out.println("以下是该场次的座位图：");
        System.out.println("O表示座位空余，X表示座位已被选择。");

        List objects;

        for (int i = sign; i <= sign + 7; i++) {
            objects = excelReader.readRow(i);

            for (int k = 5; k <= 17; k++) {
                System.out.print(objects.get(k).toString() + "\t");
            }

            System.out.println("");
        }

        objects=excelReader.readRow(sign);
        //获取该场次的电影价钱和放映时间
        movieArrangingManagement.setPrice(Double.parseDouble(objects.get(4).toString()));
        movieArrangingManagement.setShowTime(objects.get(3).toString());

        //选座操作
        int row;
        int column;
        while (true){
            System.out.println("请输入要选择的座位的行数：");
            row=input.nextInt();

            System.out.println("请输入要选择的座位的列数：");
            column=input.nextInt();

            //座位存在
            if(row>=1&&row<=7&&column>=1&column<=12){
                //判断座位是否空余
                objects = excelReader.readRow(sign+row);
                if(objects.get(column+5).toString().equals("X")){
                    System.out.println("输入错误！该座位已被选择！");
                }
                else {
                    System.out.println("该座位空余！可以选座！");
                    System.out.println("选择的片名:"+movieArrangingManagement.getName());
                    System.out.println("选择的放映厅："+movieArrangingManagement.getScreeningRoom());
                    System.out.println("选择的座位："+row+"行"+column+"列");
                    System.out.println("选择日期："+movieArrangingManagement.getShowDate());
                    System.out.println("选择的时间："+movieArrangingManagement.getShowTime());
                    System.out.println("票价："+movieArrangingManagement.getPrice());
                    System.out.println("是否确定选座：y/n");
                    if(!input.next().equals("y")){
                        System.out.println("选座取消！购票失败！");
                        excelReader.close();
                        return -1;
                    }
                    excelReader.close();
                    break;
                }

            }
            else {
                System.out.println("输入错误！座位不存在！");
            }
            System.out.println("是否重新选座：y/n");
            if(!input.next().equals("y")){
                System.out.println("选座取消！购票失败！");
                excelReader.close();
                return -1;
            }
        }

        //购买界面
        ExcelReader userExcelReader = ExcelUtil.getReader(excelRoute.getAccountExcelPath());
        User user=new User(inputID);

        //获取该用户的消费等级、消费金额和消费次数
        List userObjects = userExcelReader.readRow(rowCount);
        user.setLevel(userObjects.get(4).toString());
        user.setConsumptionAmount(Double.parseDouble(userObjects.get(5).toString()));
        user.setConsumptionFrequency(Integer.parseInt(userObjects.get(6).toString()));
        double price;

        if(user.getLevel().equals("金牌用户")){
            price=movieArrangingManagement.getPrice()*0.88;
            System.out.println("检测到用户是金牌用户，购票享受88折优惠！原价"+movieArrangingManagement.getPrice()+"只需支付"+price+"元");
            System.out.println("是否确定支付：y/n");
            if(!input.next().equals("y")){
                System.out.println("支付取消！购票失败！");
                return -1;
            }
            System.out.println("确定支付！购票成功！");
            user.setConsumptionAmount(user.getConsumptionAmount()+price);   ////用户累计消费金额增加
        }
        else if(user.getLevel().equals("银牌用户")){
            price=movieArrangingManagement.getPrice()*0.95;
            System.out.println("检测到用户是银牌用户，购票享受95折优惠！原价"+movieArrangingManagement.getPrice()+"只需支付"+price+"元");
            System.out.println("是否确定支付：y/n");
            if(!input.next().equals("y")){
                System.out.println("支付取消！购票失败！");
                return -1;
            }
            System.out.println("确定支付！购票成功！");
            user.setConsumptionAmount(user.getConsumptionAmount()+price);   //用户累计消费金额增加
        }
        else {
            System.out.println("需支付"+movieArrangingManagement.getPrice()+"元");
            System.out.println("是否确定支付：y/n");
            if(!input.next().equals("y")){
                System.out.println("支付取消！购票失败！");
                return -1;
            }
            System.out.println("确定支付！购票成功！");
            user.setConsumptionAmount(user.getConsumptionAmount()+movieArrangingManagement.getPrice());    //用户累计消费金额增加
        }

        userExcelReader.close();

        //随机生成取票的电影票的电子 ID 编号
        Random random = new Random();
        int ticketID = random.nextInt(89999) +10000; // 生成[-3,15)区间的整数[10000,99999)
        System.out.println("以下是购买的电影票信息：");
        System.out.println("片名:"+movieArrangingManagement.getName());
        System.out.println("放映厅："+movieArrangingManagement.getScreeningRoom());
        System.out.println("座位："+row+"行"+column+"列");
        System.out.println("日期："+movieArrangingManagement.getShowDate());
        System.out.println("时间："+movieArrangingManagement.getShowTime());
        System.out.println("票价："+movieArrangingManagement.getPrice());
        System.out.println("电子 ID 编号:"+ticketID);

//对存储数据进行处理
        //更新排片座位图
        ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getMovieArrangingExcelPath());
        ExcelWriter userExcelWriter = ExcelUtil.getWriter(excelRoute.getAccountExcelPath());

        excelWriter.writeCellValue(column+5,sign+row,"X");     //写入座位已被选择
        excelWriter.flush();
        excelWriter.close();

        //更新用户的消费次数、看过的电影名、放映厅和日期
        user.setPurchasedMovie(movieArrangingManagement.getName());
        user.setScreeningRoom(movieArrangingManagement.getScreeningRoom());
        user.setShowDate(movieArrangingManagement.getShowDate());

        //向用户表格中新增一行填入历史记录
        Workbook workbook = userExcelWriter.getWorkbook();
        Sheet sheet = workbook.getSheetAt(0);

        if(user.getConsumptionFrequency()!=0&&rowCount+user.getConsumptionFrequency()!=userExcelWriter.getRowCount()-1){
            sheet.shiftRows(rowCount+user.getConsumptionFrequency() , sheet.getLastRowNum(), 1);
            userExcelWriter.writeCellValue(1,rowCount+user.getConsumptionFrequency(),"NULL");
        }

        userExcelWriter.writeCellValue(7,rowCount+user.getConsumptionFrequency(),user.getPurchasedMovie());
        userExcelWriter.writeCellValue(8,rowCount+user.getConsumptionFrequency(),user.getShowDate());
        userExcelWriter.writeCellValue(9,rowCount+user.getConsumptionFrequency(),user.getScreeningRoom());
        userExcelWriter.writeCellValue(10,rowCount+user.getConsumptionFrequency(),"未取票");
        userExcelWriter.writeCellValue(11,rowCount+user.getConsumptionFrequency(),ticketID);


        //向用户表格中填入累计消费金额和消费次数，并检查消费等级
        user.setConsumptionFrequency(user.getConsumptionFrequency()+1);
        userExcelWriter.writeCellValue(5,rowCount,user.getConsumptionAmount());
        userExcelWriter.writeCellValue(6,rowCount,user.getConsumptionFrequency());
        if(user.getConsumptionAmount()>=100){
            userExcelWriter.writeCellValue(4,rowCount,"银牌用户");
        }
        if(user.getConsumptionAmount()>=500){
            userExcelWriter.writeCellValue(4,rowCount,"金牌用户");
        }
        userExcelWriter.flush();
        userExcelWriter.close();

        return ticketID;
    }
}
