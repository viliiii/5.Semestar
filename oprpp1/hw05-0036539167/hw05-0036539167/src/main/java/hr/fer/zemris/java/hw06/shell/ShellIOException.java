package hr.fer.zemris.java.hw06.shell;

public class ShellIOException extends RuntimeException{

    public ShellIOException(){
        super();
    }

    /**
     * Constructs a new ShellIOException with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ShellIOException(String detailMessage) {
        super(detailMessage);
    }

}
