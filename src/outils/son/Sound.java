package outils.son;

import outils.son.ecouteurs.EcouteurSon;
import outils.son.exceptions.SonErreurDiverse;
import java.io.IOException;
import outils.son.exceptions.SonErreurLecture;
import javax.sound.sampled.UnsupportedAudioFileException;
import outils.son.exceptions.SonTypeException;
import javax.sound.sampled.Line;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioSystem;
import outils.son.exceptions.SonIntrouvableException;
import java.io.File;
import outils.son.exceptions.SonException;
import java.net.URL;
import java.util.Vector;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.Serializable;

public class Sound implements Serializable, Runnable
{
    private Duree duree;
    private AudioInputStream lecteurAudio;
    private AudioFileFormat formatFichier;
    private AudioFormat format;
    private Clip clip;
    private Thread thread;
    private int tour;
    private boolean pause;
    private boolean fermerALaFin;
    private Vector ecouteurs;
    
    public Sound(final URL url) throws SonException {
        this.ecouteurs = new Vector();
        this.initialise(url);
    }
    
    public Sound(final File fichier) throws SonException {
        this.ecouteurs = new Vector();
        if (!fichier.exists()) {
            throw new SonIntrouvableException(fichier);
        }
        this.initialise(fichier);
    }
    
    private void initialise(final File fichier) throws SonException {
        try {
            this.lecteurAudio = AudioSystem.getAudioInputStream(fichier);
            this.formatFichier = AudioSystem.getAudioFileFormat(fichier);
            this.format = this.lecteurAudio.getFormat();
            if (this.format.getEncoding() == AudioFormat.Encoding.ULAW || this.format.getEncoding() == AudioFormat.Encoding.ALAW) {
                final AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.format.getSampleRate(), this.format.getSampleSizeInBits() * 2, this.format.getChannels(), this.format.getFrameSize() * 2, this.format.getFrameRate(), true);
                this.lecteurAudio = AudioSystem.getAudioInputStream(tmp, this.lecteurAudio);
                this.format = tmp;
            }
            final DataLine.Info info = new DataLine.Info(Clip.class, this.lecteurAudio.getFormat(), (int)this.lecteurAudio.getFrameLength() * this.format.getFrameSize());
            this.clip = (Clip)AudioSystem.getLine(info);
            this.reouvrir();
        }
        catch (UnsupportedAudioFileException uafe) {
            throw new SonTypeException();
        }
        catch (IOException ioe) {
            throw new SonErreurLecture();
        }
        catch (Exception e) {
            throw new SonErreurDiverse(e);
        }
        this.duree = new Duree(this.longueurSonMicroseconde());
    }
    
    private void initialise(final URL url) throws SonException {
        try {
            this.lecteurAudio = AudioSystem.getAudioInputStream(url);
            this.formatFichier = AudioSystem.getAudioFileFormat(url);
            this.format = this.lecteurAudio.getFormat();
            if (this.format.getEncoding() == AudioFormat.Encoding.ULAW || this.format.getEncoding() == AudioFormat.Encoding.ALAW) {
                final AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.format.getSampleRate(), this.format.getSampleSizeInBits() * 2, this.format.getChannels(), this.format.getFrameSize() * 2, this.format.getFrameRate(), true);
                this.lecteurAudio = AudioSystem.getAudioInputStream(tmp, this.lecteurAudio);
                this.format = tmp;
            }
            final DataLine.Info info = new DataLine.Info(Clip.class, this.lecteurAudio.getFormat(), (int)this.lecteurAudio.getFrameLength() * this.format.getFrameSize());
            this.clip = (Clip)AudioSystem.getLine(info);
            this.reouvrir();
        }
        catch (UnsupportedAudioFileException uafe) {
            throw new SonTypeException();
        }
        catch (IOException ioe) {
            throw new SonErreurLecture();
        }
        catch (Exception e) {
            throw new SonErreurDiverse(e);
        }
        this.duree = new Duree(this.longueurSonMicroseconde());
    }
    
    public void jouer() {
        if (this.thread == null) {
            (this.thread = new Thread(this)).start();
        }
        this.tour = 1;
    }
    
    public void boucle(final int nbFois) {
        if (this.thread == null) {
            (this.thread = new Thread(this)).start();
        }
        this.tour = nbFois;
    }
    
    public void boucle() {
        this.boucle(Integer.MAX_VALUE);
    }
    
    @Override
    public void run() {
        while (this.thread != null) {
            try {
                Thread.sleep(123L);
            }
            catch (Exception ex) {}
            if (this.tour > 0) {
                this.clip.start();
                try {
                    Thread.sleep(99L);
                }
                catch (Exception ex2) {}
                while ((this.clip.isActive() || this.pause) && this.thread != null) {
                    if (!this.pause) {
                        this.avancer();
                    }
                    try {
                        Thread.sleep(99L);
                    }
                    catch (Exception e) {
                        break;
                    }
                }
                this.clip.stop();
                this.placeMicroseconde(0L);
                --this.tour;
                if (this.tour >= 1) {
                    continue;
                }
                this.terminer();
                if (!this.fermerALaFin) {
                    continue;
                }
                this.fermer();
            }
        }
    }
    
    private void reouvrir() throws Exception {
        this.clip.open(this.lecteurAudio);
    }
    
    public void pause() {
        if (!this.pause) {
            this.clip.stop();
            this.pause = true;
        }
    }
    
    public void reprise() {
        if (this.pause) {
            this.pause = false;
            this.clip.start();
        }
    }
    
    public void stop() {
        this.clip.stop();
        this.placeMicroseconde(0L);
        this.pause = false;
        this.tour = 0;
        this.thread = null;
    }
    
    public void fermer() {
        this.stop();
        this.clip.close();
        this.clip = null;
        this.duree = null;
        this.ecouteurs.clear();
        this.ecouteurs = null;
        this.format = null;
        this.formatFichier = null;
    }
    
    public boolean estFermerALaFin() {
        return this.fermerALaFin;
    }
    
    public void setFermerALaFin(final boolean fermer) {
        this.fermerALaFin = fermer;
    }
    
    public long longueurSonMicroseconde() {
        return this.clip.getMicrosecondLength();
    }
    
    public long getRenduMicroseconde() {
        return this.clip.getMicrosecondPosition();
    }
    
    public Duree getRendu() {
        return new Duree(this.getRenduMicroseconde());
    }
    
    public void placeMicroseconde(final long microseconde) {
        this.clip.setMicrosecondPosition(microseconde);
    }
    
    public void placeDuree(final Duree duree) {
        this.placeMicroseconde(duree.getMicroseconde());
    }
    
    public void placeDepart() {
        this.clip.setMicrosecondPosition(0L);
    }
    
    public boolean estEnPause() {
        return this.pause;
    }
    
    public boolean estEntrainDeJouer() {
        return !this.pause && this.tour > 0;
    }
    
    public void ajouteEcouteurSon(final EcouteurSon ecouteur) {
        if (ecouteur != null) {
            this.ecouteurs.addElement(ecouteur);
        }
    }
    
    public void retireEcouteurSon(final EcouteurSon ecouteur) {
        if (ecouteur != null) {
            this.ecouteurs.removeElement(ecouteur);
        }
    }
    
    private void terminer() {
        final Thread t = new Thread() {
            @Override
            public void run() {
                Sound.this.terminer1();
            }
        };
        t.start();
    }
    
    private void terminer1() {
        for (int nb = this.ecouteurs.size(), i = 0; i < nb; ++i) {
            final EcouteurSon ecouteur = (EcouteurSon) this.ecouteurs.elementAt(i);
            ecouteur.sonTermine(this);
        }
    }
    
    private void avancer() {
        final Thread t = new Thread() {
            @Override
            public void run() {
                Sound.this.avancer1();
            }
        };
        t.start();
    }
    
    private void avancer1() {
        for (int nb = this.ecouteurs.size(), i = 0; i < nb; ++i) {
            final EcouteurSon ecouteur = (EcouteurSon) this.ecouteurs.elementAt(i);
            ecouteur.sonChangePosition(this);
        }
    }
    
    public Duree getDuree() {
        return this.duree;
    }
}