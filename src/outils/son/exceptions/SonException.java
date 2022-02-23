package outils.son.exceptions;

import java.io.Serializable;

public class SonException extends Exception implements Serializable
{
    public SonException(final String message) {
        super(message);
    }
}