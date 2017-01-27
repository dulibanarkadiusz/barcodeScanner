package example.scanner;

/**
 * Created by Hanna on 16.01.2017.
 */

public interface ISendComputer {
    void onComputerSend(Computer computer);
    //ZADANIE 4
    void onComputerSend(Computer computer, Boolean save);
}
