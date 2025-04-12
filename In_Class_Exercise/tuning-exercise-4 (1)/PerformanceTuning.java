
public class PerformanceTuning {

    public static void main(String[] args) {
        MysteryClass mystery = new MysteryClass();
        mystery.initialSetup();
        for (int i = 0; i < 100; i++) {
            mystery.doOneTask();
        }
    }
}
