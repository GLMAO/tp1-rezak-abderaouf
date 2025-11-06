// ==================== Horloge.java ====================
package org.emp.gl.clients;

import java.beans.PropertyChangeEvent;
import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

/**
 * Représentation d'une horloge numérique
 * Affiche l'heure courante du système de timing
 */
public class Horloge implements TimerChangeListener {

    String name;
    TimerService timerService;
    private int compteurAffichages;
    private boolean actif;

    /**
     * Création d'une nouvelle instance d'horloge
     * @param name Identifiant unique de l'horloge
     * @param timerService Service de gestion du temps
     */
    public Horloge(String name, TimerService timerService) {
        this.name = name;
        this.timerService = timerService;
        this.compteurAffichages = 0;
        this.actif = true;
        enregistrerHorloge();
        loggerInitialisation();
    }
    
    /**
     * Enregistre cette horloge auprès du service de timing
     */
    private void enregistrerHorloge() {
        this.timerService.addTimeChangeListener(this);
    }
    
    /**
     * Affiche un message de confirmation d'initialisation
     */
    private void loggerInitialisation() {
        System.out.println("Horloge " + name + " initialized!");
    }
    
    /**
     * Retourne le nom de cette horloge
     */
    public String getNom() {
        return this.name;
    }
    
    /**
     * Vérifie si l'horloge est active
     */
    public boolean isActive() {
        return this.actif;
    }
    
    /**
     * Retourne le nombre total d'affichages effectués
     */
    public int getNombreAffichages() {
        return this.compteurAffichages;
    }
    
    /**
     * Réception et traitement des événements temporels
     * Affiche l'heure actuelle à chaque seconde
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();

        if (!TimerChangeListener.SECONDE_PROP.equals(prop)) {
            return;
        }
        
        if (!this.actif) {
            return;
        }
        
        afficherHeure();
        incrementerCompteur();
    }
    
    /**
     * Formate et affiche l'heure courante
     */
    private void afficherHeure() {
        String heureFormatee = construireAffichageHeure();
        System.out.println(heureFormatee);
    }
    
    /**
     * Construit la chaîne d'affichage de l'heure
     */
    private String construireAffichageHeure() {
        return name + " affiche " +
                timerService.getHeures() + ":" +
                timerService.getMinutes() + ":" +
                timerService.getSecondes();
    }
    
    /**
     * Incrémente le compteur d'affichages
     */
    private void incrementerCompteur() {
        this.compteurAffichages++;
    }
    
    /**
     * Désactive l'horloge
     */
    public void desactiver() {
        this.actif = false;
    }
    
    /**
     * Réactive l'horloge
     */
    public void activer() {
        this.actif = true;
    }
    
    /**
     * Réinitialise le compteur d'affichages
     */
    public void resetCompteur() {
        this.compteurAffichages = 0;
    }
}