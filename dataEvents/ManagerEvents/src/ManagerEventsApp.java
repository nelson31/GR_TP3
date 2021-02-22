import Comunicator.Manager;

import java.io.IOException;

public class ManagerEventsApp {

    /**
     * Metodo principal do projeto
     * @param args
     */
    public static void main(String[] args) {

        try {
            Manager m = new Manager();
            m.start();
        } catch (IOException e){
            System.out.println("Erro na comunicação com o host!!!");
        }
    }

}
