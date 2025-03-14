import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboard {
    private JFrame frame;

    public AdminDashboard() {
        // Controleer of de gebruiker ingelogd is als admin
        if (!LoginScreen.isLoggedInAsAdmin) {
            new LoginScreen();  // Als niet ingelogd, toon het inlogscherm
            return;
        }

        frame = new JFrame("Unasat Studenten Administratie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Donkergrijze achtergrond

        // Bovenste balk (Sluiten, Titel, Uitloggen)
        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setOpaque(false);
        topBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ruimte rondom

        // Sluitenknop (linksboven)
        JButton closeButton = createStyledButton("Sluiten", new Color(255, 0, 0), Color.WHITE); // Rood
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        closeButton.setPreferredSize(new Dimension(80, 25)); // Kleinere knop
        closeButton.addActionListener(e -> confirmExit()); // Sluiten functionaliteit
        topBarPanel.add(closeButton, BorderLayout.WEST);

        // Titel in het midden (Oranje)
        JLabel titleLabel = new JLabel("Studenten Administratie", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Grotere tekst
        titleLabel.setForeground(new Color(255, 165, 0)); // Oranje
        topBarPanel.add(titleLabel, BorderLayout.CENTER);

        // Uitlogknop (rechtsboven)
        JButton logoutButton = createStyledButton("Uitloggen", new Color(0, 100, 0), Color.WHITE); // Darkgreen
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        logoutButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        logoutButton.addActionListener(e -> logout()); // Uitloggen functionaliteit
        topBarPanel.add(logoutButton, BorderLayout.EAST);

        frame.add(topBarPanel, BorderLayout.NORTH);

        // Hoofdpaneel voor knoppen
        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setLayout(new BoxLayout(mainButtonPanel, BoxLayout.Y_AXIS));
        mainButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Kleinere marges
        mainButtonPanel.setOpaque(false);

        // Tekst "Toevoegen" (links)
        JLabel toevoegenLabel = new JLabel("Toevoegen");
        toevoegenLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Grotere tekst
        toevoegenLabel.setForeground(Color.WHITE); // Witte tekst
        toevoegenLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Links uitlijnen
        toevoegenLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Kleinere ruimte
        mainButtonPanel.add(toevoegenLabel);

        // Bovenste rij (Toevoegen knoppen)
        JPanel topRowPanel = createButtonPanel();
        JButton addStudentButton = createStyledButton("Student Toevoegen", new Color(0, 29, 109), Color.WHITE); // #001d6d
        addStudentButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        addStudentButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        addStudentButton.addActionListener(e -> new AddStudentGUI()); // Open AddStudentGUI
        topRowPanel.add(addStudentButton);

        JButton addSubjectButton = createStyledButton("Vak Toevoegen", new Color(211, 85, 0), Color.WHITE); // #d35500
        addSubjectButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        addSubjectButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        addSubjectButton.addActionListener(e -> new AddVakGUI()); // Open AddVakGUI
        topRowPanel.add(addSubjectButton);

        JButton addGradeButton = createStyledButton("Cijfer Toevoegen", new Color(64, 171, 254), Color.WHITE); // #40abfe
        addGradeButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        addGradeButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        addGradeButton.addActionListener(e -> new AddCijfersGUI()); // Open AddCijfersGUI
        topRowPanel.add(addGradeButton);

        mainButtonPanel.add(topRowPanel);

        // Tekst "Overzichtigen" (links)
        JLabel overzichtLabel = new JLabel("Overzichtigen");
        overzichtLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Grotere tekst
        overzichtLabel.setForeground(Color.WHITE); // Witte tekst
        overzichtLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Links uitlijnen
        overzichtLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0)); // Kleinere ruimte
        mainButtonPanel.add(overzichtLabel);

        // Middelste rij (Overzicht knoppen)
        JPanel middleRowPanel = createButtonPanel();
        JButton overviewStudentsButton = createStyledButton("Overzicht Studenten", new Color(0, 29, 109), Color.WHITE); // #001d6d
        overviewStudentsButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        overviewStudentsButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        overviewStudentsButton.addActionListener(e -> {
            try {
                List<Student> students = API.getStudents();
                new OverzichtStudentenGUI(students);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Fout bij het ophalen van studenten: " + ex.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
            }
        });
        middleRowPanel.add(overviewStudentsButton);

        JButton overviewSubjectsButton = createStyledButton("Overzicht Vakken", new Color(211, 85, 0), Color.WHITE); // #d35500
        overviewSubjectsButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        overviewSubjectsButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
//        overviewSubjectsButton.addActionListener(e -> new OverzichtVakkenGUI()); // Open OverzichtVakkenGUI
        middleRowPanel.add(overviewSubjectsButton);

        JButton overviewGradesButton = createStyledButton("Overzicht Cijfers", new Color(64, 171, 254), Color.WHITE); // #40abfe
        overviewGradesButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        overviewGradesButton.addActionListener(e -> {
            try {
                // Haal de cijfers op met volledige namen
                List<Grade> scores = API.getScoresWithStudentNames();
                // Open het overzicht met de opgehaalde cijfers
                new OverzichtCijfersGUI(scores);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Fout bij het ophalen van cijfers: " + ex.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
            }
        });
        middleRowPanel.add(overviewGradesButton);

        mainButtonPanel.add(middleRowPanel);

        // Derde rij (Cohorten en Semesters)
        JPanel bottomRowPanel = createButtonPanel();
        JButton overviewCohortsButton = createStyledButton("Overzicht Cohorten", new Color(138, 43, 226), Color.WHITE); // #8A2BE2 (Blauwviolet)
        overviewCohortsButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        overviewCohortsButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        overviewCohortsButton.addActionListener(e -> showOverview("Cohorten"));
        bottomRowPanel.add(overviewCohortsButton);

        JButton overviewSemestersButton = createStyledButton("Overzicht Semesters", new Color(255, 105, 180), Color.WHITE); // #FF69B4 (Roze)
        overviewSemestersButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        overviewSemestersButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        overviewSemestersButton.addActionListener(e -> {
            try {
                List<Semester> semesters = API.getSemesters();
                new OverzichtSemestersGUI(semesters);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Fout bij het ophalen van semesters: " + ex.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
            }
        });
        bottomRowPanel.add(overviewSemestersButton);

        mainButtonPanel.add(bottomRowPanel);

        frame.add(mainButtonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0, 5, 5)); // Kleinere ruimte tussen knoppen
        panel.setOpaque(false);
        return panel;
    }

    private JButton createStyledButton(String text, Color backgroundColor, Color foregroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(backgroundColor.darker(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10))); // Kleinere padding
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setRolloverEnabled(true);
        button.addChangeListener(e -> {
            if (button.getModel().isRollover()) {
                button.setBackground(backgroundColor.brighter()); // Lichtere kleur bij hover
            } else {
                button.setBackground(backgroundColor);
            }
        });
        return button;
    }

    private void showOverview(String overviewType) {
        JOptionPane.showMessageDialog(frame, "Overzicht van: " + overviewType);
    }

    private void confirmExit() {
        int response = JOptionPane.showConfirmDialog(frame, "Weet u zeker dat u het programma wilt afsluiten?", "Bevestiging", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            frame.dispose();
        }
    }

    private void logout() {
        int response = JOptionPane.showConfirmDialog(frame, "Weet u zeker dat u wilt uitloggen?", "Bevestiging", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            frame.dispose();
            LoginScreen.isLoggedInAsAdmin = false;  // Zet loginstatus uit
            new LoginScreen(); // Terug naar het login scherm
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}