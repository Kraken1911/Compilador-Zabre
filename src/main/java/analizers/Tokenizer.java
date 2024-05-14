/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizers;

/**
 *
 * @author derek
 */
public class Tokenizer {
    private final String valor;
    private final Tokens tipo;
    private final int line ,column;

    public Tokenizer(String valor, Tokens tipo, int line, int column) {
        this.valor = valor;
        this.tipo = tipo;
        this.line = line;
        this.column = column;
    }

    public String getValor() {
        return valor;
    }

    public Tokens getTipo() {
        return tipo;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
    public String toString()
    {
        return "Tokenizer (valor=  "+valor+", tipo=  "+tipo+", Linea ="+line+" , columna = "+ column;
        
    }
    
    public void analisisLexico(String texto , Tokens tk )
    {
         String [] textodividido = texto.split("()");
        for (int i = 0; i < textodividido.length; i++) {
            
        }
    }
           
}
