package outils.son.exceptions;

import java.io.Serializable;

public class SonTypeException extends SonException implements Serializable
{
    public SonTypeException() {
        super("Le type du son n'est pas reconnu");
    }
}