package operate;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import excel.ExcelRoute;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Scanner;

public class MovieManagement {
    Scanner input = new Scanner(System.in);
    ExcelRoute excelRoute=new ExcelRoute();

    //信息写入Excel表格,接受要加入的电影以及插入表格的行
    public void excelInsert(Movie movie, int rowCount){

        ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getMovieExcelPath());

        excelWriter.writeCellValue(0, rowCount , movie.getName());
        excelWriter.writeCellValue(1, rowCount , movie.getDirector());
        excelWriter.writeCellValue(2, rowCount , movie.getActor());
        excelWriter.writeCellValue(3, rowCount , movie.getType());
        excelWriter.writeCellValue(4, rowCount , movie.getIntroduction());
        excelWriter.writeCellValue(5, rowCount , movie.getDuration());

        excelWriter.flush();

        excelWriter.close();

    }

    //添加电影
    public void addMovie(){
        Movie movie = inputInfo();
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieExcelPath());
        int rowCount=excelReader.getRowCount();
        excelInsert(movie,rowCount);
        excelReader.close();
    }


    //输入电影信息
    public Movie inputInfo(){

        Movie movie = new Movie();
        System.out.print("请输入片名：");
        movie.setName(input.next());

        System.out.print("请输入导演：");
        movie.setDirector(input.next());

        System.out.print("请输入主演：");
        movie.setActor(input.next());

        System.out.print("请输入类型：");
        movie.setType(input.next());

        System.out.print("请输入剧情简介：");
        movie.setIntroduction(input.next());

        System.out.print("请输入时长：");
        movie.setDuration(input.next());

        return movie;

    }


    //更改电影数据
    public void updateMovie(String movieName){

        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieExcelPath());

        for (int i = 0; i < excelReader.getRowCount(); i++) {

            List objects = excelReader.readRow(i);            //依次读取每一行的数据

            String name = objects.get(0).toString();          //读取该行第一列的数据

            if (movieName.equals(name)) {
                System.out.println("查找成功！该电影存在，开始修改。");
                excelInsert(inputInfo(),i);

                System.out.println("更改成功！");
                excelReader.close();
                return;
            }
        }
        System.out.println("更改失败！不存在该电影！");
        excelReader.close();
    }


    //删除电影信息
    public void removeMovie(String name){

        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieExcelPath());
        List objects;

        for (int i = 0; i < excelReader.getRowCount(); i++) {

            objects = excelReader.readRow(i);

            if (objects.get(0).equals(name)) {
                System.out.println("已找到名为"+name+"的电影！");
                System.out.println("是否删除该电影y/n");
                if(input.next().equals("y")){
                    ExcelWriter excelWriter = ExcelUtil.getWriter(excelRoute.getMovieExcelPath());
                    Workbook workbook = excelWriter.getWorkbook();

                    Sheet sheet = workbook.getSheetAt(0);

                    Row row = sheet.getRow(i);

                    sheet.removeRow(row);
                    if(i!=excelReader.getRowCount()-1){
                        sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                    }
                    System.out.println("删除电影成功！");

                    excelWriter.flush();
                    excelWriter.close();
                    excelReader.close();
                    return;
                }
                else {
                    System.out.println("取消删除！");
                    excelReader.close();
                    return;
                }
            }

        }
        System.out.println("未找到名为"+name+"的电影！");
        excelReader.close();
    }
}
