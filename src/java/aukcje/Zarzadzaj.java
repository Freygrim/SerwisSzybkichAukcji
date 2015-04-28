/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aukcje;

import java.util.List;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Tomasz
 */
@ManagedBean(name="Zarzadzaj")
@RequestScoped
public class Zarzadzaj
{
    @PersistenceContext(name = "SerwisSzybkichAukcjiPU")
    EntityManager em;

    @Resource
    UserTransaction tx;

    private DataModel kategorieDM = new ListDataModel();
    private DataModel uzytkownicyDM = new ListDataModel();
    private DataModel aukcjeDM = new ListDataModel();

    public Zarzadzaj() {
        // UWAGA: Kolejność wykonywania DI - serwer aplikacyjny i JSF
        // Korzystając z DI dla EM i korzystając z niego w konstruktorze nie można skorzystać z DI poprzez
        // faces-config.xml
    }

    public DataModel getKategorie()
    {
        kategorieDM.setWrappedData(pobierzKategorie());
        return kategorieDM;
    }

    public void setKategorie(DataModel kategorieDM)
    {
        this.kategorieDM = kategorieDM;
    }
    
        protected List<Kategoria> pobierzKategorie()
    {
        return em.createNamedQuery("pobierzKategorie").getResultList();
    }

    public int getKategorieSize()
    {
        return kategorieDM.getRowCount();
    }
    
    public String usunKategorie()
    {
        try
        {
            tx.begin();
            // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
            Kategoria kategoria = (Kategoria) em.merge(kategorieDM.getRowData());
            em.remove(kategoria);
            tx.commit();
        }
        catch (Exception e)
        {
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
        }
        return "Kategorie?faces-redirect=true";
    }

    public String dodajKategorie()
    {
        // UWAGA:
        // Niepotrzebnie musimy opiekować się transakcją - najlepiej wynieść funkcjonalność do EJB z odpowiednią
        // deklaracją transakcji
        try
        {
            tx.begin();
            em.merge(getKategoria());
            tx.commit();
        }
        catch (Exception e)
        {
            // tutaj pojawia sie wyjatek jezeli nastepuje proba dodania tej samej kategori
            if(javax.transaction.RollbackException.class == e.getClass())
            {
                return "KatDuplikatError";
            }
        }
        getKategoria().setNazwa("");
        return "Kategorie?faces-redirect=true";
    }
    
    public DataModel getUzytkownicy()
    {
        uzytkownicyDM.setWrappedData(pobierzUzytkownikow());
        return uzytkownicyDM;
    }
    
    public void setUzytkownicy(DataModel uzytkownicyDM)
    {
        this.uzytkownicyDM = uzytkownicyDM;
    }
    
    public List<Uzytkownik> pobierzUzytkownikow() {
        return em.createNamedQuery("pobierzUzytkownikow").getResultList();
    }
    
    public int getUzytkownicySize()
    {
        return uzytkownicyDM.getRowCount();
    }

    public String usunUzytkownika()
    {
        try
        {
            tx.begin();
            // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
            Uzytkownik uzytkownik = (Uzytkownik) em.merge(uzytkownicyDM.getRowData());
            em.remove(uzytkownik);
            tx.commit();
        }
        catch (Exception e)
        {
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
        }
        return "Uzytkownicy?faces-redirect=true";
    }
    
    public String dodajUzytkownika()
    {
        try
        {
            tx.begin();
            em.merge(getUzytkownik());
            tx.commit();
        }
        catch (Exception e)
        {
            // tutaj pojawia sie wyjatek jezeli nastepuje proba dodania tego samego uzytkownika
            if(javax.transaction.RollbackException.class == e.getClass())
            {
                return "UzytDuplikatError";
            }
        }
        getUzytkownik().reset();
        return "Uzytkownicy?faces-redirect=true";
    }
    
    public DataModel getAukcje()
    {
        aukcjeDM.setWrappedData(pobierzAukcje());
        return aukcjeDM;
    }
    
    public DataModel getAukcjeUzytkownika()
    {
        aukcjeDM.setWrappedData(pobierzAukcjeUzytkownika());
        return aukcjeDM;
    }
    
    public void setAukcje(DataModel aukcjeDM)
    {
        this.aukcjeDM = aukcjeDM;
    }
    
    public List<Aukcja> pobierzAukcje() {
        return em.createNamedQuery("pobierzAukcje").getResultList();
    }
    
    public List<Aukcja> pobierzAukcjeUzytkownika() {
        
        return em.createNamedQuery("pobierzAukcjeUzytkownika").setParameter("userId", auth.getId()).getResultList();
    }
    
    public int getAukcjeSize()
    {
        return aukcjeDM.getRowCount();
    }

    public String usunAukcje()
    {
        try
        {
            tx.begin();
            // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
            Aukcja aukcja = (Aukcja) em.merge(aukcjeDM.getRowData());
            em.remove(aukcja);
            tx.commit();
        }
        catch (Exception e)
        {
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
        }
        return "Aukcje?faces-redirect=true";
    }
    
    public String dodajAukcje()
    {
        try
        {
            tx.begin();
            em.merge(getAukcja());
            tx.commit();
        }
        catch (Exception e)
        {
            // tutaj pojawia sie wyjatek jezeli nastepuje proba dodania tego samego uzytkownika
            if(javax.transaction.RollbackException.class == e.getClass())
            {
                return "AukcjaDuplikatError";
            }
        }
        getAukcja().reset();
        return "Aukcje?faces-redirect=true";
    }
    @ManagedProperty("#{Auth}")
    private Auth auth; // +setter (no getter!)
    
    // Korzystamy z DI dla JSF - patrz plik faces-config.xml
    @ManagedProperty(value="#{Kategoria}")
    Kategoria kategoria;
    @ManagedProperty(value="#{Uzytkownik}")
    Uzytkownik uzytkownik;
    @ManagedProperty(value="#{Aukcja}")
    Aukcja aukcja;

    public void setAuth(Auth auth) {
        this.auth = auth;
    }
    
    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    public Kategoria getKategoria() {
        return this.kategoria;
    }
    
    public void setUzytkownik(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }
    
    public Uzytkownik getUzytkownik() {
        return this.uzytkownik;
    }
    
    public void setAukcja(Aukcja aukcja) {
        this.aukcja = aukcja;
    }
    
    public Aukcja getAukcja() {
        return this.aukcja;
    }
}
