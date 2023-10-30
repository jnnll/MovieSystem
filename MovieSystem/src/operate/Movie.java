package operate;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import excel.ExcelRoute;

import java.util.List;

public class Movie {
    private String name;

    private String director;

    private String actor;
    private String type;
    private String introduction;
    private String duration;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override

    public String toString() {

        return "片名" + name + '\t' +
                "导演：" + director + '\t' +
                "主演：" + actor + '\t' +
                "类型：" + type + '\t' +
                "剧情简介：" + introduction + '\t' +
                "时长：" + duration ;
    }
    //输出所有电影信息列表
    public static void ShowMovie(){

        ExcelRoute excelRoute=new ExcelRoute();
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieExcelPath());

        List<List<Object>> rows = excelReader.read();

        for (List<Object> row : rows) {
            for (Object data : row) {
                System.out.print(data + "\t");
            }
            System.out.println("");
        }

        excelReader.close();
    }

    //输出指定的电影信息
    public static void ShowMovie(int rowCount){

        ExcelRoute excelRoute=new ExcelRoute();
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieExcelPath());

        List objects= excelReader.readRow(rowCount);

        System.out.println(objects.toString());
        excelReader.close();

    }

    //根据电影名字返回电影所在行数
    public static int searchMovies(String movieName){
        ExcelRoute excelRoute=new ExcelRoute();
        ExcelReader excelReader = ExcelUtil.getReader(excelRoute.getMovieExcelPath());
        List objects;
        String name;

        for (int i = 0; i < excelReader.getRowCount(); i++) {

            objects = excelReader.readRow(i);            //依次读取每一行的数据

            name = objects.get(0).toString();          //读取该行第一列的数据

            if (movieName.equals(name)) {
                excelReader.close();
                return i;
            }
        }
        excelReader.close();
        return -1;
    }
}
