/*
 * ... (commentaires de licence) ...
 */
package org.emp.gl.timer.service;

// 1. Importer la classe Java standard
import java.beans.PropertyChangeListener;

/**
 *
 * @author tina
 */
// 2. Faire hériter l'interface
public interface TimerChangeListener extends PropertyChangeListener {
    
    final static String DIXEME_DE_SECONDE_PROP = "dixième" ;
    final static String SECONDE_PROP = "seconde" ;
    final static String MINUTE_PROP = "minute" ;
    final static String HEURE_PROP = "heure" ;
    
    // 3. La méthode propertyChange(String, Object, Object)
    //    est maintenant redondante car elle est en conflit
    //    avec celle de PropertyChangeListener.
    //    Nous devons la supprimer ou la commenter.
    /*
    void propertyChange (String prop, Object oldValue, Object newValue) ;
    */
    
    // L'interface PropertyChangeListener nous force déjà à implémenter :
    // void propertyChange(PropertyChangeEvent evt);
}