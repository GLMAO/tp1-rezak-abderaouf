/*
 * Service de gestion temporelle avec notification d'événements
 * Implémentation utilisant le pattern Observer avec PropertyChangeSupport
 */
package org.emp.gl.time.service.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

/**
 * Implémentation du service de chronométrage
 * Fournit un système de notification basé sur les changements de temps
 * 
 * @author tina
 * @version 2.0
 */
public class DummyTimeServiceImpl implements TimerService {

    private int dixiemeDeSeconde;
    private int minutes;
    private int secondes;
    private int heures;
    
    private final PropertyChangeSupport support;
    private Timer internalTimer;
    private boolean serviceActif;
    private long derniereMiseAJour;

    /**
     * Initialise le service de chronométrage
     * Configure le timer interne et démarre la surveillance temporelle
     */
    public DummyTimeServiceImpl() {
        this.support = new PropertyChangeSupport(this);
        this.serviceActif = true;
        this.derniereMiseAJour = System.currentTimeMillis();
        
        initialiserValeurs();
        demarrerTimer();
    }
    
    /**
     * Démarre le timer de surveillance
     */
    private void demarrerTimer() {
        internalTimer = creerTimer();
        TimerTask tache = construireTimerTask();
        planifierExecution(internalTimer, tache);
    }
    
    /**
     * Crée une nouvelle instance de Timer
     */
    private Timer creerTimer() {
        return new Timer();
    }
    
