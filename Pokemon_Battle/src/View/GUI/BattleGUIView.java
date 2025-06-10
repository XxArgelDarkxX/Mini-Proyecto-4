/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import Model.Trainer;
import Model.BattleHistory;
import Model.Pokemon;
import javax.swing.JSeparator;


/**
 *
 * @author crisc
 */
public class BattleGUIView extends JFrame {

    public Trainer[] trainers;
    public String[] pokemonsBattle;
    public byte moveTrainer1 = -1;
    public byte moveTrainer2 = -1;
    public byte x = 0;
    public byte y = 0;

    private BattleHistory battleHistory;
    class PokemonHealthBar extends JPanel {
        private float currentHP;
        private final float maxHP;
        private float targetHP;
        private JLabel hpLabel;
        private Timer animationTimer;
        private static final int ANIMATION_DELAY = 50;
    
        public PokemonHealthBar(float maxHP, JLabel hpLabel) {
            this.maxHP = maxHP;
            this.currentHP = maxHP;
            this.targetHP = maxHP;
            this.hpLabel = hpLabel;
            setPreferredSize(new Dimension(150, 8));
            setOpaque(false);
    
            // Configurar el timer para la animación
            animationTimer = new Timer(ANIMATION_DELAY, e -> {
                if (currentHP > targetHP) {
                    currentHP = Math.max(targetHP, currentHP - 1);
                    updateHPLabel();
                    repaint();
                    if (currentHP == targetHP) {
                        animationTimer.stop();
                    }
                }
            });
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
            int width = getWidth();
            int height = getHeight();
            int arc = height; // Para hacer los bordes completamente redondeados
    
            // Fondo negro
            g2d.setColor(new Color(48, 48, 48));
            g2d.fillRoundRect(0, 0, width, height, arc, arc);
    
            // Barra de vida
            float hpPercentage = currentHP / maxHP;
            int barWidth = (int) (width * hpPercentage);
    
            // Color basado en el porcentaje de vida
            Color healthColor;
            if (hpPercentage > 0.5f) {
                healthColor = new Color(0, 192, 0); // Verde
            } else if (hpPercentage > 0.25f) {
                healthColor = new Color(255, 192, 0); // Amarillo
            } else {
                healthColor = new Color(192, 0, 0); // Rojo
            }
    
            g2d.setColor(healthColor);
            g2d.fillRoundRect(0, 0, barWidth, height, arc, arc);
    
            // Borde para mejor visibilidad
            g2d.setColor(new Color(32, 32, 32));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);
    
            g2d.dispose();
        }
    
        private void updateHPLabel() {
            hpLabel.setText(String.valueOf((int)currentHP));
        }
    
