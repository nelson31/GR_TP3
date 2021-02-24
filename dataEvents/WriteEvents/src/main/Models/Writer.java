package main.Models;

import java.io.*;
import java.util.List;

/**
 * Classe responsável por escrever uma dada string para um ficheiro
 *
 * @author (Nelson Faria)
 * @version 1.0 (12/2020)
 */

public class Writer {

    /**
     * Método que escreve um conjunto de linhas para um ficheiro (faz o append)
     * @param linhas
     * @param filename
     * @throws IOException
     */
    public static void write_lines(List<String> linhas, String filename)
            throws IOException
    {
        // O true serve para usar o modo append
        PrintWriter pw = new PrintWriter(new FileWriter(filename,true));

        for(String linha : linhas) {
            pw.println(linha);
        }

        pw.close();
    }

    /**
     * Método que escreve para um ficheiro o conteudo da string (faz o append)
     * @param content
     * @param filename
     * @throws IOException
     */
    public static void write(String content, String filename)
            throws IOException
    {
        // O true serve para usar o modo append
        PrintWriter pw = new PrintWriter(new FileOutputStream(filename,true));

        pw.println(content);

        pw.close();
    }

    /**
     * Método que escreve para um ficheiro o conteudo da string
     * @param content
     * @param filename
     * @throws IOException
     */
    public static void create(String content, String filename)
            throws IOException
    {
        // O true serve para usar o modo append
        PrintWriter pw = new PrintWriter(new FileWriter(filename));

        pw.print(content);

        pw.close();
    }

}
