package excel;

public class ExcelRoute {
    private String projectPath = this.getClass().getClassLoader().getResource("").getPath();
    private String movieRelativePath = "excel/电影名单.xlsx";
    private String movieArrangingRelativePath="excel/排片信息.xlsx";
    private String accountRelativePath="excel/账号信息.xlsx";
    private String movieExcelPath = projectPath + movieRelativePath;
    private String movieArrangingExcelPath=projectPath+movieArrangingRelativePath;
    private String accountExcelPath=projectPath+accountRelativePath;

    public String getMovieExcelPath() {
        return movieExcelPath;
    }

    public String getMovieArrangingExcelPath() {
        return movieArrangingExcelPath;
    }

    public String getAccountExcelPath() {
        return accountExcelPath;
    }
}
