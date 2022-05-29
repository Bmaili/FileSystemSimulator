import controller.MainWindow;
import service.SuperBlockSercive;

public class ApplicationStart {
    public static void main(String[] args) {
        SuperBlockSercive.boot(); //数据初始引导
        MainWindow.boot(); //UI初始引导
    }
}
