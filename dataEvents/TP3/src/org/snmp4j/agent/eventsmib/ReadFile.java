package org.snmp4j.agent.eventsmib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

    /**
     * Método que lê o conteúdo de um ficheiro,
     * retornando as linhas todas do ficheiro
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
     * Método que lê o conteúdo de um ficheiro,
     * retornando o conteudo em bruto
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
