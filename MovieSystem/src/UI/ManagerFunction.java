package UI;

import domain.Manager;
import operate.Movie;
import operate.MovieArrangingManagement;
import operate.MovieManagement;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ManagerFunction implements Function{

    Manager manager=new Manager();
    public ManagerFunction(){}
    public ManagerFunction(Manager manager){this.manager=manager;}
    MovieManagement movieManagement=new MovieManagement();

    MovieArrangingManagement movieArrangingManagement=new MovieArrangingManagement();
    Scanner input = new Scanner(System.in);

    //显示主菜单//

    public void showMenu(){
        String flag;
        int sign;

        do {

            menu.showManagerOperation();
            while (true) {
                try {
                    sign=input.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("输入的数只能为整数,请重新输入！");
                    input.nextLine(); // 清空输入缓冲区
                }
            }

            if(sign==10){
                System.out.println("系统已成功退出！");
                break;
            }

            start(sign);

            System.out.println("返回操作菜单：y/n？");

            flag = input.next();

        } while ("y".equals(flag));
    }

    public void start(int sign){

        switch (sign){

//输出电影信息
            case 1: {
                Movie.ShowMovie();
                break;
            }

//增加电影
            case 2:{

                String flag;

                do {

                    movieManagement.addMovie();

                    System.out.println("是否继续增加：y/n？");
                    flag = input.next();

                }while ("y".equals(flag));

                break;
            }

            case 3:{
//更改电影信息
                String flag;

                do{

                    System.out.println("请输入要更改的片名：");

                    movieManagement.updateMovie(input.next());

                    System.out.println("是否继续更改：y/n？");

                    flag = input.next();

                }while ("y".equals(flag));

                break;
            }

//删除电影
            case 4:{

                String flag;

                do {
                    System.out.println("请输入要删除的片名：");

                    movieManagement.removeMovie(input.next());

                    System.out.println("是否继续删除：y/n？");

                    flag = input.next();

                }while ("y".equals(flag));

                break;
            }

//查询电影信息
            case 5: {

                String flag;

                int i = 0;

                do {
                    System.out.println("请输入要查询的片名：");

                    i = Movie.searchMovies(input.next());

                    if (i != -1) {
                        Movie.ShowMovie(i);
                    } else {
                        System.out.println("该电影不存在！");
                    }
                    System.out.println("是否继续查询：y/n？");

                    flag = input.next();

                } while ("y".equals(flag));

                break;
            }

//增加电影场次
            case 6:{
                movieArrangingManagement.addArrangement();
                break;
            }

//修改电影场次
            case 7:{
                movieArrangingManagement.updateArrangement();
                break;
            }

//删除电影场次
            case 8:{
                movieArrangingManagement.removeArrangement();
                break;
            }

//列出所有场次信息
            case 9:{
                movieArrangingManagement.showInformation();
                break;
            }

            default:{
                System.out.println("输入的数字不在范围内！");
                break;

            }

        }

    }
}
