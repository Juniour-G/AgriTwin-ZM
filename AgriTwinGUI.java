import agritwinzm2.Maize;
import agritwinzm2.SupplyDemandEngine;
import agritwinzm2.Tomato;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class AgriTwinGUI {

    static final Color GREEN_DARK   = new Color(26, 92, 26);
    static final Color GREEN_LIGHT  = new Color(76, 175, 80);
    static final Color YELLOW       = new Color(255, 193, 7);
    static final Color YELLOW_LIGHT = new Color(255, 248, 225);
    static final Color WHITE        = Color.WHITE;
    static final Color TEXT_DARK    = new Color(30, 30, 30);

    static class NumericFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string,
                                 AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String result  = current.substring(0, offset) + string + current.substring(offset);
            if (isValid(result)) super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text,
                            AttributeSet attrs) throws BadLocationException {
            if (text == null) { super.replace(fb, offset, length, text, attrs); return; }
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String result  = current.substring(0, offset) + text + current.substring(offset + length);
            if (isValid(result)) super.replace(fb, offset, length, text, attrs);
        }

        private boolean isValid(String text) {
            if (text.isEmpty()) return true;
            try { Double.parseDouble(text); return true; }
            catch (NumberFormatException e) { return false; }
        }
    }

    // CardLayout pages
    static final String PAGE_INPUT   = "INPUT";
    static final String PAGE_RESULTS = "RESULTS";

    public AgriTwinGUI() {

        JFrame frame = new JFrame("AgriTwin-ZM Simulation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));

        // ── NAV BAR ──────────────────────────────────────────
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(new Color(20, 60, 20));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        navPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel logoLabel = new JLabel("AgriTwin-ZM");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setForeground(YELLOW);
        JButton navBtn = createBtn("Run Now +", YELLOW, GREEN_DARK);
        navPanel.add(logoLabel, BorderLayout.WEST);
        navPanel.add(navBtn, BorderLayout.EAST);
        frame.add(navPanel, BorderLayout.NORTH);

        // ── CARD LAYOUT (switches between pages) ─────────────
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // ══════════════════════════════════════════════════════
        //  PAGE 1 — INPUT PAGE
        // ══════════════════════════════════════════════════════
        JPanel inputPage = new JPanel();
        inputPage.setLayout(new BoxLayout(inputPage, BoxLayout.Y_AXIS));
        inputPage.setBackground(WHITE);

        // HERO PANEL
        JPanel heroPanel = new JPanel(new BorderLayout()) {
            private BufferedImage bgImage;
            {
                try { bgImage = ImageIO.read(new File("farm.jpg")); }
                catch (Exception ex) { bgImage = null; }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                if (bgImage != null) {
                    int panelW = getWidth(); int panelH = getHeight();
                    int imgW = bgImage.getWidth(); int imgH = bgImage.getHeight();
                    double scale = Math.max((double) panelW / imgW, (double) panelH / imgH);
                    int drawW = (int)(imgW * scale); int drawH = (int)(imgH * scale);
                    int drawX = (panelW - drawW) / 2; int drawY = (panelH - drawH) / 2;
                    g2d.drawImage(bgImage, drawX, drawY, drawW, drawH, this);
                } else {
                    g2d.setColor(new Color(26, 92, 26));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                g2d.setColor(new Color(5, 30, 5, 200));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        heroPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 320));
        heroPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        heroPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JPanel heroInner = new JPanel();
        heroInner.setLayout(new BoxLayout(heroInner, BoxLayout.Y_AXIS));
        heroInner.setOpaque(false);

        JLabel heroTag = new JLabel("Smart Agricultural Simulation");
        heroTag.setFont(new Font("Arial", Font.PLAIN, 11));
        heroTag.setForeground(new Color(255, 255, 255, 200));
        heroTag.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1, true),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        heroTag.setOpaque(false);
        heroTag.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heroTitle = new JLabel("<html><b>AgriTwin-ZM<br>Simulation System</b></html>");
        heroTitle.setFont(new Font("Arial", Font.BOLD, 36));
        heroTitle.setForeground(WHITE);
        heroTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));

        JLabel heroSub = new JLabel("<html>Enter crop data and get real-time supply,<br>demand and revenue insights for Zambia.</html>");
        heroSub.setFont(new Font("Arial", Font.PLAIN, 14));
        heroSub.setForeground(new Color(220, 220, 220));
        heroSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroSub.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JPanel heroBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        heroBtns.setOpaque(false);
        heroBtns.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnSimulate = createBtn("Simulate Now", YELLOW, GREEN_DARK);
        JButton btnReports  = createBtn("View Reports", new Color(0, 0, 0, 0), WHITE);
        btnReports.setOpaque(false);
        btnReports.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WHITE, 1, true),
                BorderFactory.createEmptyBorder(9, 16, 9, 16)));
        heroBtns.add(btnSimulate);
        heroBtns.add(btnReports);
        heroInner.add(heroTag);
        heroInner.add(heroTitle);
        heroInner.add(heroSub);
        heroInner.add(heroBtns);
        heroPanel.add(heroInner, BorderLayout.CENTER);
        inputPage.add(heroPanel);

        // STATS BAR
        JPanel statsBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 12));
        statsBar.setBackground(YELLOW);
        statsBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        JLabel stat1 = new JLabel("500+ Farms Tracked in Zambia");
        stat1.setFont(new Font("Arial", Font.BOLD, 13));
        stat1.setForeground(GREEN_DARK);
        JLabel statDiv = new JLabel("|");
        statDiv.setForeground(new Color(0, 0, 0, 60));
        JLabel stat2 = new JLabel("Real-Time Supply and Demand Engine");
        stat2.setFont(new Font("Arial", Font.BOLD, 13));
        stat2.setForeground(GREEN_DARK);
        statsBar.add(stat1); statsBar.add(statDiv); statsBar.add(stat2);
        inputPage.add(statsBar);

        // INPUT SECTION
        JPanel simSection = new JPanel();
        simSection.setLayout(new BoxLayout(simSection, BoxLayout.Y_AXIS));
        simSection.setBackground(WHITE);
        simSection.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(WHITE);
        centerWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel simTitle = new JLabel("Agricultural Data Input");
        simTitle.setFont(new Font("Arial", Font.BOLD, 22));
        simTitle.setForeground(GREEN_DARK);
        simTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        simTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        centerWrapper.add(simTitle);

        // CROP CARDS
        JPanel cropRow = new JPanel(new GridLayout(1, 2, 20, 0));
        cropRow.setBackground(WHITE);
        cropRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        cropRow.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));

        JPanel maizeCard = createSectionCard("Maize Data", YELLOW);
        JTextField mProd  = addFieldToCard(maizeCard, "Production (tons)");
        JTextField mStore = addFieldToCard(maizeCard, "Storage Capacity");
        JTextField mMoist = addFieldToCard(maizeCard, "Moisture Level (%)");
        JTextField mPrice = addFieldToCard(maizeCard, "Price Per Ton (ZMW)");
        JTextField mLoss  = addFieldToCard(maizeCard, "Loss Percent (%)");
        cropRow.add(maizeCard);

        JPanel tomatoCard = createSectionCard("Tomato Data", GREEN_LIGHT);
        JTextField tProd  = addFieldToCard(tomatoCard, "Production (tons)");
        JTextField tStore = addFieldToCard(tomatoCard, "Storage Capacity");
        JTextField tSpoil = addFieldToCard(tomatoCard, "Spoilage Rate (%)");
        JTextField tPrice = addFieldToCard(tomatoCard, "Price Per Ton (ZMW)");
        cropRow.add(tomatoCard);

        centerWrapper.add(cropRow);
        centerWrapper.add(Box.createVerticalStrut(14));

        // DEMAND CARD
        JPanel demandCard = createSectionCard("Market Demand", GREEN_DARK);
        demandCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        demandCard.setMaximumSize(new Dimension(900, 120));
        JPanel demandRow = new JPanel(new GridLayout(1, 2, 16, 0));
        demandRow.setBackground(WHITE);
        JTextField mDem = createNumericField();
        JTextField tDem = createNumericField();
        demandRow.add(wrapField("Maize Demand (tons)", mDem));
        demandRow.add(wrapField("Tomato Demand (tons)", tDem));
        demandCard.add(demandRow);
        centerWrapper.add(demandCard);
        centerWrapper.add(Box.createVerticalStrut(20));

        // BUTTONS
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        actionBar.setBackground(WHITE);
        actionBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton btnClear = createBtn("Clear Fields",   WHITE,      TEXT_DARK);
        JButton btnRun   = createBtn("Run Simulation", GREEN_DARK, YELLOW);
        actionBar.add(btnClear);
        actionBar.add(btnRun);
        centerWrapper.add(actionBar);
        centerWrapper.add(Box.createVerticalStrut(30));

        simSection.add(centerWrapper);
        inputPage.add(simSection);

        // STATUS BAR
        JLabel statusBar = new JLabel("  Ready — enter data and click Run Simulation");
        statusBar.setFont(new Font("Arial", Font.ITALIC, 11));
        statusBar.setForeground(YELLOW);
        statusBar.setBackground(new Color(20, 60, 20));
        statusBar.setOpaque(true);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        JScrollPane inputScroll = new JScrollPane(inputPage);
        inputScroll.setBorder(null);
        inputScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputScroll.getVerticalScrollBar().setUnitIncrement(16);
        inputScroll.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                inputPage.setPreferredSize(new Dimension(
                        inputScroll.getViewport().getWidth(),
                        inputPage.getPreferredSize().height));
                inputPage.revalidate();
            }
        });

        // ══════════════════════════════════════════════════════
        //  PAGE 2 — RESULTS PAGE
        // ══════════════════════════════════════════════════════
        JPanel resultsPage = new JPanel(new BorderLayout());
        resultsPage.setBackground(WHITE);

        // Results top bar
        JPanel resultsTopBar = new JPanel(new BorderLayout());
        resultsTopBar.setBackground(GREEN_DARK);
        resultsTopBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        JLabel resultsPageTitle = new JLabel("Simulation Results");
        resultsPageTitle.setFont(new Font("Arial", Font.BOLD, 22));
        resultsPageTitle.setForeground(YELLOW);
        JButton btnBack = createBtn("Back to Input", YELLOW, GREEN_DARK);
        resultsTopBar.add(resultsPageTitle, BorderLayout.WEST);
        resultsTopBar.add(btnBack, BorderLayout.EAST);
        resultsPage.add(resultsTopBar, BorderLayout.NORTH);

        // Results content
        JPanel resultsContent = new JPanel();
        resultsContent.setLayout(new BoxLayout(resultsContent, BoxLayout.Y_AXIS));
        resultsContent.setBackground(WHITE);
        resultsContent.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Date label
        JLabel dateLabel = new JLabel();
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        resultsContent.add(dateLabel);

        // Results grid
        JPanel resultsGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        resultsGrid.setBackground(WHITE);
        resultsGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsGrid.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));

        // Maize result card
        JPanel maizeResult = new JPanel();
        maizeResult.setLayout(new BoxLayout(maizeResult, BoxLayout.Y_AXIS));
        maizeResult.setBackground(WHITE);
        maizeResult.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(YELLOW, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel rMaizeTitle = new JLabel("Maize");
        rMaizeTitle.setFont(new Font("Arial", Font.BOLD, 16));
        rMaizeTitle.setForeground(GREEN_DARK);
        rMaizeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rMaizeCrop = resultLabel("Available (tons)", 12, new Color(100,100,100));
        JLabel rMaizeVal  = resultLabel("--", 32, GREEN_DARK);
        JLabel rMaizeStat = resultLabel("--", 13, GREEN_DARK);
        JLabel rMaizeRev  = resultLabel("--", 13, new Color(80,80,80));

        maizeResult.add(rMaizeTitle);
        maizeResult.add(Box.createVerticalStrut(8));
        maizeResult.add(rMaizeCrop);
        maizeResult.add(rMaizeVal);
        maizeResult.add(Box.createVerticalStrut(8));
        maizeResult.add(rMaizeStat);
        maizeResult.add(Box.createVerticalStrut(4));
        maizeResult.add(rMaizeRev);

        // Tomato result card
        JPanel tomResult = new JPanel();
        tomResult.setLayout(new BoxLayout(tomResult, BoxLayout.Y_AXIS));
        tomResult.setBackground(WHITE);
        tomResult.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GREEN_LIGHT, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel rTomTitle = new JLabel("Tomato");
        rTomTitle.setFont(new Font("Arial", Font.BOLD, 16));
        rTomTitle.setForeground(GREEN_DARK);
        rTomTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rTomCrop = resultLabel("Available (tons)", 12, new Color(100,100,100));
        JLabel rTomVal  = resultLabel("--", 32, GREEN_DARK);
        JLabel rTomStat = resultLabel("--", 13, GREEN_DARK);
        JLabel rTomRev  = resultLabel("--", 13, new Color(80,80,80));

        tomResult.add(rTomTitle);
        tomResult.add(Box.createVerticalStrut(8));
        tomResult.add(rTomCrop);
        tomResult.add(rTomVal);
        tomResult.add(Box.createVerticalStrut(8));
        tomResult.add(rTomStat);
        tomResult.add(Box.createVerticalStrut(4));
        tomResult.add(rTomRev);

        resultsGrid.add(maizeResult);
        resultsGrid.add(tomResult);
        resultsContent.add(resultsGrid);
        resultsContent.add(Box.createVerticalStrut(24));

        // Total Revenue bar
        JPanel totalBar = new JPanel(new BorderLayout());
        totalBar.setBackground(GREEN_DARK);
        totalBar.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        totalBar.setMaximumSize(new Dimension(1000, 80));
        totalBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel totalLbl = new JLabel("Total Revenue");
        totalLbl.setFont(new Font("Arial", Font.BOLD, 14));
        totalLbl.setForeground(new Color(255, 255, 255, 180));
        JLabel rTotal = new JLabel("ZMW 0");
        rTotal.setFont(new Font("Arial", Font.BOLD, 28));
        rTotal.setForeground(YELLOW);
        totalBar.add(totalLbl, BorderLayout.WEST);
        totalBar.add(rTotal, BorderLayout.EAST);
        resultsContent.add(totalBar);
        resultsContent.add(Box.createVerticalStrut(24));

        // Save button on results page
        JPanel saveBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveBar.setBackground(WHITE);
        saveBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton btnSave = createBtn("Save Report", YELLOW, GREEN_DARK);
        saveBar.add(btnSave);
        resultsContent.add(saveBar);

        JScrollPane resultsScroll = new JScrollPane(resultsContent);
        resultsScroll.setBorder(null);
        resultsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultsScroll.getVerticalScrollBar().setUnitIncrement(16);
        resultsPage.add(resultsScroll, BorderLayout.CENTER);

        // Results status bar
        JLabel resultsStatusBar = new JLabel("  Simulation completed successfully");
        resultsStatusBar.setFont(new Font("Arial", Font.ITALIC, 11));
        resultsStatusBar.setForeground(YELLOW);
        resultsStatusBar.setBackground(new Color(20, 60, 20));
        resultsStatusBar.setOpaque(true);
        resultsStatusBar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        resultsPage.add(resultsStatusBar, BorderLayout.SOUTH);

        // ── ADD PAGES TO CARD ─────────────────────────────────
        cardPanel.add(inputScroll, PAGE_INPUT);
        cardPanel.add(resultsPage, PAGE_RESULTS);

        frame.add(cardPanel, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);

        // ── REAL-TIME CALCULATION LISTENER ───────────────────
        Runnable recalculate = () -> {
            try {
                double maizeProduction  = parseField(mProd);
                double maizeStorage     = parseField(mStore);
                double maizeMoisture    = parseField(mMoist);
                double maizePrice       = parseField(mPrice);
                double maizeLoss        = parseField(mLoss);
                double tomatoProduction = parseField(tProd);
                double tomatoStorage    = parseField(tStore);
                double tomatoSpoilage   = parseField(tSpoil);
                double tomatoPrice      = parseField(tPrice);
                double maizeDemand      = parseField(mDem);
                double tomatoDemand     = parseField(tDem);

                Maize  maize  = new Maize(maizeProduction, maizeStorage, maizeMoisture, maizePrice, maizeLoss);
                Tomato tomato = new Tomato(tomatoProduction, tomatoStorage, tomatoSpoilage, tomatoPrice);

                double maizeAvail = maize.availableMaize();
                double tomAvail   = tomato.availableTomatoes();
                double maizeRev   = maize.calculateRevenue();
                double tomRev     = tomato.calculateRevenue();
                String maizeStat  = SupplyDemandEngine.checkSupply(maizeAvail, maizeDemand);
                String tomStat    = SupplyDemandEngine.checkSupply(tomAvail, tomatoDemand);

                rMaizeVal.setText(String.format("%.1f", maizeAvail));
                rTomVal.setText(String.format("%.1f", tomAvail));
                rMaizeStat.setText("Status: " + maizeStat);
                rTomStat.setText("Status: " + tomStat);
                rMaizeRev.setText("Revenue: ZMW " + String.format("%,.0f", maizeRev));
                rTomRev.setText("Revenue: ZMW " + String.format("%,.0f", tomRev));
                rTotal.setText("ZMW " + String.format("%,.0f", maizeRev + tomRev));

                statusBar.setText("  Live calculation updated");
            } catch (Exception ex) {
                // fields not fully filled yet — ignore silently
            }
        };

        // Attach real-time listener to every field
        for (JTextField f : new JTextField[]{mProd, mStore, mMoist, mPrice, mLoss,
                tProd, tStore, tSpoil, tPrice, mDem, tDem}) {
            f.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e)  { recalculate.run(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e)  { recalculate.run(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { recalculate.run(); }
            });
        }

        // ── BUTTON ACTIONS ────────────────────────────────────
        btnRun.addActionListener(e -> {
            if (mProd.getText().isEmpty() || mStore.getText().isEmpty() ||
                    mMoist.getText().isEmpty() || mPrice.getText().isEmpty() ||
                    mLoss.getText().isEmpty()  || tProd.getText().isEmpty() ||
                    tStore.getText().isEmpty() || tSpoil.getText().isEmpty() ||
                    tPrice.getText().isEmpty() || mDem.getText().isEmpty()  ||
                    tDem.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please fill in all fields before running.",
                        "Missing Input", JOptionPane.WARNING_MESSAGE);
                statusBar.setText("  Warning: All fields are required");
                return;
            }
            // Set date and time on results page
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter fmt =
                    java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy  |  HH:mm:ss");
            dateLabel.setText("Report generated: " + now.format(fmt));

            recalculate.run();
            cardLayout.show(cardPanel, PAGE_RESULTS);
            statusBar.setText("  Simulation completed successfully");
        });

        btnSimulate.addActionListener(e -> btnRun.doClick());
        navBtn.addActionListener(e -> btnRun.doClick());

        btnBack.addActionListener(e -> {
            cardLayout.show(cardPanel, PAGE_INPUT);
            statusBar.setText("  Back to input — you can edit and re-run");
        });

        btnReports.addActionListener(e -> {
            if (rMaizeVal.getText().equals("--")) {
                JOptionPane.showMessageDialog(frame,
                        "Run a simulation first to view results.",
                        "No Results Yet", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            cardLayout.show(cardPanel, PAGE_RESULTS);
        });

        btnClear.addActionListener(e -> {
            for (JTextField f : new JTextField[]{mProd, mStore, mMoist, mPrice, mLoss,
                    tProd, tStore, tSpoil, tPrice, mDem, tDem})
                f.setText("");
            statusBar.setText("  Fields cleared — ready for new simulation");
        });

        btnSave.addActionListener(e -> {
            try {
                FileWriter w = new FileWriter("simulation_report.txt");
                w.write("AgriTwin-ZM Simulation Report\n");
                w.write(dateLabel.getText() + "\n\n");
                w.write("Maize Available: " + rMaizeVal.getText() + " tons\n");
                w.write("Maize Status:    " + rMaizeStat.getText() + "\n");
                w.write("Maize Revenue:   " + rMaizeRev.getText() + "\n\n");
                w.write("Tomato Available: " + rTomVal.getText() + " tons\n");
                w.write("Tomato Status:    " + rTomStat.getText() + "\n");
                w.write("Tomato Revenue:   " + rTomRev.getText() + "\n\n");
                w.write("Total Revenue:    " + rTotal.getText() + "\n");
                w.close();
                JOptionPane.showMessageDialog(frame, "Report saved to simulation_report.txt");
                resultsStatusBar.setText("  Report saved successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Error saving report.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addHover(btnRun,   GREEN_DARK, new Color(10, 70, 10));
        addHover(btnClear, WHITE,      new Color(220, 220, 220));
        addHover(btnSave,  YELLOW,     new Color(255, 210, 0));
        addHover(btnBack,  YELLOW,     new Color(255, 210, 0));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ── HELPERS ───────────────────────────────────────────────

    private double parseField(JTextField f) {
        String t = f.getText().trim();
        return t.isEmpty() ? 0 : Double.parseDouble(t);
    }

    private JTextField createNumericField() {
        JTextField field = new JTextField();
        styleField(field);
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericFilter());
        return field;
    }

    private JPanel createSectionCard(String title, Color borderColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(GREEN_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lbl);
        return panel;
    }

    private JTextField addFieldToCard(JPanel card, String label) {
        JTextField field = createNumericField();
        JPanel row = wrapField(label, field);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(row);
        return field;
    }

    private JPanel wrapField(String label, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(8, 2));
        row.setBackground(WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(TEXT_DARK);
        lbl.setPreferredSize(new Dimension(160, 22));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBackground(YELLOW_LIGHT);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GREEN_LIGHT, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        field.setToolTipText("Numbers only");
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBackground(WHITE);
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(YELLOW, 2),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            }
            public void focusLost(FocusEvent e) {
                field.setBackground(YELLOW_LIGHT);
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GREEN_LIGHT, 1),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            }
        });
    }

    private JButton createBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addHover(JButton btn, Color normal, Color hover) {
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(normal); }
        });
    }
 
    private JLabel resultLabel(String text, int size, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", size > 12 ? Font.BOLD : Font.PLAIN, size));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new AgriTwinGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

