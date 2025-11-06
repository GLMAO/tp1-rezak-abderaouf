// ==================== CompteARebours.java ====================
package org.emp.gl.clients;

import java.beans.PropertyChangeEvent;
import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

/**
 * Classe permettant de gérer un décompte temporel
 * Utilise le pattern Observer pour réagir aux changements de temps
 */
public class CompteARebours implements TimerChangeListener {

    private int compteur;
    private TimerService timerService;
    private boolean isActive;
    private long startTimestamp;//it is

    /**
     * Constructeur principal du compte à rebours
     * @param valeurInitiale La valeur de départ du compteur
     * @param service Le service de timer à utiliser
     */
    public CompteARebours(int valeurInitiale, TimerService service) {
        this.compteur = valeurInitiale;
        this.timerService = service;
        this.isActive = true;
        this.startTimestamp = System.currentTimeMillis();
        this.timerService.addTimeChangeListener(this);
        afficherMessage("Nouveau CompteARebours initialisé à " + valeurInitiale);
    }
    
    /**
     * Méthode auxiliaire pour l'affichage formaté
     */
    private void afficherMessage(String msg) {
        System.out.println(msg);
    }
    
    /**
     * Retourne l'état actuel du compteur
     */
    public int getCompteurActuel() {
        return this.compteur;
    }
    
    /**
     * Vérifie si le compte à rebours est toujours actif
     */
    public boolean estActif() {
        return this.isActive && this.compteur > 0;
    }
    
    /**
     * Calcule le temps écoulé depuis le démarrage non necessaire
     
    private long getTempsEcoule() {
        return System.currentTimeMillis() - this.startTimestamp;
    }*/
    
    /**
     * Gestion des événements de changement de propriété
     * Décrémente le compteur à chaque seconde
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        
        if (!TimerChangeListener.SECONDE_PROP.equals(prop)) {
            return;
        }
        
        if (!estActif()) {
            return;
        }
        
        if (compteur > 0) {
            afficherMessage("CompteARebours (" + compteur + ")");
            decompter();
        } else {
            terminerDecompte();
        }
    }
    
    /**
     * Décrémente le compteur d'une unité
     */
    private void decompter() {
        compteur--;
    }
    
    /**
     * Finalise le compte à rebours et se désinscrit du service
     */
    private void terminerDecompte() {
        afficherMessage("CompteARebours TERMINÉ. Désinscription.");
        this.isActive = false;
        this.timerService.removeTimeChangeListener(this);
    }
    
    /**
     * Réinitialise le compteur à une nouvelle valeur
     */
    public void reinitialiser(int nouvelleValeur) {
        this.compteur = nouvelleValeur;
        this.isActive = true;
        this.startTimestamp = System.currentTimeMillis();
    }
}

