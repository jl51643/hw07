package hr.fer.zemris.java.gui.layouts;

/**
 * Thrown if user tries to create forbidden calculator layout component
 */
public class CalcLayoutException extends RuntimeException{

    /**
     * Constructing new CalcLayoutException with given message
     *
     * @param message
     */
    public CalcLayoutException(String message) {
        super(message);
    }
}
