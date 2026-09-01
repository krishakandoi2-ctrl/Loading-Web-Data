package csci2020u.lab09;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StockDataLoader {

    /**
     * Downloads historical stock closing prices from GitHub CSV dataset.
     *
     * @param ticker  Lowercase ticker symbol, e.g. "aapl" or "goog"
     * @return        List of closing prices in chronological order (oldest first)
     */
    public static List<Float> downloadStockPrices(String ticker) {
        List<Float> prices = new ArrayList<>();

        String urlString = "https://raw.githubusercontent.com/OntarioTech-CS-program/"
                + "w26-lab09-Stock-Datasets/refs/heads/main/data/"
                + ticker.toLowerCase() + ".us.csv";

        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // CSV columns: Date, Open, High, Low, Close, Volume, OpenInt
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        float close = Float.parseFloat(parts[4].trim());
                        prices.add(close);
                    } catch (NumberFormatException e) {
                        // Skip malformed rows
                    }
                }
            }

            reader.close();
            System.out.println("Loaded " + prices.size() + " prices for " + ticker.toUpperCase());

        } catch (Exception e) {
            System.err.println("Error downloading data for " + ticker + ": " + e.getMessage());
        }

        return prices;
    }
}