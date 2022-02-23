package outils.son.exceptions;

import java.net.URL;
import java.io.File;
import java.io.Serializable;

public class SonIntrouvableException extends SonException implements Serializable
{
    public SonIntrouvableException(final File fichier) {
        super("Le fichier " + fichier.getAbsolutePath() + " est introuvable");
    }
    
    public SonIntrouvableException(final URL url) {
        super("L'URL : " + url.getFile() + " est introuvable");
    }
    
    public SonIntrouvableException(final String nom) {
        super("Le son : " + nom + " est introuvable");
    }
}