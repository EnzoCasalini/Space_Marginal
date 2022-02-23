package outils.son.exceptions;

import java.io.Serializable;

public class SonErreurLecture extends SonException implements Serializable
{
    public SonErreurLecture() {
        super("Erreur lors de la lecture du son");
    }
}