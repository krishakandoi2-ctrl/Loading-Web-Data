package csci2020u.lab09;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StockPanel extends JPanel {

    private static final int PAD = 50;          // 50-pixel padding on left and bottom
    private static final Color COLOR_AAPL = Color.BLUE;
    private static final Color COLOR_GOOG = Color.RED;

    private final List<Float> aaplPrices;
    private final List<Float> googPrices;

    public StockPanel(List<Float> aaplPrices, List<Float> googPrices) {
        this.aaplPrices = aaplPrices;
        this.googPrices = googPrices;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawAxes(g);
        plotLines(g, aaplPrices, googPrices);
        drawLegend(g,
                new String[]{"AAPL", "GOOG"},
                new Color[]{COLOR_AAPL, COLOR_GOOG});
    }

    // -----------------------------------------------------------------------
    // Draw x-axis and y-axis 50 pixels from the left and bottom edge
    // -----------------------------------------------------------------------
    private void drawAxes(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        g.setColor(Color.BLACK);

        // Y-axis: 50px from left, from top to bottom-padding
        g.drawLine(PAD, 0, PAD, h - PAD);

        // X-axis: from left-padding to right edge, 50px from bottom
        g.drawLine(PAD, h - PAD, w, h - PAD);
    }

    // -----------------------------------------------------------------------
    // plotLines – takes two price lists, finds global max, calls plotLine twice
    // -----------------------------------------------------------------------
    private void plotLines(Graphics g, List<Float> prices1, List<Float> prices2) {
        if (prices1.isEmpty() && prices2.isEmpty()) return;

        // Find the global maximum price across both series for Y scaling
        float maxPrice = 0f;
        for (float p : prices1) if (p > maxPrice) maxPrice = p;
        for (float p : prices2) if (p > maxPrice) maxPrice = p;

        // The longer list determines the total number of trading days on the X axis
        int totalDays = Math.max(prices1.size(), prices2.size());

        plotLine(g, prices1, totalDays, maxPrice, COLOR_AAPL);
        plotLine(g, prices2, totalDays, maxPrice, COLOR_GOOG);
    }

    // -----------------------------------------------------------------------
    // plotLine – draws line segments for one stock's closing prices
    //
    // Key formula notes
    //   drawable width  = getWidth()  - PAD          (x from PAD to right edge)
    //   drawable height = getHeight() - PAD          (y from 0   to h - PAD)
    //
    //   x pixel: PAD + (dayIndex / totalDays) * drawableWidth
    //   y pixel: (h - PAD) - (price / maxPrice) * drawableHeight
    //            ↑ subtract from bottom because screen y is inverted
    //
    // "Not all stock data starts on the same date" → if a series has fewer
    //  entries than totalDays, we right-align it so its last point always
    //  lands on the same x position as the other series' last point.
    // -----------------------------------------------------------------------
    private void plotLine(Graphics g, List<Float> prices, int totalDays,
                          float maxPrice, Color color) {
        if (prices.isEmpty()) return;

        int w = getWidth();
        int h = getHeight();

        int drawableWidth  = w - PAD;
        int drawableHeight = h - PAD;

        // How many days earlier does this series start compared to the longest one?
        int dayOffset = totalDays - prices.size();

        g.setColor(color);

        int prevX = -1, prevY = -1;

        for (int i = 0; i < prices.size(); i++) {
            // Align to the right: day index in the full time-range
            int dayIndex = dayOffset + i;

            int x = PAD + (int) ((double) dayIndex / totalDays * drawableWidth);
            int y = (h - PAD) - (int) ((prices.get(i) / maxPrice) * drawableHeight);

            if (prevX >= 0) {
                g.drawLine(prevX, prevY, x, y);
            }

            prevX = x;
            prevY = y;
        }
    }

    // -----------------------------------------------------------------------
    // drawLegend – provided template, unchanged
    // -----------------------------------------------------------------------
    private void drawLegend(Graphics g2d, String[] labels, Color[] colours) {
        final int width     = 50;
        final int height    = 30;
        final int rowHeight = 50;
        final int x         = 150;
        int y               = 100;

        for (int i = 0; i < colours.length; i++) {
            g2d.setColor(colours[i]);
            g2d.fillRect(x, y, width, height);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);

            g2d.drawString(labels[i], x + 60, y + 20);
            y += rowHeight;
        }
    }
}