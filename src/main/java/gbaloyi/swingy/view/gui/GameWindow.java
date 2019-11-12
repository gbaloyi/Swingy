package gbaloyi.swingy.view.gui;

import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import gbaloyi.swingy.model.hero.Hero;
import gbaloyi.swingy.view.console.Logo;
import gbaloyi.swingy.model.hero.HeroEnum;
import gbaloyi.swingy.controller.MapFactory;
import gbaloyi.swingy.controller.HeroFactory;
import gbaloyi.swingy.database.DatabaseHandler;
import gbaloyi.swingy.view.console.ConsoleView;
import gbaloyi.swingy.controller.GameController;

import static gbaloyi.swingy.StaticGlobal.*;

public class GameWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private JButton backButton = new JButton("BACK");
    private JButton playButton = new JButton("Play");
    private JButton createButton = new JButton("Create");
    private JButton cancelButton = new JButton("Cancel");
    private JButton eastButton = new JButton("EAST \u2192");
    private JButton westButton = new JButton("WEST \u2190");
    private JButton northButton = new JButton("NORTH \u2191");
    private JButton southButton = new JButton("SOUTH \u2193");
    private JButton createHeroButton = new JButton("Create A Hero");
    private JButton selectHeroButton = new JButton("Select A Hero");

    private JLabel movesLabel = new JLabel("Please Select Your Move:");
    private JLabel selectHeroLabel = new JLabel("Please Select A Hero Below:");
    private JLabel heroNameInputLabel = new JLabel("Please Enter Hero Name Below:");
    private JLabel chooseHeroTypeLabel = new JLabel("Please Choose Hero Type Below:");

    private JPanel mapPanel = new JPanel();
    private JPanel gridPanel = new JPanel();
    private JPanel menuPanel = new JPanel();
    private JPanel displayPanel = new JPanel();
    private JPanel containerPanel = new JPanel();
    private JPanel heroInputPanel = new JPanel();
    private JPanel createHeroPanel = new JPanel();
    private JPanel selectHeroPanel = new JPanel();
    private JPanel imageContainerPanel = new JPanel();
    private JPanel buttonContainerPanel = new JPanel();

    private JTextArea logoTextArea = new JTextArea();
    private JTextField inputTextField = new JTextField();
    private JTextArea displayTextArea = new JTextArea();

    private GridLayout gridLayout = new GridLayout();
    private JComboBox<String> createHeroComboList = new JComboBox();
    private JComboBox<String> selectHeroComboList = new JComboBox();

    GameWindow() {
        setTitle("Swingy 1.0");
        setSize(900, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        containerPanel.setLayout(new BorderLayout());
        loadComponents(this);
        initComponents(this);
        this.setVisible(true);
    }

    private void loadComponents(GameWindow gameWindow) {
        menuPanel.add(createHeroButton);
        menuPanel.add(selectHeroButton);
        menuPanel.add(createHeroPanel);
        menuPanel.add(selectHeroPanel);
        menuPanel.add(imageContainerPanel);

        mapPanel.add(gridPanel);
        mapPanel.add(buttonContainerPanel);
        heroInputPanel.add(heroNameInputLabel);
        heroInputPanel.add(inputTextField);
        logoTextArea.append(Logo.logoText);
        displayPanel.add(displayTextArea);

        containerPanel.add(mapPanel, BorderLayout.CENTER);
        containerPanel.add(menuPanel, BorderLayout.WEST);
        containerPanel.add(displayPanel, BorderLayout.SOUTH);
        gameWindow.setContentPane(containerPanel);
    }

    private void initComponents(GameWindow gameWindow) {
        menuPanel.setPreferredSize(new Dimension(gameWindow.getWidth() / 1, gameWindow.getHeight()));
        menuPanel.setBackground(Color.GRAY);
        menuPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        imageContainerPanel.setPreferredSize(new Dimension(600, 400));
        imageContainerPanel.setBackground(Color.LIGHT_GRAY);
        imageContainerPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        logoTextArea.setBackground(Color.LIGHT_GRAY);
        logoTextArea.setLayout(new BoxLayout(logoTextArea, BoxLayout.X_AXIS));
        imageContainerPanel.add(logoTextArea);

        mapPanel.setPreferredSize(new Dimension(gameWindow.getWidth() / 2, gameWindow.getHeight() / 2));
        mapPanel.setBackground(Color.GRAY);
        mapPanel.setVisible(false);

        gridPanel.setPreferredSize(new Dimension(gameWindow.getWidth() / 2, gameWindow.getHeight() / 2));
        gridPanel.setBackground(Color.CYAN);

        displayPanel.setPreferredSize(new Dimension(gameWindow.getWidth(), gameWindow.getHeight() / 4));
        displayPanel.setBackground(Color.LIGHT_GRAY);
        displayPanel.add(displayTextArea);
        displayPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        displayTextArea.setPreferredSize(new Dimension(800, 180));
        displayTextArea.setBackground(Color.WHITE);
        displayTextArea.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        displayTextArea.setEditable(false);

        createHeroPanel.setPreferredSize(new Dimension(600, 250));
        createHeroPanel.setBackground(Color.LIGHT_GRAY);
        createHeroPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        createHeroPanel.setLayout(new BoxLayout(createHeroPanel, BoxLayout.Y_AXIS));
        createHeroPanel.add(chooseHeroTypeLabel);
        createHeroPanel.add(createHeroComboList);
        createHeroPanel.add(heroNameInputLabel);
        createHeroPanel.add(inputTextField);
        createHeroPanel.add(heroInputPanel);
        createHeroPanel.setVisible(false);
        createButton.addActionListener(new CreateButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());
        createHeroComboList.setAlignmentX(Component.CENTER_ALIGNMENT);

        selectHeroPanel.setPreferredSize(new Dimension(660, 250));
        selectHeroPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        selectHeroPanel.setLayout(new BoxLayout(selectHeroPanel, BoxLayout.Y_AXIS));
        selectHeroPanel.add(selectHeroLabel);
        selectHeroPanel.add(selectHeroComboList);
        selectHeroPanel.setVisible(false);
        selectHeroComboList.setAlignmentX(Component.CENTER_ALIGNMENT);

        createHeroComboList.addItem("Crab");
        createHeroComboList.addItem("Cuttlefish");
        createHeroComboList.addItem("Octopus");

        createHeroComboList.setPreferredSize(new Dimension(200, 50));
        selectHeroComboList.setPreferredSize(new Dimension(200, 50));

        createHeroButton.setPreferredSize(new Dimension(200, 50));
        selectHeroButton.setPreferredSize(new Dimension(200, 50));
       
        createHeroButton.addActionListener(new CreateHeroButtonListener());
        selectHeroButton.addActionListener(new SelectHeroButtonListener());
       
        playButton.addActionListener(new PlayButtonListener());
        heroInputPanel.setVisible(false);

        gridPanel.setLayout(gridLayout);
        gridPanel.setVisible(false);
    }

    private class CreateHeroButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            createButton.setPreferredSize(new Dimension(200, 50));
            cancelButton.setPreferredSize(new Dimension(200, 50));

            buttonContainerPanel.add(createButton);
            buttonContainerPanel.add(cancelButton);
            buttonContainerPanel.remove(playButton);
            buttonContainerPanel.setVisible(true);
            buttonContainerPanel.setPreferredSize(new Dimension(100, 100));
            buttonContainerPanel.setBackground(Color.GRAY);

            createHeroPanel.add(buttonContainerPanel);
            selectHeroPanel.remove(heroInputPanel);
            createHeroButton.setVisible(false);
            selectHeroButton.setVisible(false);

            imageContainerPanel.setVisible(false);

            createHeroPanel.setVisible(true);
        }
    }

    private class CreateButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            int index = createHeroComboList.getSelectedIndex();
            Hero hero = null;

            try {
                if (DatabaseHandler.getInstance().heroExists(inputTextField.getText())) {
                    JOptionPane.showMessageDialog(GameWindow.this, "Hero Already Exists!", "Alert", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    switch (index) {
                        case 0:
                            hero = HeroFactory.newHero(inputTextField.getText(), HeroEnum.CRAB);
                            break;
                        case 1:
                            hero = HeroFactory.newHero(inputTextField.getText(), HeroEnum.CUTTLEFISH);
                            break;
                        case 2:
                            hero = HeroFactory.newHero(inputTextField.getText(), HeroEnum.OCTOPUS);
                        default:
                            break;
                    }
                    if (hero != null && !inputTextField.getText().equals("")) {
                        try {
                            DatabaseHandler.getInstance().insertHero(hero);
                        } catch (ClassNotFoundException | SQLException | IOException exception) {
                            exception.printStackTrace();
                        }
                        createHeroPanel.setVisible(false);
                        createHeroButton.setVisible(true);
                        selectHeroButton.setVisible(true);

                        imageContainerPanel.setVisible(true);
                        JOptionPane.showMessageDialog(GameWindow.this, inputTextField.getText() + " Is Created", "Alert", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(GameWindow.this, "Hero name can not be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (ClassNotFoundException | SQLException exception) {
                exception.printStackTrace();
            }   
        }
    }

    private class CancelButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            createHeroPanel.setVisible(false);
            selectHeroPanel.setVisible(false);

            createHeroButton.setVisible(true);
            selectHeroButton.setVisible(true);
            imageContainerPanel.setVisible(true);
        } 
    }

    private class SelectHeroButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            try {
                if (DatabaseHandler.getInstance().numberOfHeroes() > 0) {
                    playButton.setPreferredSize(new Dimension(200, 50));
                    cancelButton.setPreferredSize(new Dimension(200, 50));

                    buttonContainerPanel.add(playButton);
                    buttonContainerPanel.add(cancelButton);
                    buttonContainerPanel.remove(createButton);
                    buttonContainerPanel.setVisible(true);
                    buttonContainerPanel.setPreferredSize(new Dimension(100, 100));
                    buttonContainerPanel.setBackground(Color.GRAY);
    
                    selectHeroPanel.add(buttonContainerPanel);
                    createHeroPanel.remove(cancelButton);
                    createHeroButton.setVisible(false);
                    selectHeroButton.setVisible(false);
                    imageContainerPanel.setVisible(false);

                    selectHeroComboList.removeAllItems();
                    List<Hero> heros = DatabaseHandler.getInstance().retrieveDatabase();
                    for (Hero hero : heros) {
                        selectHeroComboList.addItem(hero.getName());
                    }
                    selectHeroPanel.setVisible(true);
                } else {
                    displayTextArea.setText(">>> No Heroes Available.");
                }
            } catch (IOException | ClassNotFoundException | SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    private class PlayButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            selectHeroPanel.setVisible(false);
            imageContainerPanel.setVisible(false);

            try {
                hero = DatabaseHandler.getInstance().retrieveHeroData(selectHeroComboList.getSelectedItem().toString());
                map = MapFactory.generateMap(hero);
                displayTextArea.setText("::: HERO STATISTICS :::"
                    + "\nName: " + hero.getName()
                    + "\nType: " + hero.getType()
                    + "\nLevel: " + hero.getLevel()
                    + "\nAttack: " + hero.getAttack()
                    + "\nDefense: " + hero.getDefense()
                    + "\nExperience: " + hero.getExperience()
                    + "\nHit Points: " + hero.getHitPoints()
                    + "\nArmor: " + hero.getArmor().getName()
                    + "\nHelm: " + hero.getHelm().getName()
                    + "\nWeapon: " + hero.getWeapon().getName()
                );
            } catch (ClassNotFoundException | SQLException | IOException exception) {
                exception.printStackTrace();
            }
            
            gridLayout.setRows(map.getSize());
            gridLayout.setColumns(map.getSize());
            gridLayout.setHgap(-1);
            gridLayout.setVgap(-1);
            gridPanel.setVisible(true);
        
            mapPanel.setVisible(true);
            menuPanel.setVisible(false);

            northButton.setPreferredSize(new Dimension(100, 50));
            northButton.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            northButton.addActionListener(new NorthButtonListener());

            eastButton.setPreferredSize(new Dimension(100, 50));
            eastButton.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            eastButton.addActionListener(new EastButtonListener());

            southButton.setPreferredSize(new Dimension(100, 50));
            southButton.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            southButton.addActionListener(new SouthButtonListener());

            westButton.setPreferredSize(new Dimension(100, 50));
            westButton.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            westButton.addActionListener(new WestButtonListener());
            
            backButton.setPreferredSize(new Dimension(100, 50));
            backButton.addActionListener(new BackButtonListner());

            buttonContainerPanel.removeAll();
            buttonContainerPanel.add(movesLabel);
            buttonContainerPanel.add(northButton);
            buttonContainerPanel.add(eastButton);
            buttonContainerPanel.add(southButton);
            buttonContainerPanel.add(westButton);
            buttonContainerPanel.add(backButton);
            buttonContainerPanel.setPreferredSize(new Dimension(200, 400));
            buttonContainerPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            buttonContainerPanel.setBackground(Color.GRAY);
            buttonContainerPanel.setVisible(true);
            mapPanel.add(buttonContainerPanel);
           
        }
    }

    private class SwitchToConsoleButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            GameWindow.this.dispose();
            ConsoleView.run();
        }
    }

    private class NorthButtonListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            GameController.moveHero(1);
            if (GOAL_REACHED == false) {
                GameController.goal();   
            }
            if (GOAL_REACHED == true) {
                String message = "CONGRATULATIONS, You Reached Your Goal!!!";
                String title = "Alert";
                int messageType = JOptionPane.INFORMATION_MESSAGE;

                JOptionPane.showMessageDialog(GameWindow.this, message, title, messageType);
                GameController.goal();
                GOAL_REACHED = false;
            }

            gridPanel.removeAll();
            gridLayout.setRows(map.getSize());
            gridLayout.setColumns(map.getSize());
            gridLayout.setHgap(-1);
            gridLayout.setVgap(-1);
            gridPanel.setLayout(gridLayout);
            // guiMapView();
            gridPanel.revalidate();
            gridPanel.repaint();
        }    
    }
    private class EastButtonListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            GameController.moveHero(2);
            if (GOAL_REACHED == false) {
                GameController.goal();   
            }
            if (GOAL_REACHED == true) {
                String message = "CONGRATULATIONS, You Reached Your Goal!!!";
                String title = "Alert";
                int messageType = JOptionPane.INFORMATION_MESSAGE;

                JOptionPane.showMessageDialog(GameWindow.this, message, title, messageType);
                GOAL_REACHED = false;
            }

            gridPanel.removeAll();
            gridLayout.setRows(map.getSize());
            gridLayout.setColumns(map.getSize());
            gridLayout.setHgap(-1);
            gridLayout.setVgap(-1);
            gridPanel.setLayout(gridLayout);
            // guiMapView();
            gridPanel.revalidate();
            gridPanel.repaint();
        }
    }
    private class SouthButtonListener implements ActionListener{

        public void actionPerformed(ActionEvent actionEvent) {
            GameController.moveHero(3);
            if (GOAL_REACHED == false) {
                GameController.goal();   
            }
            if (GOAL_REACHED == true) {
                String message = "CONGRATULATIONS, You Reached Your Goal!!!";
                String title = "Alert";
                int messageType = JOptionPane.INFORMATION_MESSAGE;

                JOptionPane.showMessageDialog(GameWindow.this, message, title, messageType);
                GOAL_REACHED = false;
            }

            gridPanel.removeAll();
            gridLayout.setRows(map.getSize());
            gridLayout.setColumns(map.getSize());
            gridLayout.setHgap(-1);
            gridLayout.setVgap(-1);
            gridPanel.setLayout(gridLayout);
         
            gridPanel.revalidate();
            gridPanel.repaint();
        }
    }

    private class WestButtonListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            GameController.moveHero(4);
            if (GOAL_REACHED == false) {
                GameController.goal();   
            }
            if (GOAL_REACHED == true) {
                String message = "CONGRATULATIONS, You Reached Your Goal!!!";
                String title = "Alert";
                int messageType = JOptionPane.INFORMATION_MESSAGE;

                JOptionPane.showMessageDialog(GameWindow.this, message, title, messageType);
                GOAL_REACHED = false;
            }

            gridPanel.removeAll();
            gridLayout.setRows(map.getSize());
            gridLayout.setColumns(map.getSize());
            gridLayout.setHgap(-1);
            gridLayout.setVgap(-1);
            gridPanel.setLayout(gridLayout);
            // guiMapView();
            gridPanel.revalidate();
            gridPanel.repaint();
        }
    }

    private class BackButtonListner implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            GameWindow.run();
        }
        
    }

    public static void run() {
        new GameWindow();
    }
    
}