package outils.son;

import outils.son.exceptions.SonException;
import java.io.File;
import java.io.Serializable;

public class Son implements Serializable
{
    private String son;
    private Sound sound;
    
    public Son(final String nomfic) {
        this.son = nomfic;
        try {
            this.sound = new Sound(new File(this.son));
        }
        catch (SonException ex) {}
    }
    
    public void play() {
        this.sound.boucle(1);
    }
    
    public void close() {
        this.sound.fermer();
    }
    
    public void playContinue() {
        this.sound.boucle();
    }
}