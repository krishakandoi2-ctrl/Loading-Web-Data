package csci2020u.lab09;

import javax.swing.*;
import java.util.List;

public class StockApp extends JFrame {

    public StockApp() {
        setTitle("Stock Performance");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        List<Float> aaplPrices = StockDataLoader.downloadStockPrices("aapl");
        List<Float> googPrices = StockDataLoader.downloadStockPrices("goog");

        StockPanel panel = new StockPanel(aaplPrices, googPrices);
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StockApp::new);
    }
}