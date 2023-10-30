package domain;

public class User extends Person {
    private String name;            //用户名
    private String level;         //消费水平
    private double consumptionAmount;      //消费总金额
    private int consumptionFrequency;    //消费总次数
    private String purchasedMovie;      //购买的电影
    private String screeningRoom;      //电影所在放映厅
    private String showDate;         //电影放映日期

    public User() {

    }

    public User(String ID) {
        setID(ID);
    }

    public User(String ID, String password, String name) {
        setID(ID);
        setPassword(password);
        this.name = name;
    }

    //构造函数

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getConsumptionAmount() {
        return consumptionAmount;
    }

    public void setConsumptionAmount(double consumptionAmount) {
        this.consumptionAmount = consumptionAmount;
    }

    public int getConsumptionFrequency() {
        return consumptionFrequency;
    }

    public void setConsumptionFrequency(int consumptionFrequency) {
        this.consumptionFrequency = consumptionFrequency;
    }

    public String getPurchasedMovie() {
        return purchasedMovie;
    }

    public void setPurchasedMovie(String purchasedMovie) {
        this.purchasedMovie = purchasedMovie;
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


}