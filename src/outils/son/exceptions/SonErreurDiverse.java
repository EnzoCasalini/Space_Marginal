package outils.son.exceptions;

import java.io.Serializable;

public class SonErreurDiverse extends SonException implements Serializable
{
    public SonErreurDiverse(final Exception e) {
        super("Une erreur s'est produite lors de l'analyse du son : " + e.getMessage());
    }
}