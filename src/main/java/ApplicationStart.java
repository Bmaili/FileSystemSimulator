import controller.MainWindow;
import service.SuperBlockSercive;

/**
 * 项目启动类
 */
public class ApplicationStart {
    public static void main(String[] args) {
        SuperBlockSercive.boot(); //数据初始引导
        MainWindow.boot(); //界面初始引导
    }
}
