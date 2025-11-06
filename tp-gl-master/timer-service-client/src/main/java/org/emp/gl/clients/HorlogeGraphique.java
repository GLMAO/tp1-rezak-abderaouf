package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class HorlogeGraphique extends JFrame implements TimerChangeListener {

    private final TimerService timerService;
    private final JLabel timeLabel;
    private final JLabel countdownLabel;
    private final JProgressBar progressBar;
    private final JButton startButton;
    private final JButton pauseButton;
    private final JButton stopButton;
    
    private int compteur = -1; // -1 = pas actif
    private int compteurInitial = 0;
    private boolean isPaused = false;

    public HorlogeGraphique(String title, TimerService service) {
        super(title);
        this.timerService = service;
        FlatDarkLaf.setup();

        // Interface
        this.timeLabel = new JLabel("", SwingConstants.CENTER);
        this.countdownLabel = new JLabel("", SwingConstants.CENTER);
        this.progressBar = new JProgressBar();
        this.startButton = new JButton("▶ START");
        this.pauseButton = new JButton("⏸ PAUSE");
        this.stopButton = new JButton("⏹ STOP");

        setupUI();
        this.timerService.addTimeChangeListener(this);
        updateTime();
        updateButtonStates();
    }

    private void setupUI() {
        // === COULEURS MODERNES ===
        Color bgPrimary = new Color(18, 18, 18);
        Color bgSecondary = new Color(28, 28, 30);
        Color accentBlue = new Color(10, 132, 255);
        Color accentGreen = new Color(48, 209, 88);
        Color accentRed = new Color(255, 69, 58);
        Color textPrimary = new Color(255, 255, 255);
        Color textSecondary = new Color(152, 152, 157);

        // === TITRE ===
        JLabel titleLabel = new JLabel("⏱ TIC TOC", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        titleLabel.setForeground(textPrimary);
        titleLabel.setBorder(new EmptyBorder(30, 20, 20, 20));

        // === HORLOGE - Carte moderne ===
        JPanel clockCard = createCard(bgSecondary);
        timeLabel.setFont(new Font("SF Mono", Font.BOLD, 72));
        timeLabel.setForeground(accentBlue);
        timeLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        clockCard.add(timeLabel);

        // === COMPTE À REBOURS - Carte moderne ===
        JPanel countdownCard = createCard(bgSecondary);
        countdownCard.setLayout(new BorderLayout(10, 10));
        
        // Titre "Countdown"
        JLabel countdownTitle = new JLabel("⏳ Countdown", SwingConstants.CENTER);
        countdownTitle.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        countdownTitle.setForeground(textSecondary);
        countdownCard.add(countdownTitle, BorderLayout.NORTH);
        
        countdownLabel.setFont(new Font("SF Pro Display", Font.BOLD, 48));
        countdownLabel.setForeground(accentGreen);
        countdownLabel.setText("00:00:00");
        countdownCard.add(countdownLabel, BorderLayout.CENTER);

        // === BARRE DE PROGRESSION MODERNE ===
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 22)); // 🔹 Taille du texte
        progressBar.setForeground(new Color(0, 200, 0));          // 🔹 Couleur de remplissage (vert)
        progressBar.setBackground(new Color(50, 50, 50));         // 🔹 Couleur de fond (gris foncé)
        progressBar.setStringPainted(true);                       // 🔹 Active l'affichage du texte
        progressBar.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2)); // contour léger
        progressBar.setString("");                 // texte par défaut

        countdownCard.add(progressBar, BorderLayout.SOUTH);

        // === BOUTONS MODERNES ===
        styleModernButton(startButton, accentGreen, textPrimary);
        styleModernButton(pauseButton, new Color(255, 159, 10), textPrimary);
        styleModernButton(stopButton, accentRed, textPrimary);

        startButton.addActionListener(e -> startCountdown());
        pauseButton.addActionListener(e -> togglePause());
        stopButton.addActionListener(e -> stopCountdown());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 20, 30, 20));
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);

        // === LAYOUT PRINCIPAL ===
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(bgPrimary);
        contentPanel.setBorder(new EmptyBorder(0, 40, 0, 40));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(clockCard);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(countdownCard);
        contentPanel.add(buttonPanel);

        // === CONFIGURATION FENÊTRE ===
        this.setTitle("TIME");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(contentPanel);
        this.setSize(800, 950);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private JPanel createCard(Color bgColor) {
        JPanel card = new JPanel();
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(48, 48, 50), 1, true),
            new EmptyBorder(20, 30, 20, 30)
        ));
        card.setMaximumSize(new Dimension(620, Integer.MAX_VALUE));
        return card;
    }

    private void styleModernButton(JButton button, Color bg, Color fg) {
        button.setFont(new Font("SF Pro Text", Font.BOLD, 15));
        button.setForeground(fg);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(140, 44));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(bg.brighter());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });
    }

    private void startCountdown() {
        if (compteur > 0 && isPaused) {
            // Reprendre
            isPaused = false;
            updateButtonStates();
            return;
        }

        JTextField hoursField = new JTextField("0");
        JTextField minutesField = new JTextField("0");
        JTextField secondsField = new JTextField("10");
        
        // Augmenter la taille des champs
        hoursField.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        minutesField.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        secondsField.setFont(new Font("SF Pro Text", Font.PLAIN, 16));

        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel hoursLabel = new JLabel("⏰ Heures :");
        JLabel minutesLabel = new JLabel("⏰ Minutes :");
        JLabel secondsLabel = new JLabel("⏰ Secondes :");
        hoursLabel.setFont(new Font("SF Pro Text", Font.BOLD, 15));
        minutesLabel.setFont(new Font("SF Pro Text", Font.BOLD, 15));
        secondsLabel.setFont(new Font("SF Pro Text", Font.BOLD, 15));
        
        panel.add(hoursLabel);
        panel.add(hoursField);
        panel.add(minutesLabel);
        panel.add(minutesField);
        panel.add(secondsLabel);
        panel.add(secondsField);
        
        panel.setPreferredSize(new Dimension(400, 180));

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Nouveau compte à rebours",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int h = Integer.parseInt(hoursField.getText());
                int m = Integer.parseInt(minutesField.getText());
                int s = Integer.parseInt(secondsField.getText());

                if (h < 0 || m < 0 || s < 0) {
                    showError("Veuillez entrer des valeurs positives.");
                    return;
                }

                compteur = h * 3600 + m * 60 + s;
                compteurInitial = compteur;
                
                if (compteur > 0) {
                    isPaused = false;
                    countdownLabel.setText(formatCountdown(compteur));
                    progressBar.setMaximum(compteurInitial);
                    progressBar.setValue(compteur);
                    progressBar.setString(String.format("%.0f%%", 100.0));
                    updateButtonStates();
                } else {
                    showError("La durée doit être supérieure à zéro.");
                }

            } catch (NumberFormatException ex) {
                showError("Veuillez entrer des nombres valides.");
            }
        }
    }

    private void togglePause() {
        if (compteur > 0) {
            isPaused = !isPaused;
            pauseButton.setText(isPaused ? "▶ RESUME" : "⏸ PAUSE");
            countdownLabel.setForeground(isPaused ? 
                new Color(255, 159, 10) : new Color(48, 209, 88));
        }
    }

    private void stopCountdown() {
        compteur = -1;
        compteurInitial = 0;
        isPaused = false;
        countdownLabel.setText("00:00:00");
        countdownLabel.setForeground(new Color(48, 209, 88));
        progressBar.setValue(0);
        progressBar.setString("");
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean hasCountdown = compteur > 0;
        startButton.setEnabled(!hasCountdown || isPaused);
        startButton.setText((hasCountdown && isPaused) ? "▶ RESUME" : "▶ START");
        pauseButton.setEnabled(hasCountdown && !isPaused);
        stopButton.setEnabled(hasCountdown);
        
        pauseButton.setText(isPaused ? "▶ RESUME" : "⏸ PAUSE");
    }

    private void updateTime() {
        String currentTime = String.format("%02d:%02d:%02d",
                timerService.getHeures(),
                timerService.getMinutes(),
                timerService.getSecondes());
        timeLabel.setText(currentTime);
    }

    private String formatCountdown(int totalSeconds) {
        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TimerChangeListener.SECONDE_PROP.equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(() -> {
                updateTime();
                
                if (compteur > 0 && !isPaused) {
                    compteur--;
                    countdownLabel.setText(formatCountdown(compteur));
                    progressBar.setValue(compteur);
                    
                    double percentage = (compteur * 100.0) / compteurInitial;
                    progressBar.setString(String.format("%.0f%%", percentage));
                    
                    // Changer la couleur selon le temps restant
                    if (percentage <= 20) {
                        progressBar.setForeground(new Color(255, 69, 58));
                        countdownLabel.setForeground(new Color(255, 69, 58));
                    } else if (percentage <= 50) {
                        progressBar.setForeground(new Color(255, 159, 10));
                    }
                    
                    updateButtonStates();
                    
                } else if (compteur == 0) {
                    countdownLabel.setText("✅ FINISHED");
                    countdownLabel.setForeground(new Color(48, 209, 88));
                    progressBar.setValue(0);
                    progressBar.setString("FINISHED!");
                    progressBar.setForeground(new Color(48, 209, 88));
                    compteur = -1;
                    updateButtonStates();
                    
                    // Son de notification (optionnel)
                    Toolkit.getDefaultToolkit().beep();
                }
            });
        }
    }
}