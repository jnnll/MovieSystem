package operate;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import excel.ExcelRoute;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MovieArrangingManagement {
    Scanner input = new Scanner(System.in);
    ExcelRoute excelRoute=new ExcelRoute();

    private String name;
    private String screeningRoom;
    private String showDate;
    private String showTime;
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreeningRoom() {
        return screeningRoom;
    }

    public void setScreeningRoom(String screeningRoom) {
        this.screeningRoom = screeningRoom;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //列出所有影片放映信息
    public void showInformation() {
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieArrangingExcelPath());
        for (int i = 1; i <excelReader.getRowCount(); i += 8) {
            List objects = excelReader.readRow(i);            //依次读取每一行的数据
            System.out.println(objects.get(0).toString() + "\t" + "\t" + objects.get(1).toString() + "\t" + "\t" + objects.get(2).toString()+"\t"+"\t"+objects.get(3)+"\t"+"\t"+objects.get(4));

        }
        excelReader.close();
    }

    //列出指定电影和场次的信息
    public void showSpecifiedInformation() {
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieArrangingExcelPath());
        Scanner input = new Scanner(System.in);

        int j=0;
        List objects;

        System.out.println("请输入要查看排片的电影名：");
        setName(input.next());

        System.out.println("请输入该电影放映的日期：");
        setShowDate(input.next());

        System.out.println("请输入该电影所在的放映厅：");
        setScreeningRoom(input.next());

        j=searchInformation();

        if(j==-1){
            System.out.println("查询失败！" + getShowDate() + getScreeningRoom() + "已有其他电影的排片！");

        }
        else if(j==-2) {
            System.out.println("查询失败！"+getShowDate() + "没有在" + getScreeningRoom() + "的排片！");
        }
        else if (j==-3){
            System.out.println("查询失败！"+getShowDate()+"没有排片！");
        }
        else{
            System.out.println("查找成功！" + getShowDate() + getScreeningRoom() + "有" + getName() + "的排片！");
            //展示座位图
            for (int i = j; i <= j + 7; i++) {
                objects = excelReader.readRow(i);

                for (int k = 5; k <= 17; k++) {
                    System.out.print(objects.get(k).toString() + "\t");
                }

                System.out.println("");
            }
        }
        excelReader.close();
    }

    //搜索排片信息
    public int searchInformation() {
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieArrangingExcelPath());

        int j = 0;
        boolean findRoom=false;
        boolean findDate=false;
        List objects;
        for (int i = 1; i < excelReader.getRowCount(); i+=8) {
            objects = excelReader.readRow(i);
            //先搜索放映时间
            if (objects.get(2).toString().equals(getShowDate())) {
                findDate=true;
                //放映时间找到后再搜索放映厅
                for (j = i; (j < i + 17)&&j<excelReader.getRowCount(); j+=8) {
                    objects = excelReader.readRow(j);
                    if (objects.get(1).toString().equals(getScreeningRoom())&&objects.get(2).toString().equals(getShowDate())) {
                        findRoom=true;
                        //放映厅找到后再检查电影名是否匹配，因为每天每个放映厅只有一部电影！
                        if (objects.get(0).toString().equals(getName())) {
                            excelReader.close();
                            return j;
                        }
                        else {
                            //在Date日期放映厅Room已经有了其他电影的排片
                            excelReader.close();
                            return -1;
                        }
                    }
                }
            }
            //若在Date日期没有放映厅Room的排片，返回-2
            if(!findRoom&&findDate){
                excelReader.close();
                return -2;
            }
        }
        excelReader.close();
        return -3;
    }


    //增加排片
    public void addArrangement(){

        int sign;

        System.out.println("请输入要增加排片的日期：");
        setShowDate(input.next());

        while (true){
            System.out.println("请输入要增加排片的放映厅：");
           setScreeningRoom(input.next());

            if(getScreeningRoom().equals("1号厅")||getScreeningRoom().equals("2号厅")||getScreeningRoom().equals("3号厅")){
                break;
            }
            System.out.println("输入的放映厅不存在！请重新输入（1/2/3号厅）");
        }

        sign=searchInformation();

        if(sign==-2){
            System.out.println(getShowDate()+"没有在"+getScreeningRoom()+"的排片！可以增加排片！");
        }
        else if (sign==-3){
            System.out.println(getShowDate()+"没有排片！可以增加排片！");
        }
        else{
            System.out.println(getShowDate()+"在"+getScreeningRoom()+"已排片！无法增加排片！");
            return;
        }

        boolean flag=false;
        while(!flag){
            System.out.println("请输入要增加排片的片名：");
            setName(input.next());
            if(Movie.searchMovies(getName())!=-1){
                flag=true;
            }
            else{
                System.out.println("输入的片名不在电影列表中！请重新输入！");
            }
        }

        System.out.println("请输入要增加排片的时间：");
        setShowTime(input.next());

        System.out.println("请输入该场次的电影票价：");
        setPrice(input.nextInt());

        ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getMovieArrangingExcelPath());
        int rowCount=excelWriter.getRowCount();

        excelWriter.writeCellValue(0, rowCount , getName());
        excelWriter.writeCellValue(1, rowCount , getScreeningRoom());
        excelWriter.writeCellValue(2, rowCount , getShowDate());
        excelWriter.writeCellValue(3, rowCount , getShowTime());
        excelWriter.writeCellValue(4, rowCount , getPrice());

        //增加行头
        int k=1;
        for(int i=6;i<=17;i++){
            excelWriter.writeCellValue(i, rowCount , k++);
        }

        //增加列头
        k=1;
        for(int i=rowCount+1;i<=rowCount+7;i++){
            excelWriter.writeCellValue(5, i , k++);
        }
        for(int i=rowCount+1;i<=rowCount+7;i++){
            for(int j=6;j<=17;j++){
                excelWriter.writeCellValue(j, i ,"O");
            }
        }

        System.out.println("新增排片成功！");
        excelWriter.flush();
        excelWriter.close();
    }

    //修改排片
    public void updateArrangement(){
        int sign;
        while (true){
            System.out.println("请输入要修改排片的日期：");
            setShowDate(input.next());

            System.out.println("请输入要修改排片的放映厅：");
            setScreeningRoom(input.next());

            if(!getScreeningRoom().equals("1号厅")&&!getScreeningRoom().equals("2号厅")&&!getScreeningRoom().equals("3号厅")){
                System.out.println("输入的放映厅不存在！请重新输入（1/2/3号厅）");
                continue;
            }

            setName("NULL");
            sign=searchInformation();

            if(sign==-2){
                System.out.println(getShowDate()+"没有在"+getScreeningRoom()+"的排片！无法修改！");
            }
            else if (sign==-3){
                System.out.println(getShowDate()+"没有排片！无法修改！");
            }
            else{
                System.out.println(getShowDate()+"在"+getScreeningRoom()+"已排片！可以修改！");
                break;
            }
            System.out.println("是否重新输入：y/n");
            if(!input.next().equals("y")){
                return;
            }
        }

        boolean flag=false;
        while(!flag){
            System.out.println("请输入修改后"+getShowDate()+getScreeningRoom()+"要放映的片名：");
            setName(input.next());
            if(Movie.searchMovies(getName())!=-1){
                flag=true;
            }
            else{
                System.out.println("输入的片名不在电影列表中！请重新输入！");
            }
        }

        System.out.println("请输入该电影排片的时间：");
       setShowTime(input.next());

        System.out.println("请输入该场次的电影票价：");
        setPrice(input.nextInt());

        if(sign==-1){

            ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieArrangingExcelPath());

            int j = 0;

            List objects;
            for (int i = 1; i < excelReader.getRowCount(); i+=8) {
                objects = excelReader.readRow(i);
                //先搜索放映时间
                if (objects.get(2).toString().equals(getShowDate())) {
                    //放映时间找到后再搜索放映厅
                    for (j = i; j < i + 17; j+=8) {
                        objects = excelReader.readRow(j);
                        if (objects.get(1).toString().equals(getScreeningRoom())&&objects.get(2).toString().equals(getShowDate())) {
                            excelReader.close();
                            break;
                        }
                    }
                    break;
                }
            }
            sign=j;
        }


        ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getMovieArrangingExcelPath());

        excelWriter.writeCellValue(0, sign , getName());
        excelWriter.writeCellValue(3, sign , getShowTime());
        excelWriter.writeCellValue(4, sign , getPrice());

        System.out.println("修改排片信息成功！");
        excelWriter.flush();
        excelWriter.close();

    }
    //删除排片
    public void removeArrangement(){

        int sign;
        while (true){

            System.out.println("请输入要删除排片的日期：");
            setShowDate(input.next());

            System.out.println("请输入要删除该日排片的放映厅：");
            setScreeningRoom(input.next());

            sign=searchInformation();

            if(sign==-2){
                System.out.println(getShowDate()+"没有在"+getScreeningRoom()+"的排片！无法删除！");
            }
            else if (sign==-3){
                System.out.println(getShowDate()+"没有排片！无法删除！");
            }
            else{
                System.out.println(getShowDate()+"在"+getScreeningRoom()+"已排片！可以删除！");
                break;
            }
            System.out.println("是否重新输入：y/n");
            if(!input.next().equals("y")){
                return;
            }
        }
        System.out.println("请确定是否删除该场排片：y/n");
        if(!input.next().equals("y")){
            System.out.println("取消删除！");
            return;
        }

        if(sign==-1){

            ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieArrangingExcelPath());

            int j = 0;

            List objects;
            for (int i = 1; i < excelReader.getRowCount(); i++) {
                objects = excelReader.readRow(i);
                //先搜索放映时间
                if (objects.get(2).toString().equals(getShowDate())) {
                    //放映时间找到后再搜索放映厅
                    for (j = i; j < i + 17; j++) {
                        objects = excelReader.readRow(j);
                        if (objects.get(1).toString().equals(getScreeningRoom())&&objects.get(2).toString().equals(getShowDate())) {
                            excelReader.close();
                            break;
                        }
                    }
                    break;
                }
            }
            sign=j;
        }

        ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getMovieArrangingExcelPath());
        Workbook workbook = excelWriter.getWorkbook();
        int i;

        Sheet sheet = workbook.getSheetAt(0);

        if(sign==excelWriter.getRowCount()-8){

            for(i=sign;i<sign+7;i++){

                Row row = sheet.getRow(sign);

                sheet.removeRow(row);

                sheet.shiftRows(sign + 1, sheet.getLastRowNum(), -1);

            }

            Row row = sheet.getRow(sign);

            sheet.removeRow(row);

        }
        else {
            for (i = sign; i <= sign + 7; i++) {

                Row row = sheet.getRow(sign);

                sheet.removeRow(row);

                sheet.shiftRows(sign + 1, sheet.getLastRowNum(), -1);
            }
        }

        System.out.println("删除电影成功！");

        try {

            FileOutputStream outputStream = new FileOutputStream(excelRoute.getMovieArrangingExcelPath());
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {

            e.printStackTrace();

        }

        excelWriter.flush();
        excelWriter.close();
    }
}
