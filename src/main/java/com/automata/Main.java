package com.automata;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Main extends JFrame {
    private static String[] estados = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static List<String> estadosAFD, listAceptacion;
    private static Map<String, Map<Integer, String>> afndMap;
    private static int nEstados, nEntradas, nAceptacion;
    private static String[] sEntradas;
    static boolean isAFND = false;

    static JFrame jFrame;

    public Main() {

    }

    public static void main(String[] args) {
        //menu();
        initAFND();
    }

    public static void initMain(){
        JOptionPane.showMessageDialog(null, initAFND());
        JOptionPane.showMessageDialog(null, "Validar y convertir automata");

        if (isAFND) {
            convertAutomata();
            JOptionPane.showMessageDialog(null, initAFD());
        } else {
            JOptionPane.showMessageDialog(null, "El automata no es FND.  \nPor favor ingrese uno valido");
            JOptionPane.showMessageDialog(null, initAFND());
        }

    }
    public static String initAFND() {
        nEntradas = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de Entradas"));

        sEntradas = new String[nEntradas];

        for (int i = 0; i < nEntradas; i++) {
            sEntradas[i] = JOptionPane.showInputDialog("Entrada # " + (i + 1));
        }

        nEstados = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de Estados"));
        afndMap = new HashMap<>();
        for (int i = 0; i < nEstados; i++) {
            Map<Integer, String> entries = new HashMap<>();
            for (int j = 0; j < nEntradas; j++) {
                String estadoTo = JOptionPane.showInputDialog("Estado " + estados[i] + " llega un " + sEntradas[j]);

                if (estadoTo.equals("")){
                    estadoTo = " ";
                }
                entries.put(j, estadoTo);

            }
            afndMap.put(estados[i], entries);

        }

        nAceptacion = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de estados de aceptación"));

        listAceptacion = new ArrayList<>();

        if (nAceptacion <= nEstados) {
            for (int i = 0; i < nAceptacion; i++) {
                String estado = JOptionPane.showInputDialog("Ingrese el estado de aceptacion " + (i + 1));
                listAceptacion.add(estado);
            }
        }
        Map<Integer, String> entries = new HashMap<>();

        String tabAFND = "Estado/Entradas  ";

        Object [][] object = new Object[nEstados][nEntradas+2];

        for (int i = 0; i < nEntradas; i++) {
            tabAFND += sEntradas[i] + "  ";
        }

        tabAFND += "\n";

        for (int i = 0; i < afndMap.size(); i++) {
            String estado = estados[i];
            entries = afndMap.get(estados[i]);
            tabAFND += estado + ":          ";
            object[i][0] = estado;

            for (int j = 0; j < entries.size(); j++) {

                String estadoTo = entries.get(j);
                tabAFND += estadoTo + "     ";

                object[i][j+1] = estadoTo;

                if (estadoTo.length() != 1) {
                    isAFND = true;
                }
            }
            String aceptacion = "0";

            for (int j = 0; j < nAceptacion; j++) {
                if (estado.equals(listAceptacion.get(j))) {
                    aceptacion = "1";
                }
            }
            object[i][nEntradas+1] = aceptacion;

            tabAFND += aceptacion + "\n";
        }
        initTable("Automata Finito No Determinsitico", object);
        return tabAFND;
    }
    public static void convertAutomata() {
        System.out.println("convertAutomata");
        estadosAFD = new ArrayList<>();
        estadosAFD.add(estados[0]);

        Map<Integer, String> entries;
        boolean isEmpty = false;

        for (int j = 0; j < estadosAFD.size(); j++) {

            String estado = estadosAFD.get(j);

            for (int i = 0; i < nEntradas; i++) {

                StringBuilder estadoTo = new StringBuilder();

                if (estadosAFD.get(j).length() >= 2) {
                    for (int y = 0; y < estadosAFD.get(j).length(); y++) {
                        char c = estadosAFD.get(j).charAt(y);

                        entries = afndMap.get(String.valueOf(c));
                        String s = entries.get(i);

                        if (!s.equals(" ")){
                            for (int k = 0; k < s.length(); k++) {
                                if (estadoTo.toString().indexOf(s.charAt(k)) != -1) {

                                } else {
                                    estadoTo.append(s.charAt(k));
                                }
                            }
                        }


                    }
                } else {
                    if (estado.equals(" ")){
                        isEmpty = true;
                    }else {
                        entries = afndMap.get(estado);
                        estadoTo = new StringBuilder(entries.get(i));
                    }

                }

                char[] StringtoChar = estadoTo.toString().toCharArray();
                Arrays.sort(StringtoChar);
                String SortedString = new String(StringtoChar);

                if (!SortedString.equals(" ")) {
                    boolean isEstado = estadosAFD.contains(SortedString);
                    if (!isEstado) {
                        estadosAFD.add(SortedString);
                    }
                } else {
                    isEmpty = true;
                }

            }
        }
        if (isEmpty) {
            estadosAFD.add("");
        }
    }
    public static String initAFD() {
        String tabAFD = "Estado/Entradas  ";

        for (int i = 0; i < nEntradas; i++) {
            tabAFD += sEntradas[i] + " ";
        }

        tabAFD += "\n";
        Object [][] object = new Object[estadosAFD.size()][nEntradas+2];
        Map<Integer, String> entries;
        for (int i = 0; i < estadosAFD.size(); i++) {

            String estado = estadosAFD.get(i);

            if (estado.equals("")){
                tabAFD += "Error:    ";
                object[i][0] = "Error";
            }else {
                tabAFD += estado + ":    ";
                object[i][0] = estado;
            }

            for (int j = 0; j < nEntradas; j++) {
                String estadoTo = "";

                if (estadosAFD.get(i).length() >= 2) {
                    for (int y = 0; y < estadosAFD.get(i).length(); y++) {
                        char c = estadosAFD.get(i).charAt(y);

                        entries = afndMap.get(String.valueOf(c));
                        String s = entries.get(j);

                        for (int k = 0; k < s.length(); k++) {

                            if (estadoTo.indexOf(s.charAt(k)) != -1) {

                            } else {
                                estadoTo += (s.charAt(k));
                            }
                        }
                    }
                } else {
                    if (!estado.equals("")) {
                        entries = afndMap.get(estado);
                        estadoTo = entries.get(j);
                    }

                }

                char[] StringtoChar = estadoTo.toCharArray();
                Arrays.sort(StringtoChar);
                String SortedString = new String(StringtoChar);

                if (SortedString.equals(" ") || SortedString.equals("")) {
                    SortedString = "Error";
                }

                object[i][j+1] = SortedString;
                tabAFD += SortedString + "  ";


            }
            String aceptacion = "0";

            for (int k = 0; k < nAceptacion; k++) {
                if (estado.contains(listAceptacion.get(k))) {
                    aceptacion = "1";
                }
            }
            object[i][nEntradas+1] = aceptacion;
            tabAFD += "\n";
        }
        initTable("Automata Finito Determinsitico", object);

        return tabAFD;
    }
    public static void initTable(String tittle, Object[][] object){
        // Print the table (Imprimo la tabla)
        jFrame = new JFrame();
        JPanel jPanel1 = new javax.swing.JPanel();
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        JTable jTable1 = new javax.swing.JTable();
        JButton jButton1 = new javax.swing.JButton();
        JButton jButton2 = new javax.swing.JButton();

        //Línea 1
        jFrame.setSize(new Dimension(600, 400));

        //Línea 2
        jFrame.setMinimumSize(new Dimension(600, 400));
        jFrame.setLocationRelativeTo(null);


        jFrame.setTitle("CovertAutomata Compiladores");

        jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(tittle));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(object,
                new String [] {
                        "Estados/Entradas", "0", "1", "A(1)/R(0)"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        jButton1.setText("SALIR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Convertir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println("Convertir " + isAFND);
                if (isAFND) {
                    convertAutomata();
                    initAFD();
                } else {
                    JOptionPane.showMessageDialog(null, "El automata no es FND.  \nPor favor ingrese uno valido");
                    initAFND();
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jFrame.getContentPane());
        jFrame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(89, 89, 89))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
                                .addContainerGap(18, Short.MAX_VALUE))
        );

        jFrame.setVisible(true);
    }
}