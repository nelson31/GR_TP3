package main.Models;

import java.io.*;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
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
        FileOutputStream fos = new FileOutputStream(filename,true);
        FileLock fl = fos.getChannel().lock();
        // O true serve para usar o modo append

        fos.write((content + "\n").getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.getFD().sync();

        fl.release();
        fos.close();
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
        FileOutputStream fos = new FileOutputStream(filename);
        FileLock fl = fos.getChannel().lock();

        fos.write(content.getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.getFD().sync();

        fl.release();
        fos.close();
    }

}