    /**
     * Construit la tâche de surveillance temporelle
     */
    private TimerTask construireTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (serviceActif) {
                    traiterChangementTemps();
                }
            }
        };
    }
    
    /**
     * Planifie l'exécution périodique de la tâche
     */
    private void planifierExecution(Timer timer, TimerTask task) {
        timer.scheduleAtFixedRate(task, 100, 100);
    }

    /**
     * Initialise les valeurs temporelles à partir de l'heure système
     */
    private void initialiserValeurs() {
        LocalTime tempsActuel = obtenirTempsSysteme();
        appliquerValeurs(tempsActuel);
    }
    
    /**
     * Obtient le temps système actuel
     */
    private LocalTime obtenirTempsSysteme() {
        return LocalTime.now();
    }
    
    /**
     * Applique les valeurs du temps local
     */
    private void appliquerValeurs(LocalTime localTime) {
        mettreAJourSecondes(localTime.getSecond());
        mettreAJourMinutes(localTime.getMinute());
        mettreAJourHeures(localTime.getHour());
        mettreAJourDixiemes(localTime.getNano() / 100000000);
    }
    
    /**
     * Enregistre un écouteur de changements temporels
     */
    @Override
    public void addTimeChangeListener(TimerChangeListener pl) {
        if (pl == null) {
            return;
        }
        support.addPropertyChangeListener(pl);
    }

    /**
     * Retire un écouteur de changements temporels
     */
    @Override
    public void removeTimeChangeListener(TimerChangeListener pl) {
        if (pl == null) {
            return;
        }
        support.removePropertyChangeListener(pl);
    }

    /**
     * Gère le changement de temps détecté
     */
    private void traiterChangementTemps() {
        initialiserValeurs();
        mettreAJourTimestamp();
    }
    
    /**
     * Met à jour le timestamp de dernière modification
     */
    private void mettreAJourTimestamp() {
        this.derniereMiseAJour = System.currentTimeMillis();
    }

    /**
     * Définit une nouvelle valeur pour les dixièmes de seconde
     */
    public void setDixiemeDeSeconde(int newDixiemeDeSeconde) {
        if (!valeurDifferente(dixiemeDeSeconde, newDixiemeDeSeconde)) {
            return;
        }
        
        int ancienneValeur = dixiemeDeSeconde;
        dixiemeDeSeconde = newDixiemeDeSeconde;
        notifierChangementDixieme(ancienneValeur, dixiemeDeSeconde);
    }
    
    /**
     * Vérifie si deux valeurs sont différentes
     */
    private boolean valeurDifferente(int ancienne, int nouvelle) {
        return ancienne != nouvelle;
    }
    
    /**
     * Notifie les observateurs d'un changement de dixième
     */
    private void notifierChangementDixieme(int oldValue, int newValue) {
        support.firePropertyChange(TimerChangeListener.DIXEME_DE_SECONDE_PROP, 
                                   oldValue, newValue);
    }
    
    /**
     * Met à jour la valeur des dixièmes
     */
    private void mettreAJourDixiemes(int valeur) {
        setDixiemeDeSeconde(valeur);
    }

    /**
     * Définit une nouvelle valeur pour les secondes
     */
    public void setSecondes(int newSecondes) {
        if (!valeurDifferente(secondes, newSecondes)) {
            return;
        }
        
        int ancienneValeur = secondes;
        secondes = newSecondes;
        notifierChangementSeconde(ancienneValeur, secondes);
    }
    
    /**
     * Notifie les observateurs d'un changement de seconde
     */
    private void notifierChangementSeconde(int oldValue, int newValue) {
        support.firePropertyChange(TimerChangeListener.SECONDE_PROP, 
                                   oldValue, newValue);
    }
    
    /**
     * Met à jour la valeur des secondes
     */
    private void mettreAJourSecondes(int valeur) {
        setSecondes(valeur);
    }

    /**
     * Définit une nouvelle valeur pour les minutes
     */
    public void setMinutes(int newMinutes) {
        if (!valeurDifferente(minutes, newMinutes)) {
            return;
        }
        
        int ancienneValeur = minutes;
        minutes = newMinutes;
        notifierChangementMinute(ancienneValeur, minutes);
    }
    
    /**
     * Notifie les observateurs d'un changement de minute
     */
    private void notifierChangementMinute(int oldValue, int newValue) {
        support.firePropertyChange(TimerChangeListener.MINUTE_PROP, 
                                   oldValue, newValue);
    }
    
    /**
     * Met à jour la valeur des minutes
     */
    private void mettreAJourMinutes(int valeur) {
        setMinutes(valeur);
    }

    /**
     * Définit une nouvelle valeur pour les heures
     */
    public void setHeures(int newHeures) {
        if (!valeurDifferente(heures, newHeures)) {
            return;
        }
        
        int ancienneValeur = heures;
        heures = newHeures;
        notifierChangementHeure(ancienneValeur, heures);
    }
    
    /**
     * Notifie les observateurs d'un changement d'heure
     */
    private void notifierChangementHeure(int oldValue, int newValue) {
        support.firePropertyChange(TimerChangeListener.HEURE_PROP, 
                                   oldValue, newValue);
    }
    
    /**
     * Met à jour la valeur des heures
     */
    private void mettreAJourHeures(int valeur) {
        setHeures(valeur);
    }

    /**
     * Récupère les dixièmes de seconde actuels
     */
    @Override
    public int getDixiemeDeSeconde() { 
        return dixiemeDeSeconde; 
    }
    
    /**
     * Récupère l'heure actuelle
     */
    @Override
    public int getHeures() { 
        return heures; 
    }
    
    /**
     * Récupère les minutes actuelles
     */
    @Override
    public int getMinutes() { 
        return minutes; 
    }
    
    /**
     * Récupère les secondes actuelles
     */
    @Override
    public int getSecondes() { 
        return secondes; 
    }
    
    /**
     * Vérifie si le service est actif
     */
    public boolean estActif() {
        return serviceActif;
    }
    
    /**
     * Active le service de chronométrage
     */
    public void activer() {
        this.serviceActif = true;
    }
    
    /**
     * Désactive le service de chronométrage
     */
    public void desactiver() {
        this.serviceActif = false;
    }
    
    /**
     * Obtient le timestamp de la dernière mise à jour
     */
    public long getDerniereMiseAJour() {
        return derniereMiseAJour;
    }
    
    /**
     * Arrête complètement le service et libère les ressources
     */
    public void arreter() {
        if (internalTimer != null) {
            internalTimer.cancel();
            internalTimer = null;
        }
        serviceActif = false;
    }
}