package outils.son.ecouteurs;

import outils.son.Sound;

public interface EcouteurSon
{
    void sonTermine(final Sound p0);
    
    void sonChangePosition(final Sound p0);
}