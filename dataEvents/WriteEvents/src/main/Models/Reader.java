package main.Models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por ler o conteudo de um ficheiro
 *
 * @author (Nelson Faria)
 * @version 1.0 (12/2020)
 */

public class Reader {

    /**
     * Método que lê o conteúdo de um ficheiro passado como parametro
     * e retorna todas as linhas do ficheiro
     * @param filename
     * @return
     * @throws IOException
     */
    public static List<String> read_lines(String filename)
            throws IOException
    {
        List<String> content = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while((line = br.readLine()) != null){
            content.add(line);
        }
        br.close();
        return content;
    }

    /**
     * Método que lê o conteúdo de um ficheiro passado como parametro
     * e retorna o conteudo de todo o ficheiro
     * @param filename
     * @return
     * @throws IOException
     */
    public static String read(String filename)
            throws IOException
    {
        StringBuilder content = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while((line = br.readLine()) != null){
            content.append(line);
        }
        br.close();
        return content.toString();
    }
}
