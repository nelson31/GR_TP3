package org.snmp4j.agent.eventsmib;

import java.io.*;
import java.nio.channels.FileLock;
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

        FileInputStream fis = new FileInputStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        /* Obtemos acesso exclusivo ao ficheiro de logs */
        FileLock fl = fis.getChannel().lock(0,Long.MAX_VALUE,true);

        String line;
        while((line = br.readLine()) != null){
            content.add(line);
        }
        fl.release();

        br.close();
        fis.close();
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