        public void setHP(float newHP) {
            this.targetHP = Math.max(0, Math.min(newHP, maxHP));
            if (!animationTimer.isRunning()) {
                animationTimer.start();
            }
        }
    }
    /**
     * Creates new form Battle
     */
    BackgroundPanel background = new BackgroundPanel();
    public BattleGUIView() {

        initComponents();
        loadPokemonImage();
        loadPokemonImage2();
    }

    public BattleGUIView(Trainer[] trainers, String[] pokemonsBattle) {
        this.pokemonsBattle = pokemonsBattle;
        this.trainers = trainers;
        this.battleHistory = new BattleHistory(trainers[0].getName(), trainers[1].getName());
        battleHistory.logBattleStart(); // Registrar inicio de batalla
        initComponents();
        initializePokemonSelection();
        loadPokemonImage();
        loadPokemonImage2();
        
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel2 = new JPanel();
        jPanel1 = new BackgroundPanel();
        imageUrl = new JLabel();
        ImageUrl2 = new JLabel();
        attack1_2 = new JToggleButton();
        attack1_1 = new JToggleButton();
        attack1_3 = new JToggleButton();
        attack1_4 = new JToggleButton();
        maxHpLabel1 = new JLabel();
        maxHpLabel2 = new JLabel();
        hpLabel1 = new JLabel();
        slashLabel1 = new JLabel("/");
        hpLabel2 = new JLabel();
        slashLabel2 = new JLabel("/");
        hpPanel1 = new JPanel();
        hpPanel2 = new JPanel();
        pokemon1 = new JLabel();
        trainer1 = new JLabel();
        trainer2 = new JLabel();
        pokemon2 = new JLabel();
        attack2_1 = new JToggleButton();
        attack2_2 = new JToggleButton();
        attack2_3 = new JToggleButton();
        attack2_4 = new JToggleButton();
        healthBar1 = new PokemonHealthBar(
            trainers[0].getPokemonTeam().get(x).getHp(), 
            hpLabel1
        );
        healthBar2 = new PokemonHealthBar(
            trainers[1].getPokemonTeam().get(y).getHp(), 
            hpLabel2
        );

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        
        jPanel2.setBackground(new Color(228, 228, 228));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        imageUrl.setText("");
        
        ImageUrl2.setText("");

        hpPanel1.setLayout(null);
        hpPanel1.setBackground(new Color(255, 255, 255));
        hpPanel1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 7, true));
        hpPanel1.setBounds(510, 270, 130, 80);

        pokemon1.setBounds(10, 10, 100, 20);
        slashLabel1.setBounds(80, 35, 10, 20);
        hpLabel1.setBounds(50, 35, 40, 20);
        maxHpLabel1.setBounds(90, 35, 40, 20);
        healthBar1.setBounds(10, 53, 110, 11);

        hpPanel1.add(pokemon1);
        hpPanel1.add(hpLabel1);
        hpPanel1.add(maxHpLabel1);
        hpPanel1.add(healthBar1);
        hpPanel1.add(slashLabel1);

        hpPanel2.setLayout(null);
        hpPanel2.setBackground(new Color(255, 255, 255));
        hpPanel2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 7, true));
        hpPanel2.setBounds(40, 30, 130, 80);

        pokemon2.setBounds(10, 10, 100, 20);
        hpLabel2.setBounds(50, 35, 40, 20);
        slashLabel2.setBounds(80, 35, 10, 20);
        maxHpLabel2.setBounds(90, 35, 40, 20);
        healthBar2.setBounds(10, 53, 110, 11);

        hpPanel2.add(pokemon2);
        hpPanel2.add(hpLabel2);
        hpPanel2.add(maxHpLabel2);
        hpPanel2.add(healthBar2);
        hpPanel2.add(slashLabel2);

        jPanel2.add(hpPanel1);
        jPanel2.add(hpPanel2);

        createStylizedBorder(10, 369, 652, 5, true, false, false, false);  // Borde superior
        createStylizedBorder(10, 369, 5, 369, false, false, false, false); // Borde izquierdo
        createStylizedBorder(10, 539, 652, 5, true, false, true, false);  // Borde inferior
        createStylizedBorder(662, 369, 5, 369, false, false, false, true); // Borde derecho
        createStylizedBorder(335, 370, 5, 186, false, true, false, false); // Separador central

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(67, 67, 67)
            .addComponent(imageUrl, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
            .addGap(37, 37, 37)
            .addComponent(ImageUrl2, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(71, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(124, Short.MAX_VALUE)
                .addComponent(imageUrl, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(ImageUrl2, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                for(byte i = 0; i < trainers[0].getPokemonTeam().size(); i++){
                    if(pokemonsBattle[0].equals(trainers[0].getPokemonTeam().get(i).getPokedexId())){
                        x = i;
                        break;
                    }
                }
                for(byte i = 0; i < trainers[1].getPokemonTeam().size(); i++){
                    if(pokemonsBattle[1].equals(trainers[1].getPokemonTeam().get(i).getPokedexId())){
                        y = i;
                        break;
                    }
                }
                
                attack1_2.setBackground(new Color(255, 255, 255));
                attack1_2.setFont(new Font("Roboto", 2, 15)); // NOI18N
                attack1_2.setText(trainers[0].getPokemonTeam().get(x).getMoves().get(1).getName());
                attack1_2.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent evt) {
                        attack1_2MouseEntered(evt);
                    }
                    public void mouseExited(MouseEvent evt) {
                        attack1_2MouseExited(evt);
                    }
        });
        attack1_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                attack1_2ActionPerformed(evt);
            }
        });

        attack1_1.setBackground(new Color(255, 255, 255));
        attack1_1.setFont(new Font("Roboto", 2, 15)); // NOI18N
        attack1_1.setText(trainers[0].getPokemonTeam().get(x).getMoves().get(0).getName());
        attack1_1.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                attack1_1MouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                attack1_1MouseExited(evt);
            }
        });

        attack1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                attack1_1ActionPerformed(evt);
            }
        });
        
        attack1_3.setBackground(new Color(255, 255, 255));
        attack1_3.setFont(new Font("Roboto", 2, 15)); // NOI18N
        attack1_3.setText(trainers[0].getPokemonTeam().get(x).getMoves().get(2).getName());
        attack1_3.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                attack1_3MouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                attack1_3MouseExited(evt);
            }
        });

        attack1_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                attack1_3ActionPerformed(evt);
            }
        });
        
        attack1_4.setBackground(new Color(255, 255, 255));
        attack1_4.setFont(new Font("Roboto", 2, 15)); // NOI18N
        attack1_4.setText(trainers[0].getPokemonTeam().get(x).getMoves().get(3).getName());
        attack1_4.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                attack1_4MouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                attack1_4MouseExited(evt);
            }
        });

        attack1_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                attack1_4ActionPerformed(evt);
            }
        });
        
        hpLabel1.setFont(new Font("Roboto Black", 3, 12)); // NOI18N
        hpLabel1.setText(String.valueOf(trainers[0].getPokemonTeam().get(x).getHp()));

        slashLabel1.setFont(new Font("Roboto Black", 3, 12)); // NOI18N

        maxHpLabel1.setFont(new Font("Roboto Black", 3, 12)); // NOI18N
        maxHpLabel1.setText(String.valueOf(trainers[0].getPokemonTeam().get(x).getHp()));
        
        pokemon1.setFont(new Font("Roboto Black", 3, 12)); // NOI18N
        pokemon1.setText(trainers[0].getPokemonTeam().get(x).getName());
        
        trainer1.setFont(new Font("Roboto Black", 1, 12)); // NOI18N
        trainer1.setText(trainers[0].getName());
        
        trainer2.setFont(new Font("Roboto Black", 1, 12)); // NOI18N
        trainer2.setText(trainers[1].getName());
        
        pokemon2.setFont(new Font("Roboto Black", 3, 12)); // NOI18N
        pokemon2.setText(trainers[1].getPokemonTeam().get(y).getName());
        
        hpLabel2.setFont(new Font("Roboto Black", 3, 12)); // NOI18N
        hpLabel2.setText(String.valueOf(trainers[1].getPokemonTeam().get(y).getHp()));

        slashLabel2.setFont(new Font("Roboto Black", 3, 12)); // NOI18N

        maxHpLabel2.setFont(new Font("Roboto Black", 3, 12)); // NOI18N
        maxHpLabel2.setText(String.valueOf(trainers[1].getPokemonTeam().get(y).getHp()));
        
        attack2_1.setBackground(new Color(255, 255, 255));
        attack2_1.setFont(new Font("Roboto", 2, 15)); // NOI18N
        attack2_1.setText(trainers[1].getPokemonTeam().get(y).getMoves().get(0).getName());
        attack2_1.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                attack2_1MouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                attack2_1MouseExited(evt);
            }
        });

        attack2_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                attack2_1ActionPerformed(evt);
            }
        });
        attack2_2.setBackground(new Color(255, 255, 255));
        attack2_2.setFont(new Font("Roboto", 2, 15)); // NOI18N
        attack2_2.setText(trainers[1].getPokemonTeam().get(y).getMoves().get(1).getName());
        attack2_2.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                attack2_2MouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                attack2_2MouseExited(evt);
            }
        });
        attack2_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                attack2_2ActionPerformed(evt);
            }
        });

        attack2_3.setBackground(new Color(255, 255, 255));
        attack2_3.setFont(new Font("Roboto", 2, 15)); // NOI18N
        attack2_3.setText(trainers[1].getPokemonTeam().get(y).getMoves().get(2).getName());
        attack2_3.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                attack2_3MouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                attack2_3MouseExited(evt);
            }
        });

        attack2_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                attack2_3ActionPerformed(evt);
            }
        });

        attack2_4.setBackground(new Color(255, 255, 255));
        attack2_4.setFont(new Font("Roboto", 2, 15)); // NOI18N
        attack2_4.setText(trainers[1].getPokemonTeam().get(y).getMoves().get(3).getName());
        attack2_4.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                attack2_4MouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                attack2_4MouseExited(evt);
            }
        });

        attack2_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                attack2_4ActionPerformed(evt);
            }
        });


        
        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(trainer1)  // Añadido trainer1 aquí
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(attack1_3, 120, 120, 120)
                            .addComponent(attack1_1, 120, 120, 120))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(attack1_2, 120, 120, 120)
                            .addComponent(attack1_4, 120, 120, 120))))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(trainer2)  // Añadido trainer2 aquí
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(attack2_1, 120, 120, 120)
                            .addComponent(attack2_3, 120, 120, 120))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(attack2_4, 120, 120, 120)
                            .addComponent(attack2_2, 120, 120, 120))))
                .addGap(34, 34, 34))
        );

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(trainer1)
                    .addComponent(trainer2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(attack1_1)
                            .addComponent(attack1_2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(attack1_3)
                            .addComponent(attack1_4)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(attack2_1)
                            .addComponent(attack2_2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(attack2_3)
                            .addComponent(attack2_4))))
                .addContainerGap())
        );
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                
                pack();
    }// </editor-fold>
    private void initializePokemonSelection() {
        pokemonSelectionPanel1 = new JPanel();
        pokemonSelectionPanel2 = new JPanel();
        victoryPanel = new JPanel();
        victoryLabel = new JLabel();
        pokemonButtons1 = new JButton[3];
        pokemonButtons2 = new JButton[3];

        // Configurar los paneles
        pokemonSelectionPanel1.setBackground(new Color(132, 224, 223));
        pokemonSelectionPanel2.setBackground(new Color(132, 224, 223));
        victoryPanel.setBackground(new Color(228, 228, 228));

        // Configurar el panel de victoria
        victoryLabel.setFont(new Font("Roboto Black", Font.BOLD, 30));
        victoryLabel.setForeground(new Color(125, 129, 174));
        victoryPanel.add(victoryLabel);

        // Establecer bounds
        pokemonSelectionPanel1.setBounds(18, 378, 316, 157);
        pokemonSelectionPanel2.setBounds(343, 378, 316, 157);
        victoryPanel.setBounds(0, 300, 632, 157);

        // Ahora agregar los paneles al contenedor principal
        jPanel2.add(pokemonSelectionPanel1);
        jPanel2.add(pokemonSelectionPanel2);
        jPanel2.add(victoryPanel);

        // Crear y configurar botones para el entrenador 1
        for(int i = 0; i < 3; i++) {
            pokemonButtons1[i] = new JButton();
            pokemonButtons1[i].setPreferredSize(new Dimension(120, 50));
            final int index = i;
            
            loadPokemonIcon(pokemonButtons1[i], trainers[0].getPokemonTeam().get(i));
            pokemonButtons1[i].setText("<html>" + trainers[0].getPokemonTeam().get(i).getName() + 
                                    "<br>HP: " + trainers[0].getPokemonTeam().get(i).getHp() + "</html>");
            
            pokemonButtons1[i].addActionListener(e -> selectPokemon(0, index));
            pokemonSelectionPanel1.add(pokemonButtons1[i]);
        }

        // Crear y configurar botones para el entrenador 2
        for(int i = 0; i < 3; i++) {
            pokemonButtons2[i] = new JButton();
            pokemonButtons2[i].setPreferredSize(new Dimension(120, 50));
            final int index = i;
            
            loadPokemonIcon(pokemonButtons2[i], trainers[1].getPokemonTeam().get(i));
            pokemonButtons2[i].setText("<html>" + trainers[1].getPokemonTeam().get(i).getName() + 
                                    "<br>HP: " + trainers[1].getPokemonTeam().get(i).getHp() + "</html>");
            
            pokemonButtons2[i].addActionListener(e -> selectPokemon(1, index));
            pokemonSelectionPanel2.add(pokemonButtons2[i]);
        }

        // Ocultar paneles inicialmente
        pokemonSelectionPanel1.setVisible(false);
        pokemonSelectionPanel2.setVisible(false);
        victoryPanel.setVisible(false);
    }

    private void createStylizedBorder(int x, int y, int width, int height, boolean isHorizontal, boolean isCentralSeparator, boolean isBottom, boolean isRight) {
        // Capa base (más oscura)
        JSeparator baseBorder = new JSeparator(isHorizontal ? JSeparator.HORIZONTAL : JSeparator.VERTICAL);
        baseBorder.setBackground(new Color(63, 63, 63));
        baseBorder.setForeground(new Color(63, 63, 63));
        baseBorder.setOpaque(true);
        baseBorder.setBounds(x, y, width, height);
        
        // Capa media (tono medio)
        JSeparator middleBorder = new JSeparator(isHorizontal ? JSeparator.HORIZONTAL : JSeparator.VERTICAL);
        middleBorder.setBackground(new Color(125, 129, 174));
        middleBorder.setForeground(new Color(125, 129, 174));
        middleBorder.setOpaque(true);
        if (isHorizontal) {
            if (isBottom) {
                middleBorder.setBounds(x, y - 1, width, height);
            } else {
                middleBorder.setBounds(x, y + 1, width, height);
            }
        } else {
            if (isRight) {
                middleBorder.setBounds(x - 1, y, width, height);
            } else {
                middleBorder.setBounds(x + 1, y, width, height);
            }
        }
        
        // Capa superior (más clara)
        JSeparator lightBorder = new JSeparator(isHorizontal ? JSeparator.HORIZONTAL : JSeparator.VERTICAL);
        lightBorder.setBackground(new Color(230, 230, 230));
        lightBorder.setForeground(new Color(230, 230, 230));
        lightBorder.setOpaque(true);
        if (isHorizontal) {
            if (isBottom) {
                middleBorder.setBounds(x, y - 3, width, height);
            } else {
                middleBorder.setBounds(x, y + 3, width, height);
            }
        } else {
            if (isRight) {
                middleBorder.setBounds(x - 3, y, width, height);
            } else {
                middleBorder.setBounds(x + 3, y, width, height);
            }
        }
        if (isCentralSeparator) {
            centralBaseBorder = baseBorder;
            centralMiddleBorder = middleBorder;
            centralLightBorder = lightBorder;
        }
        jPanel2.add(baseBorder);
        jPanel2.add(middleBorder);
        jPanel2.add(lightBorder);
    }
    
    // Método para cargar el icono del Pokémon
    private void loadPokemonIcon(JButton button, Pokemon pokemon) {
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                @SuppressWarnings("deprecation")
                URL url = new URL(pokemon.getIcon());
                Image image = ImageIO.read(url);
                return new ImageIcon(image);
            }
    
            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(scaledImage));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    // Método para seleccionar un nuevo Pokémon
    private void selectPokemon(int trainer, int pokemonIndex) {
        if (trainer == 0) {
            String oldPokemon = trainers[0].getPokemonTeam().get(x).getName();
            x = (byte)pokemonIndex;
            String newPokemon = trainers[0].getPokemonTeam().get(x).getName();
            battleHistory.logPokemonChange(trainer, oldPokemon, newPokemon);
            pokemonSelectionPanel1.setVisible(false);
            if (trainers[0].getPokemonTeam().get(x).getHp() > 0) {
                attack1_1.setVisible(true);
                attack1_2.setVisible(true);
                attack1_3.setVisible(true);
                attack1_4.setVisible(true);
                pokemonsBattle[0] = trainers[0].getPokemonTeam().get(x).getPokedexId();
                updatePokemonInfo();
                hpPanel1.remove(healthBar1);
                healthBar1 = new PokemonHealthBar(
                    trainers[0].getPokemonTeam().get(x).getHp(),
                    hpLabel1
                );
                healthBar1.setBounds(10, 53, 110, 11);
                hpPanel1.add(healthBar1);
                hpPanel1.revalidate();
                hpPanel1.repaint();
                loadPokemonImage();
                enableTrainer1Attacks();
            }
        } else {
            String oldPokemon = trainers[1].getPokemonTeam().get(y).getName();
            y = (byte)pokemonIndex;
            String newPokemon = trainers[1].getPokemonTeam().get(y).getName();
            battleHistory.logPokemonChange(trainer, oldPokemon, newPokemon);
            pokemonSelectionPanel2.setVisible(false);
            if (trainers[1].getPokemonTeam().get(y).getHp() > 0) {
                attack2_1.setVisible(true);
                attack2_2.setVisible(true);
                attack2_3.setVisible(true);
                attack2_4.setVisible(true);
                pokemonsBattle[1] = trainers[1].getPokemonTeam().get(y).getPokedexId();
                updatePokemonInfo();
                hpPanel2.remove(healthBar2);
                healthBar2 = new PokemonHealthBar(
                    trainers[1].getPokemonTeam().get(y).getHp(),
                    hpLabel2
                );
                healthBar2.setBounds(10, 53, 110, 11);
                hpPanel2.add(healthBar2);
                hpPanel2.revalidate();
                hpPanel2.repaint();
                loadPokemonImage2();
                enableTrainer2Attacks();
            }
        }
    }
    
    // Método para actualizar la información de los Pokémon
    private void updatePokemonInfo() {
        pokemon1.setText(trainers[0].getPokemonTeam().get(x).getName());
        pokemon2.setText(trainers[1].getPokemonTeam().get(y).getName());
        hpLabel1.setText(String.valueOf(trainers[0].getPokemonTeam().get(x).getHp()));
        hpLabel2.setText(String.valueOf(trainers[1].getPokemonTeam().get(y).getHp()));
        maxHpLabel1.setText(String.valueOf(trainers[0].getPokemonTeam().get(x).getHp()));
        maxHpLabel2.setText(String.valueOf(trainers[1].getPokemonTeam().get(y).getHp()));
        
        // Actualizar textos de los botones de ataque
        attack1_1.setText(trainers[0].getPokemonTeam().get(x).getMoves().get(0).getName());
        attack1_2.setText(trainers[0].getPokemonTeam().get(x).getMoves().get(1).getName());
        attack1_3.setText(trainers[0].getPokemonTeam().get(x).getMoves().get(2).getName());
        attack1_4.setText(trainers[0].getPokemonTeam().get(x).getMoves().get(3).getName());
        
        attack2_1.setText(trainers[1].getPokemonTeam().get(y).getMoves().get(0).getName());
        attack2_2.setText(trainers[1].getPokemonTeam().get(y).getMoves().get(1).getName());
        attack2_3.setText(trainers[1].getPokemonTeam().get(y).getMoves().get(2).getName());
        attack2_4.setText(trainers[1].getPokemonTeam().get(y).getMoves().get(3).getName());
    }

    private void hideEverything() {
        pokemonSelectionPanel1.setVisible(false);
        pokemonSelectionPanel2.setVisible(false);
        attack1_1.setVisible(false);
        attack1_2.setVisible(false);
        attack1_3.setVisible(false);
        attack1_4.setVisible(false);
        attack2_1.setVisible(false);
        attack2_2.setVisible(false);
        attack2_3.setVisible(false);
        attack2_4.setVisible(false);
        hpLabel1.setVisible(false);
        hpLabel2.setVisible(false);
        centralBaseBorder.setVisible(false);
        centralMiddleBorder.setVisible(false);
        centralLightBorder.setVisible(false);
        trainer1.setVisible(false);
        trainer2.setVisible(false);
        pokemon1.setVisible(false);
        pokemon2.setVisible(false);
        victoryPanel.setVisible(false);
    }
    
    // Método para verificar el estado de la batalla
    private void checkBattleState() {
        boolean trainer1HasAlivePokemon = false;
        boolean trainer2HasAlivePokemon = false;
        
        // Verificar Pokémon vivos del entrenador 1
        for (Pokemon pokemon : trainers[0].getPokemonTeam()) {
            if (pokemon.getHp() > 0) {
                trainer1HasAlivePokemon = true;
                break;
            }
        }
        
        // Verificar Pokémon vivos del entrenador 2
        for (Pokemon pokemon : trainers[1].getPokemonTeam()) {
            if (pokemon.getHp() > 0) {
                trainer2HasAlivePokemon = true;
                break;
            }
        }
        
        // Mostrar panel de victoria si algún entrenador perdió
        if (!trainer1HasAlivePokemon || !trainer2HasAlivePokemon) {
            hideEverything();
            victoryPanel.setVisible(true);
            victoryPanel.setBounds(0, 410, jPanel2.getWidth(), jPanel2.getHeight());
            victoryLabel.setText("<html>" + (!trainer1HasAlivePokemon ? trainers[1].getName() : trainers[0].getName()) 
                            + " ha ganado la batalla. <br><center>¡Felicidades!</center></html>");
        }
        
        // Actualizar botones de selección de Pokémon
        updatePokemonButtons();
    }
    
    // Método para actualizar los botones de selección de Pokémon
    private void updatePokemonButtons() {
        for (int i = 0; i < 3; i++) {
            Pokemon pokemon1 = trainers[0].getPokemonTeam().get(i);
            Pokemon pokemon2 = trainers[1].getPokemonTeam().get(i);
            
            pokemonButtons1[i].setText("<html>" + pokemon1.getName() + "<br>HP: " + pokemon1.getHp() + "</html>");
            pokemonButtons2[i].setText("<html>" + pokemon2.getName() + "<br>HP: " + pokemon2.getHp() + "</html>");
            
            pokemonButtons1[i].setEnabled(pokemon1.getHp() > 0);
            pokemonButtons2[i].setEnabled(pokemon2.getHp() > 0);
        }
    }
            
    private void disableTrainer1Attacks() {
        attack1_1.setEnabled(false);
        attack1_2.setEnabled(false);
        attack1_3.setEnabled(false);
        attack1_4.setEnabled(false);
    }
    
    private void enableTrainer1Attacks() {
        attack1_1.setEnabled(true);
        attack1_2.setEnabled(true);
        attack1_3.setEnabled(true);
        attack1_4.setEnabled(true);
    }
    
    private void disableTrainer2Attacks() {
        attack2_1.setEnabled(false);
        attack2_2.setEnabled(false);
        attack2_3.setEnabled(false);
        attack2_4.setEnabled(false);
    }
    
    private void enableTrainer2Attacks() {
        attack2_1.setEnabled(true);
        attack2_2.setEnabled(true);
        attack2_3.setEnabled(true);
        attack2_4.setEnabled(true);
    }

    private void deselectAllButtons() {
        // Deseleccionar botones del trainer 1
        attack1_1.setSelected(false);
        attack1_2.setSelected(false);
        attack1_3.setSelected(false);
        attack1_4.setSelected(false);
        
        // Deseleccionar botones del trainer 2
        attack2_1.setSelected(false);
        attack2_2.setSelected(false);
        attack2_3.setSelected(false);
        attack2_4.setSelected(false);
        
        // Restaurar color original
        attack1_1.setBackground(Color.white);
        attack1_2.setBackground(Color.white);
        attack1_3.setBackground(Color.white);
        attack1_4.setBackground(Color.white);
        attack2_1.setBackground(Color.white);
        attack2_2.setBackground(Color.white);
        attack2_3.setBackground(Color.white);
        attack2_4.setBackground(Color.white);
    }

    private void attack2_4ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        if (attack1_1.isEnabled() || attack1_2.isEnabled() || attack1_3.isEnabled() || attack1_4.isEnabled()) {
            moveTrainer2 = 3;
            disableTrainer2Attacks();
        } else {
            moveTrainer2 = 3;
            disableTrainer2Attacks();
            attackBySpeed();
        }
    }

    private void attack2_4MouseExited(MouseEvent evt) {
        // TODO add your handling code here:
        attack2_4.setBackground(Color.white);
    }

    private void attack2_4MouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
        attack2_4.setBackground(new Color (7, 155, 219));
    }

    private void attack2_3ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        if (attack1_1.isEnabled() || attack1_2.isEnabled() || attack1_3.isEnabled() || attack1_4.isEnabled()) {
            moveTrainer2 = 2;
            disableTrainer2Attacks();
        } else {
            moveTrainer2 = 2;
            disableTrainer2Attacks();
            attackBySpeed();
        }
    }

    private void attack2_3MouseExited(MouseEvent evt) {
        // TODO add your handling code here:
        attack2_3.setBackground(Color.white);
    }

    private void attack2_3MouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
        attack2_3.setBackground(new Color (7, 155, 219));
    }

    private void attack2_2ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        if (attack1_1.isEnabled() || attack1_2.isEnabled() || attack1_3.isEnabled() || attack1_4.isEnabled()) {
            moveTrainer2 = 1;
            disableTrainer2Attacks();
        } else {
            moveTrainer2 = 1;
            disableTrainer2Attacks();
            attackBySpeed();
        }
    }

    private void attack2_2MouseExited(MouseEvent evt) {
        // TODO add your handling code here:
        attack2_2.setBackground(Color.white);
    }

    private void attack2_2MouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
        attack2_2.setBackground(new Color (7, 155, 219));
    }

    private void attack2_1ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        if (attack1_1.isEnabled() || attack1_2.isEnabled() || attack1_3.isEnabled() || attack1_4.isEnabled()) {
            moveTrainer2 = 0;
            disableTrainer2Attacks();
        } else {
            moveTrainer2 = 0;
            disableTrainer2Attacks();
            attackBySpeed();
        }
    }

    private void attack2_1MouseExited(MouseEvent evt) {
        // TODO add your handling code here:
        attack2_1.setBackground(Color.white);
    }

    private void attack2_1MouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
        attack2_1.setBackground(new Color (7, 155, 219));
    }

    private void attack1_4ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        if (attack2_1.isEnabled() || attack2_2.isEnabled() || attack2_3.isEnabled() || attack2_4.isEnabled()) {
            moveTrainer1 = 3;
            disableTrainer1Attacks();
        } else {
            moveTrainer1 = 3;
            disableTrainer1Attacks();
            attackBySpeed();
        }
    }

    private void attack1_4MouseExited(MouseEvent evt) {
        // TODO add your handling code here:
        attack1_4.setBackground(Color.white);
    }

    private void attack1_4MouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
        attack1_4.setBackground(new Color (7, 155, 219));
    }

    private void attack1_3ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        if (attack2_1.isEnabled() || attack2_2.isEnabled() || attack2_3.isEnabled() || attack2_4.isEnabled()) {
            moveTrainer1 = 2;
            disableTrainer1Attacks();
        } else {
            moveTrainer1 = 2;
            disableTrainer1Attacks();
            attackBySpeed();
        }
    }

    private void attack1_3MouseExited(MouseEvent evt) {
        // TODO add your handling code here:
        attack1_3.setBackground(Color.white);
    }

    private void attack1_3MouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
        attack1_3.setBackground(new Color (7, 155, 219));
    }

    private void attack1_1ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        if (attack2_1.isEnabled() || attack2_2.isEnabled() || attack2_3.isEnabled() || attack2_4.isEnabled()) {
            moveTrainer1 = 0;
            disableTrainer1Attacks();
        } else {
            moveTrainer1 = 0;
            disableTrainer1Attacks();
            attackBySpeed();
        }
    }

    private void attack1_1MouseExited(MouseEvent evt) {
        // TODO add your handling code here:
        attack1_1.setBackground(Color.white);
    }

    private void attack1_1MouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
        attack1_1.setBackground(new Color (7, 155, 219));
    }

    private void attack1_2ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        if (attack2_1.isEnabled() || attack2_2.isEnabled() || attack2_3.isEnabled() || attack2_4.isEnabled()) {
            moveTrainer1 = 1;
            disableTrainer1Attacks();
        } else {
            moveTrainer1 = 1;
            disableTrainer1Attacks();
            attackBySpeed();
        }
    }

    private void attack1_2MouseExited(MouseEvent evt) {
        // TODO add your handling code here:
        attack1_2.setBackground(Color.white);
    }

    private void attack1_2MouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
        attack1_2.setBackground(new Color (7, 155, 219));
    }

    private void loadPokemonImage() {
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                if (trainers[0].getPokemonTeam().get(x) == null) {
                    return null;
                }else {
                    String urlImagen = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/"+pokemonsBattle[0]+ ".png";
                    Image imagen = ImageIO.read(new URL(urlImagen));
                    return new ImageIcon(imagen);
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    // Escalar al tamaño del JLabel
                    Image imagenEscalada = icon.getImage().getScaledInstance(
                            imageUrl.getWidth(),
                            imageUrl.getHeight(),
                            Image.SCALE_SMOOTH);
                    imageUrl.setIcon(new ImageIcon(imagenEscalada));
                    imageUrl.setText("");
                } catch (Exception e) {
                    imageUrl.setText("Error al cargar");
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void loadPokemonImage2() {
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                if (trainers[1].getPokemonTeam().get(y) == null) {
                    return null;
                }else {
                    String urlImagen = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemonsBattle[1]+ ".png";
                    Image imagen = ImageIO.read(new URL(urlImagen));
                    return new ImageIcon(imagen);
                }
                
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    Image imagenEscalada = icon.getImage().getScaledInstance(
                            ImageUrl2.getWidth() > 0 ? ImageUrl2.getWidth() : 80,
                            ImageUrl2.getHeight() > 0 ? ImageUrl2.getHeight() : 80,
                            Image.SCALE_SMOOTH);
                    ImageUrl2.setIcon(new ImageIcon(imagenEscalada));
                    ImageUrl2.setText("");
                } catch (Exception e) {
                    ImageUrl2.setText("Error al cargar");
                    e.printStackTrace();
                }
            }
        }.execute();
    }

        public void attackBySpeed() {
        Pokemon _pokemon1 = trainers[0].getPokemonTeam().get(x);
        Pokemon _pokemon2 = trainers[1].getPokemonTeam().get(y);
        
        // Registrar qué Pokémon están en batalla
        battleHistory.logPokemonSent(0, _pokemon1.getName());
        battleHistory.logPokemonSent(1, _pokemon2.getName());
        
        int damage1 = _pokemon1.getMoves().get(moveTrainer1).getPower();
        int damage2 = _pokemon2.getMoves().get(moveTrainer2).getPower();
        
        if (_pokemon1.getSpeed() >= _pokemon2.getSpeed()) {
            // Pokemon 1 ataca primero
            battleHistory.logMoveSelection(0, _pokemon1.getName(), 
                _pokemon1.getMoves().get(moveTrainer1).getName());
            _pokemon1.movement(_pokemon2, moveTrainer1);
            battleHistory.logAttack(_pokemon1.getName(), 
                _pokemon1.getMoves().get(moveTrainer1).getName(), 
                _pokemon2.getName(), damage1, _pokemon2.getHp());
                
            if (_pokemon2.getHp() <= 0) {
                _pokemon2.setHp((short)0);
                healthBar2.setHP(0);
                hpLabel2.setText("0");
                ImageUrl2.setIcon(null);
                disableTrainer2Attacks();
                attack2_1.setVisible(false);
                attack2_2.setVisible(false);
                attack2_3.setVisible(false);
                attack2_4.setVisible(false);
                pokemonSelectionPanel2.setVisible(true);
                jPanel2.setComponentZOrder(pokemonSelectionPanel2, 0);
                battleHistory.logPokemonFainted(_pokemon2.getName());
                checkBattleState();
                enableTrainer1Attacks();
            } else {
                healthBar2.setHP(_pokemon2.getHp());
                hpLabel2.setText(String.valueOf(_pokemon2.getHp()));
                
                // Pokemon 2 contraataca
                battleHistory.logMoveSelection(1, _pokemon2.getName(), 
                    _pokemon2.getMoves().get(moveTrainer2).getName());
                _pokemon2.movement(_pokemon1, moveTrainer2);
                battleHistory.logAttack(_pokemon2.getName(), 
                    _pokemon2.getMoves().get(moveTrainer2).getName(), 
                    _pokemon1.getName(), damage2, _pokemon1.getHp());
                    
                if (_pokemon1.getHp() <= 0) {
                    _pokemon1.setHp((short)0);
                    healthBar1.setHP(0);
                    hpLabel1.setText("0");
                    imageUrl.setIcon(null);
                    disableTrainer1Attacks();
                    attack1_1.setVisible(false);
                    attack1_2.setVisible(false);
                    attack1_3.setVisible(false);
                    attack1_4.setVisible(false);
                    pokemonSelectionPanel1.setVisible(true);
                    jPanel2.setComponentZOrder(pokemonSelectionPanel1, 0);
                    battleHistory.logPokemonFainted(_pokemon1.getName());
                    checkBattleState();
                    enableTrainer2Attacks();
                } else {
                    healthBar1.setHP(_pokemon1.getHp());
                    hpLabel1.setText(String.valueOf(_pokemon1.getHp()));
                    enableTrainer1Attacks();
                    enableTrainer2Attacks();
                }
            }
        } else {
            // Pokemon 2 ataca primero (implementación similar al bloque anterior)
            battleHistory.logMoveSelection(1, _pokemon2.getName(), 
                _pokemon2.getMoves().get(moveTrainer2).getName());
            _pokemon2.movement(_pokemon1, moveTrainer2);
            battleHistory.logAttack(_pokemon2.getName(), 
                _pokemon2.getMoves().get(moveTrainer2).getName(), 
                _pokemon1.getName(), damage2, _pokemon1.getHp());
            if (_pokemon1.getHp() <= 0) {
                _pokemon1.setHp((short)0);
                healthBar1.setHP(0);
                hpLabel1.setText("0");
                imageUrl.setIcon(null);
                disableTrainer1Attacks();
                attack1_1.setVisible(false);
                attack1_2.setVisible(false);
                attack1_3.setVisible(false);
                attack1_4.setVisible(false);
                pokemonSelectionPanel1.setVisible(true);
                jPanel2.setComponentZOrder(pokemonSelectionPanel1, 0);
                battleHistory.logPokemonFainted(_pokemon1.getName());
                checkBattleState();
                enableTrainer2Attacks();
            } else {
                healthBar1.setHP(_pokemon1.getHp());
                hpLabel1.setText(String.valueOf(_pokemon1.getHp()));
                
                // Pokemon 1 contraataca
                battleHistory.logMoveSelection(0, _pokemon1.getName(), 
                    _pokemon1.getMoves().get(moveTrainer1).getName());
                _pokemon1.movement(_pokemon2, moveTrainer1);
                battleHistory.logAttack(_pokemon1.getName(), 
                    _pokemon1.getMoves().get(moveTrainer1).getName(), 
                    _pokemon2.getName(), damage1, _pokemon2.getHp());
                    
                if (_pokemon2.getHp() <= 0) {
                    _pokemon2.setHp((short)0);
                    healthBar2.setHP(0);
                    hpLabel2.setText("0");
                    ImageUrl2.setIcon(null);
                    disableTrainer2Attacks();
                    attack2_1.setVisible(false);
                    attack2_2.setVisible(false);
                    attack2_3.setVisible(false);
                    attack2_4.setVisible(false);
                    pokemonSelectionPanel2.setVisible(true);
                    jPanel2.setComponentZOrder(pokemonSelectionPanel2, 0);
                    battleHistory.logPokemonFainted(_pokemon2.getName());
                    checkBattleState();
                    enableTrainer1Attacks();
                } else {
                    healthBar2.setHP(_pokemon2.getHp());
                    hpLabel2.setText(String.valueOf(_pokemon2.getHp()));
                    enableTrainer1Attacks();
                    enableTrainer2Attacks();
                }
            }       
        }
        
        // Verificar si la batalla ha terminado
        boolean trainer1Dead = trainers[0].getPokemonTeam().stream().noneMatch(p -> p.getHp() > 0);
        boolean trainer2Dead = trainers[1].getPokemonTeam().stream().noneMatch(p -> p.getHp() > 0);
        
        if (trainer1Dead || trainer2Dead) {
            String winner = trainer1Dead ? trainers[1].getName() : trainers[0].getName();
            battleHistory.logBattleEnd(winner);
            battleHistory.saveBattleHistory();
            
        }
        
        // Reinicia los movimientos seleccionados
        moveTrainer1 = -1;
        moveTrainer2 = -1;
        deselectAllButtons();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(() -> new BattleGUIView().setVisible(true));
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BattleGUIView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(BattleGUIView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BattleGUIView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(BattleGUIView.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BattleGUIView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private JLabel ImageUrl2;
    private JToggleButton attack1_1;
    private JToggleButton attack1_2;
    private JToggleButton attack1_3;
    private JToggleButton attack1_4;
    private JToggleButton attack2_1;
    private JToggleButton attack2_2;
    private JToggleButton attack2_3;
    private JToggleButton attack2_4;
    private JLabel trainer1;
    private JLabel trainer2;
    private JLabel imageUrl;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JSeparator centralBaseBorder;
    private JSeparator centralMiddleBorder;
    private JSeparator centralLightBorder;
    private JLabel pokemon2;
    private JLabel pokemon1;
    private JLabel maxHpLabel1;
    private JLabel maxHpLabel2;
    private JLabel hpLabel1;
    private JLabel hpLabel2;
    private JPanel hpPanel1;
    private JPanel hpPanel2;
    private JPanel pokemonSelectionPanel1;
    private JPanel pokemonSelectionPanel2;
    private JPanel victoryPanel;
    private JButton[] pokemonButtons1;
    private JButton[] pokemonButtons2;
    private JLabel victoryLabel;
    private JLabel slashLabel1;
    private JLabel slashLabel2;
    private PokemonHealthBar healthBar1;
    private PokemonHealthBar healthBar2;
    // End of variables declaration

    class BackgroundPanel extends JPanel{
        private Image imagen;
        @Override
        public void paint(Graphics g) {
            imagen = new ImageIcon(getClass().getResource("/View/Utils/fondoBatalla.jpg")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);

            setOpaque(false);
            super.paint(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(657, 360); // Set fixed size (width x height)
        }
    }
}