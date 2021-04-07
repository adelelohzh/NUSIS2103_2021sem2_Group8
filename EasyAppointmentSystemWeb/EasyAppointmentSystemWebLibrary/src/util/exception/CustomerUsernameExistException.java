package util.exception;


public class CustomerUsernameExistException extends Exception {

    public CustomerUsernameExistException() {
    }

    public CustomerUsernameExistException(String string) {
        super(string);
    }
    
}
