package IDE;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import utils.Directorio;
import utils.NumeroLinea;

/**
 *
 * @author derek
 */
public class IDE extends javax.swing.JFrame {

    NumeroLinea numerolinea;
    Directorio dir;

    /**
     * Creates new form IDE
     */
    public IDE() {
        initComponents();
        inizializar();
        colors();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    public void clearAllComp() {
        jtaCompile.setText("");
    }

    private void inizializar() {
        //numero de linea
        numerolinea = new NumeroLinea(jtpCode);
        jScrollPane1.setRowHeaderView(numerolinea);
        dir = new Directorio();
    }

    //metodo para encontrar ultimas cadenas
    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    //metodo para las primeras cadenas
    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    //metodo para pintar las palabras reservadas
    private void colors() {
        final StyleContext cont = StyleContext.getDefaultStyleContext();

        // Colores para diferentes tipos de palabras reservadas
        final AttributeSet attVariables = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0, 0, 249)); // Azul
        final AttributeSet attMetodos = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0, 249, 0)); // Verde
        final AttributeSet attEstructurasControl = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(249, 0, 0)); // Rojo
        final AttributeSet attOtros = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(144, 0, 144)); // Púrpura

        // Estilo del documento
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);
                String text = getText(0, getLength());
                int startLine = text.substring(0, offset).lastIndexOf('\n') + 1;
                int endLine = text.indexOf('\n', offset);
                if (endLine < 0) {
                    endLine = text.length();
                }
                String lineText = text.substring(startLine, endLine);
                String[] words = lineText.split("\\W");
                int lineOffset = 0;
                for (String word : words) {
                    if (!word.isEmpty()) {
                        int start = startLine + lineOffset;
                        int end = start + word.length();
                        applyStyleToReservedWord(word, start, end, attVariables, attMetodos, attEstructurasControl, attOtros);
                    }
                    lineOffset += word.length() + 1; // +1 for the non-word character
                }
            }

            private void applyStyleToReservedWord(String word, int start, int end, AttributeSet attVariables, AttributeSet attMetodos, AttributeSet attEstructurasControl, AttributeSet attOtros) {
                if (isVariable(word)) {
                    setCharacterAttributes(start, end - start, attVariables, false);
                } else if (isMetodo(word)) {
                    setCharacterAttributes(start, end - start, attMetodos, false);
                } else if (isEstructuraControl(word)) {
                    setCharacterAttributes(start, end - start, attEstructurasControl, false);
                } else {
                    setCharacterAttributes(start, end - start, attOtros, false);
                }
            }

            private boolean isVariable(String word) {
                return word.matches("\\b(ENT|DEC|CADENA|BOOL)\\b");
            }

            private boolean isMetodo(String word) {
                return word.matches("\\b(DEVUELVE|ENTRADA|IMPRIME)\\b");
            }

            private boolean isEstructuraControl(String word) {
                return word.matches("\\b(SI|HAZ|SINO|BUCLE)\\b");
            }
        };

        // Aplicar el estilo al JTextPane
        String temp = jtpCode.getText();
        jtpCode.setStyledDocument(doc);
        jtpCode.setText(temp);
    }

    

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        btnCompilar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnAbrir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnReserved = new javax.swing.JButton();
        btnIdentifiers = new javax.swing.JButton();
        btnTokens = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtpCode = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaCompile = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Zabre IDE");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1360, 690));
        setPreferredSize(new java.awt.Dimension(1360, 720));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCompilar.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        btnCompilar.setText("COMPILAR");
        btnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompilarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCompilar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 140, 70));

        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 140, 70));

        btnAbrir.setText("ABRIR");
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirActionPerformed(evt);
            }
        });
        getContentPane().add(btnAbrir, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 140, 70));

        btnGuardar.setFont(new java.awt.Font("Helvetica Neue", 0, 8)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 140, 70));

        btnReserved.setFont(new java.awt.Font("Helvetica Neue", 0, 8)); // NOI18N
        btnReserved.setText("RESERVADAS");
        getContentPane().add(btnReserved, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 140, 70));

        btnIdentifiers.setText("IDENT");
        btnIdentifiers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIdentifiersActionPerformed(evt);
            }
        });
        getContentPane().add(btnIdentifiers, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, 140, 70));

        btnTokens.setText("TOKENS");
        btnTokens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTokensActionPerformed(evt);
            }
        });
        getContentPane().add(btnTokens, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 140, 70));

        jScrollPane1.setViewportView(jtpCode);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1330, 360));

        jtaCompile.setColumns(20);
        jtaCompile.setRows(5);
        jScrollPane2.setViewportView(jtaCompile);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 1340, 210));

        pack();
    }// </editor-fold>                        

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        jtaCompile.setText("");
        dir.Nuevo(this);
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        dir.Guardar(this);
    }

    private void btnIdentifiersActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {
        dir.Abrir(this);
    }

    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {
       
    }

    private void btnTokensActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO: Implementar la lógica para mostrar los tokens
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnCompilar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnIdentifiers;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnReserved;
    private javax.swing.JButton btnTokens;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jtaCompile;
    public javax.swing.JTextPane jtpCode;
    // End of variables declaration                   
}
