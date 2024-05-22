package IDE;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import Analizers.Alexico;
import Analizers.Alexico.Token;
import Analizers.Interprete;
import Analizers.Parser;
import utils.Directorio;
import utils.NumeroLinea;

/**
 *
 * @author derek
 */
public class IDE extends javax.swing.JFrame {
	// ATRRIBUTOS DE LA CLASE IDE
	NumeroLinea numerolinea;
	Directorio dir;
	Alexico alex = new Alexico();
	Parser parser = new Parser();

	// CONSTRUCTOR DEL IDE
	public IDE() {
		initComponents();
		inizializar();
		colors();
	}

	// METODOS DE LA CLASE IDE

	private void inizializar() {
		// Inicializa componentes como el numero de linea del panel de texto JTextPane
		numerolinea = new NumeroLinea(jtpCode);
		jScrollPane1.setRowHeaderView(numerolinea);
		dir = new Directorio();
	}

	// metodo para pintar las palabras reservadas
	private void colors() {

		final StyleContext cont = StyleContext.getDefaultStyleContext();// Objeto cont sirve para proporcionar metodos
																		// para la gestion de estilos

		// Colores para diferentes tipos de palabras reservadas
		final AttributeSet attVariables = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(0, 0, 249)); // Azul
		final AttributeSet attMetodos = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(0, 249, 0)); // Verde
		final AttributeSet attEstructurasControl = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(249, 0, 0)); // Rojo
		final AttributeSet attOtros = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(144, 0, 144)); // Púrpura
		final AttributeSet attNumeros = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(255, 192, 0));

		// CLASE ANONIMA
		DefaultStyledDocument doc = new DefaultStyledDocument() {

			// SOBREESCRITURA DEL METODO INSETAR STRING
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
						applyStyleToReservedWord(word, start, end, attVariables, attMetodos, attEstructurasControl,
								attOtros, attNumeros);
					}
					lineOffset += word.length() + 1;
				}
			}

			private void applyStyleToReservedWord(String word, int start, int end, AttributeSet attVariables,
					AttributeSet attMetodos, AttributeSet attEstructurasControl, AttributeSet attOtros,
					AttributeSet attNumeros) {

				if (isVariable(word)) {
					setCharacterAttributes(start, end - start, attVariables, false);
				} else if (isMetodo(word)) {
					setCharacterAttributes(start, end - start, attMetodos, false);
				} else if (isEstructuraControl(word)) {
					setCharacterAttributes(start, end - start, attEstructurasControl, false);
				} else if (isNumero(word)) {
					setCharacterAttributes(start, end - start, attNumeros, false);
				} else {
					setCharacterAttributes(start, end - start, attOtros, false);
				}
			}

			// Metodos para determinar que tipode palabra es la que se esta evaluando
			private boolean isVariable(String word) {
				return word.matches("\\b(ENT|DEC|CADENA|BOOL)\\b");
			}

			private boolean isMetodo(String word) {
				return word.matches("\\b(DEVUELVE|ENTRADA|IMPRIME)\\b");
			}

			private boolean isEstructuraControl(String word) {
				return word.matches("\\b(SI|HAZ|SINO|BUCLE)\\b");
			}

			private boolean isNumero(String word) {
				return word.matches("\\d+");
			}

		};

		// Aplicar el estilo al JTextPane
		String temp = jtpCode.getText();
		jtpCode.setStyledDocument(doc);
		jtpCode.setText(temp);
	}

	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		// Los componentes se declararon coomo atrributos del IDE y aqui se inicializan
		// completamente
		// Botones, cuadros te texto y Scrolls
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

		// Se alteran los primeros cambios de
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
				// btnIdentifiersActionPerformed(evt); No hacia nada
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

	// Acciones-Metodos que realizan los diferentes botones
	private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
		jtaCompile.setText("");
		dir.Nuevo(this);
	}

	private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
		dir.Guardar(this);
	}

	private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {
		dir.Abrir(this);
	}

	private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {
		jtaCompile.setText("");
		String input = jtpCode.getText();
		ArrayList<Token> tokens = alex.getTokens(input);

		if (tokens.isEmpty()) {
			jtaCompile.append("No hay tokens válidos para compilar.\n");
			return;
		} else {
			ArrayList<Token> tokensList = new ArrayList<>();
			int line = 1;
			jtaCompile.append("Tokens encontrados son :" + tokens.size() + "\n\n");

			// Reconocimiento de diferentes lineas
			for (int i = 0; i <= tokens.size() - 1; i++) {
				tokensList.add(tokens.get(i));
				if (tokens.get(i).value.equals(";")) {
					String verificacion = parser.parse(tokensList);
					jtaCompile.append("En la linea " + line + " " );
					jtaCompile.append("\n" + "/////////////////////" + "\n");
					tokensList.clear();
					line++;

				}
			}
			// Pintado tokens
			jtaCompile.append("Los tokens fueron: \n");
			for (int i = 0; i <= tokens.size() - 1; i++) {
				jtaCompile.append((i + 1) + ".- " + tokens.get(i).type + " de valor ''" + tokens.get(i).value + "''\n");

			}

		}
		// La verdad no recuerdo el por qué de esta seccion de código
		try {

		} catch (Exception e) {
			jtaCompile.append("Error durante la compilación: " + e.getMessage() + "\n");
		} finally {
			System.setOut(System.out); // Restaurar la salida estándar
		}

		// A.sintáctico
		String verificacion = parser.parse(tokens);
		String arbol = parser.creacionArbol(parser.verificado, tokens);

		// Primero muestra en nuestra consola
		System.out.printf(arbol);
		System.out.printf(
				"El arbol Sintáctico: (descrito en nodos) \\n" + parser.convertirArbolAFormatoVisual(arbol) + "\n");
		// Muestra en
		jtaCompile.append(verificacion);
		jtaCompile.append(
				"\n \n El arbol Sintáctico: (descrito en nodos) \n" + parser.convertirArbolAFormatoVisual(arbol));

	}

	private void btnTokensActionPerformed(java.awt.event.ActionEvent evt) {																	
		lexer();
	}

	private void lexer() {
		String input = jtpCode.getText();
		ArrayList<Token> tokens = alex.getTokens(input);

		StringBuilder sb = new StringBuilder();
		for (Token token : tokens) {
			sb.append(token.toString()).append("\n");
		}

		jtaCompile.setText(sb.toString());
	}

	// ATRIBUTOS DE LA CALSE IDE
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

	public void clearAllComp() {
		jtaCompile.setText("");
		
	}
}