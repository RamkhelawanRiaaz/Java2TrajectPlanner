package GUI;

import API_calls.API;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import models.Course;
import models.Grade;
import models.Semester;
import models.Student;

public class AdminDashboard {
    private JFrame frame;
    private API API; // RS

    public AdminDashboard() {
        // Controleer of de gebruiker ingelogd is als admin
        if (!LoginScreen.isLoggedInAsAdmin) {
            new LoginScreen();  // Als niet ingelogd, toon het inlogscherm
            return;
        }

        API = new API(); // RS
        frame = new JFrame("Unasat Studenten Administratie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Donkergrijze achtergrond

        // Bovenste balk (Sluiten, Titel, Uitloggen)
        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setOpaque(false);
        topBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ruimte rondom

        // West panel
        JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        westPanel.setBackground(new Color(30, 30, 30));

        // East panel
        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        eastPanel.setBackground(new Color(30, 30, 30));

        // Sluitenknop (linksboven)
        JButton closeButton = createStyledButton("Sluiten", new Color(255, 0, 0), Color.WHITE); // Rood
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        closeButton.setPreferredSize(new Dimension(80, 25)); // Kleinere knop
        closeButton.addActionListener(e -> confirmExit()); // Sluiten functionaliteit
        westPanel.add(closeButton);
        topBarPanel.add(westPanel, BorderLayout.WEST);

        // Uitleg button dat explain() roept
        JButton uitlegButton = new JButton("Hulp en Uitleg");
        uitlegButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                explain(); }});
        uitlegButton.setBackground(new Color(211, 85, 0));
        uitlegButton.setForeground(Color.WHITE);
        westPanel.add(uitlegButton);
        topBarPanel.add(westPanel, BorderLayout.WEST);

        // Titel in het midden (Oranje)
        JLabel titleLabel = new JLabel("Studenten Administratie", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Grotere tekst
        titleLabel.setForeground(new Color(255, 165, 0)); // Oranje
        topBarPanel.add(titleLabel, BorderLayout.CENTER);

        // Leden button dat groepsleden() oproept
        JButton ledenButton = new JButton("Groepsleden");
        ledenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groepsleden(); }});
        ledenButton.setBackground(new Color(0, 29, 109));
        ledenButton.setForeground(Color.WHITE);
        eastPanel.add(ledenButton);
        topBarPanel.add(eastPanel, BorderLayout.EAST);

        // Uitlogknop (rechtsboven) dat logout() oproept
        JButton logoutButton = createStyledButton("Uitloggen", new Color(0, 100, 0), Color.WHITE); // Green
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        logoutButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        logoutButton.addActionListener(e -> logout()); // Uitloggen functionaliteit
        eastPanel.add(logoutButton);
        topBarPanel.add(eastPanel, BorderLayout.EAST);

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

        // add student button dat AddStudentGUI opent
        JButton addStudentButton = createStyledButton("Student Toevoegen", new Color(0, 29, 109), Color.WHITE); // #001d6d
        addStudentButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        addStudentButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        addStudentButton.addActionListener(e -> new AddStudentGUI()); // Open GUI.AddStudentGUI
        topRowPanel.add(addStudentButton);

        // add tentamen button dat AddTentamenGUI opent
        JButton addSubjectButton = createStyledButton("Tentamen Toevoegen", new Color(211, 85, 0), Color.WHITE); // #d35500
        addSubjectButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        addSubjectButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        addSubjectButton.addActionListener(e -> new AddTentamenGUI()); // Open GUI.AddVakGUI
        topRowPanel.add(addSubjectButton);

        // add grade button dat AddGradeGUI opent
        JButton addGradeButton = createStyledButton("Cijfer Toevoegen", new Color(64, 171, 254), Color.WHITE); // #40abfe
        addGradeButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        addGradeButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        addGradeButton.addActionListener(e -> new AddCijfersGUI()); // Open GUI.AddCijfersGUI
        topRowPanel.add(addGradeButton);

        mainButtonPanel.add(topRowPanel);

        // Tekst "Overzichtigen" (links)
        JLabel overzichtLabel = new JLabel("Overzichten");
        overzichtLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Grotere tekst
        overzichtLabel.setForeground(Color.WHITE); // Witte tekst
        overzichtLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Links uitlijnen
        overzichtLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0)); // Kleinere ruimte
        mainButtonPanel.add(overzichtLabel);

        // Middelste rij (Overzicht knoppen)
        JPanel middleRowPanel = createButtonPanel();

        // stud overzicht roept getStudent() van API class; slaat op in List; opent OverzichtStudentsGUI
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

        // tenta overzicht opent OverzichtTentamenGUI
        JButton overviewSubjectsButton = createStyledButton("Tentamen Overzichten", new Color(211, 85, 0), Color.WHITE); // #d35500
        overviewSubjectsButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        overviewSubjectsButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        overviewSubjectsButton.addActionListener(e -> new OverzichtTentamensGUI()); // Open OverzichtTentamenGUI
        middleRowPanel.add(overviewSubjectsButton);

        // grade w names overzicht roept method van API class; slaat op in List; opent OverzichtCijfersGUI()
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

        // courses overzicht roept getCourses() van API class; slaat op in List; opent OverzichtCoursesGUI
        JButton overviewCoursesButton = createStyledButton("Overzicht Studieonderdelen", new Color(138, 43, 226), Color.WHITE);
        overviewCoursesButton.setPreferredSize(new Dimension(100, 25)); // Kleinere knop
        overviewCoursesButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Grotere tekst
        overviewCoursesButton.addActionListener(e -> {
            try {
                List<Course> courses = API.getCourses();
                new OverzichtCoursesGUI(courses);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Fout bij het ophalen van cursussen: " + ex.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
            }
        });
        bottomRowPanel.add(overviewCoursesButton);

        // sem overzicht roept getSemesters() van API class; slaat op in List; opent OverzichtSemestersGUI
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

    // methods die boeven opgeroepen worden

    // opgeroepen door uitleg button
    public void explain() { // RS
        String uitleg = "Welkom bij de Studenten en Cijfers Administratie applicatie!\n";
        uitleg += "Hier vindt u een overzicht van de beschikbare functionaliteiten.\n\n";
        uitleg += "1. Studenten toevoegen: Klik op \"Student Toevoegen\" om een nieuw student toe te voegen.\n";
        uitleg += "2. Tentamen toevoegen: Klik op \"Tentamen Toevoegen\" om een nieuw tentamen aan te maken.\n";
        uitleg += "3. Cijfers toevoegen: Klik op \"Cijfer Toevoegen\" om een nieuwe cijfer toe te voegen.\n";
        uitleg += "4. Overzichten: Klik op een van de knoppen in de benedenste rij om een overzicht te openen.\n";
        uitleg += "5. Bewerken: Kies een overzicht en klik op \"Bewerken\" om een record te bewerken.\n";
        uitleg += "6. Verwijderen: Kies een overzicht en klik op \"Verwijderen\" om een record te verwijderen.\n";
        // uitleg += "7. Refreshen: Kies een overzicht en klik op \"Bewerken\" om een record te bewerken.\n";
        uitleg += "7. Visualiseer cijfers: Klik op \"Visualiseer cijfers\" om een grafische weergave te zien van de cijfers.\n";
        uitleg += "8. Uitloggen: Klik op \"Uitloggen\" om u uit te loggen.\n";
        uitleg += "9. Sluiten: Klik op \"Sluiten\" om het programma af te sluiten.\n";
        uitleg += "\n\nDank voor het gebruiken van deze applicatie.";
        JOptionPane.showMessageDialog(frame, uitleg, "Uitleg", JOptionPane.INFORMATION_MESSAGE);
    }

    // opgeroepen door leden button
    public void groepsleden() {
        String uitleg = "Ons team bestaat uit:\n";
        uitleg += "- Ramdhiansing Shakeel - SE/1123/067\n";
        uitleg += "- Ramkhelawan Riaaz - SE/1123/070\n";
        uitleg += "- Sangham Rishika - SE/1123/081\n";
        uitleg += "- Sodipo Sherreskly - SE/1123/086\n";
        JOptionPane.showMessageDialog(frame, uitleg, "Uitleg", JOptionPane.INFORMATION_MESSAGE);
    }

    // snellere manier om meerdere similar panels te maken: toprow-, middle- en bottompanel
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0, 5, 5)); // Kleinere ruimte tussen knoppen
        panel.setOpaque(false);
        return panel;
    }

    // voor button styling met hover kleur
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

    // opgeroepen door sluiten button. confirmation message met yes or no; yes: frame close
    private void confirmExit() {
        int response = JOptionPane.showConfirmDialog(frame, "Weet u zeker dat u het programma wilt afsluiten?", "Bevestiging", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            frame.dispose();
        }
    }

    // opgeroepen door logout button; confirmation message met yes or no; yes: frame close; adminDashboard open
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
    } // creates new admin dashboard window
}