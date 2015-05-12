/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aukcje;

import java.util.ArrayList;
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
    private List<Kategoria> nazwyKategorii;
    private List<Uzytkownik> nazwyUzytkownikow;
    private List<Aukcja> aktualneAukcje;
    private Uzytkownik uzytkownikPrywatny;
    private Aukcja aukcjaPrywatna;
    private Kategoria kategoriaPrywatna;

    public Zarzadzaj() {
        // UWAGA: Kolejność wykonywania DI - serwer aplikacyjny i JSF
        // Korzystając z DI dla EM i korzystając z niego w konstruktorze nie można skorzystać z DI poprzez
        // faces-config.xml
        aktualneAukcje = new ArrayList<Aukcja>();
    }

    public DataModel getKategorie()
    {
        kategorieDM.setWrappedData(pobierzKategorie());
        this.nazwyKategorii = pobierzKategorie();
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
    
    public Kategoria getKategoriaPrywatna() {
        return this.kategoriaPrywatna;
    }
    
    public String modyfikujKategorie() {
        wybory.setIdKategoriiDoModyfikacji(((Kategoria) kategorieDM.getRowData()).getId());
        return "ModyfikujKategorie?faces-redirect=true";
    }
    
    public Boolean zaladujKategorie() {
        kategoriaPrywatna = (Kategoria)em.createNamedQuery("pobierzKategoriePoId").setParameter("catId", wybory.getIdKategoriiDoModyfikacji()).getSingleResult();
        return true;
    }
    
    public String zatwierdzModyfikacjeKategorii() {
        try
        {
            tx.begin();
            em.merge(this.kategoriaPrywatna);
            tx.commit();
        }
        catch (Exception e)
        {
        }
        wybory.setIdKategoriiDoModyfikacji(0L);
        return "Kategorie?faces-redirect=true";
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
        Long idUsera = 0L;
        try
        {
            tx.begin();
            // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
            Uzytkownik uzytkownik = (Uzytkownik) em.merge(uzytkownicyDM.getRowData());
            idUsera = uzytkownik.getId();
            em.remove(uzytkownik);
            tx.commit();
        }
        catch (Exception e)
        {
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
        }
        for(Aukcja aukcja : this.pobierzAukcjeUzytkownika(idUsera)) {
            try
            {
                tx.begin();
                // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
                Aukcja auk = (Aukcja) em.merge(aukcja);
                em.remove(auk);
                tx.commit();
            }
            catch (Exception e)
            {
                // zignoruj tymczasowo jedynie dla uproszczenia przykładu
            }
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
            else {
                return "NieLapanyBlad";
            }
        }
        getUzytkownik().reset();
        if(auth.getIsLogged()) {
            return "Uzytkownicy?faces-redirect=true";
        }
        else {
            wybory.setRenderujKontoUtworzone(true);
            return "/index?faces-redirect=true";
        }
    }
    
    public Uzytkownik getUzytkownikPrywatny() {
        return this.uzytkownikPrywatny;
    }
    
    public String modyfikujUzytkownika() {
        wybory.setIdWybranegoUzytkownika( ((Uzytkownik) uzytkownicyDM.getRowData()).getId());
        wybory.setModyfikacjaSiebie(false);
        return "/user/userModify?faces-redirect=true";
    }
    
    public String modyfikujSiebie() {
        wybory.setIdWybranegoUzytkownika(auth.getId());
        wybory.setModyfikacjaSiebie(true);
        return "/user/userModify?faces-redirect=true";
    }
    
    public Boolean zaladujUzytkownika() {
        uzytkownikPrywatny = (Uzytkownik)em.createNamedQuery("pobierzUzytkownikaPoId").setParameter("userId", wybory.getIdWybranegoUzytkownika()).getSingleResult();
        return true;
    }
    
    public String zatwierdzModyfikacjeUzytkownika() {
        try
        {
            tx.begin();
            if(!auth.getIsAdmin()) {
                if(this.uzytkownikPrywatny.getAdres().isEmpty()) {
                    this.uzytkownikPrywatny.setMozeLicytowac(false);
                }
                else {
                    this.uzytkownikPrywatny.setMozeLicytowac(true);
                }
            }
            if(wybory.getModyfikacjaSiebie()) {
                auth.setMozeLicytowac(this.uzytkownikPrywatny.getMozeLicytowac());
            }
            em.merge(this.uzytkownikPrywatny);
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
        wybory.setIdWybranegoUzytkownika(0L);
        if(wybory.getModyfikacjaSiebie()) {
            return "/user/userPanel?faces-redirect=true";
        }
        else {
            return "/admin/Uzytkownicy?faces-redirect=true";
        }
    }
    
    public String anulujModyfikacjeUzytkownika() {
        wybory.setIdWybranegoUzytkownika(0L);
        if(wybory.getModyfikacjaSiebie()) {
            return "/user/userPanel?faces-redirect=true";
        }
        else {
            return "/admin/Uzytkownicy?faces-redirect=true";
        }
    }
    
    public DataModel getAukcje()
    {
        aukcjeDM.setWrappedData(pobierzAukcje());
        this.nazwyKategorii = pobierzKategorie();
        this.nazwyUzytkownikow = pobierzUzytkownikow();
        return aukcjeDM;
    }
    
    public DataModel getAukcjeUzytkownika()
    {
        aukcjeDM.setWrappedData(pobierzAukcjeUzytkownika());
        this.nazwyKategorii = pobierzKategorie();
        this.nazwyUzytkownikow = pobierzUzytkownikow();
        return aukcjeDM;
    }
    
    public DataModel getAukcjeWKategorii()
    {
        aukcjeDM.setWrappedData(pobierzAukcjeWKategorii());
        this.nazwyKategorii = pobierzKategorie();
        this.nazwyUzytkownikow = pobierzUzytkownikow();
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
    
    public List<Aukcja> pobierzAukcjeUzytkownika(Long id) {
        return em.createNamedQuery("pobierzAukcjeUzytkownika").setParameter("userId", id).getResultList();
    }
    
    public List<Aukcja> pobierzAukcjeWKategorii() {    
        return em.createNamedQuery("pobierzAukcjeWKategorii").setParameter("catId", wybory.getIdWybranejKategorii()).getResultList();
    }
    
    public Aukcja pobierzAukcjePoId(Long id) {      
        return (Aukcja)em.createNamedQuery("pobierzAukcjePoId").setParameter("aukcjaId", id).getSingleResult();
    }
    
    public int getAukcjeSize()
    {
        return aukcjeDM.getRowCount();
    }

    public String licytuj() {
        try
        {
            tx.begin();
            // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
            Aukcja aukcja = em.merge(pobierzAukcjePoId(((Aukcja)aukcjeDM.getRowData()).getId())); // siegamy bezposrednio do bazy
            int kwota = getKwota();
            if(aukcja.getCena() < kwota)
            {
                aukcja.setCena(kwota);
                aukcja.setIdZwyciezcy(auth.getId());
            }
            tx.commit();
        }
        catch (Exception e)
        {
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
        }
        return "AukcjeAll?faces-redirect=true";
    }
    
    public String kup() {
        try
        {
            tx.begin();
            // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
            Aukcja aukcja = em.merge(pobierzAukcjePoId(((Aukcja)aukcjeDM.getRowData()).getId())); // siegamy bezposrednio do bazy
            
            if(aukcja.getNieZakonczona())
            {
                aukcja.zakoncz();
                aukcja.setIdZwyciezcy(auth.getId());
            }
            tx.commit();
        }
        catch (Exception e)
        {
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
        }
        return "AukcjeAll?faces-redirect=true";
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
    
    public Aukcja getAukcjaPrywatna() {
        return this.aukcjaPrywatna;
    }
    
    public void setAukcjaPrywatna(Aukcja aukcja) {
        this.aukcjaPrywatna = aukcja;
    }
    
    public String modyfikujAukcje() {
        wybory.setIdAukcjiDoModyfikacji(((Aukcja) aukcjeDM.getRowData()).getId());
        return "/user/ModyfikujAukcje?faces-redirect=true";
    }
    
    public Boolean zaladujAukcje() {
        aukcjaPrywatna = (Aukcja)em.createNamedQuery("pobierzAukcjePoId").setParameter("aukcjaId", wybory.getIdAukcjiDoModyfikacji()).getSingleResult();
        return true;
    }
    
    public Boolean zaladujNowaAukcjeWKategorii() {
        aukcjaPrywatna = new Aukcja();
        aukcjaPrywatna.setIdKategorii(wybory.getIdWybranejKategorii());
        aukcjaPrywatna.setIdWystawiajacego(auth.getId());
        return true;
    }
    
    public String dodajAukcjeWKategorii() {
        try
        {
            tx.begin();
            em.merge(this.aukcjaPrywatna);
            tx.commit();
        }
        catch (Exception e)
        {
        }
        return "AukcjeAll?faces-redirect=true";
    }
    
    public String zatwierdzModyfikacjeAukcji() {
        try
        {
            tx.begin();
            em.merge(this.aukcjaPrywatna);
            tx.commit();
        }
        catch (Exception e)
        {
        }
        wybory.setIdAukcjiDoModyfikacji(0L);
        if(auth.getIsAdmin()) {
            return "/admin/Aukcje?faces-redirect=true";
        }
        else {
            return "/user/userPanel?faces-redirect=true";
        }
    }
    
    public String anulujModyfikacjeAukcji() {
        wybory.setIdAukcjiDoModyfikacji(0L);
        if(auth.getIsAdmin()) {
            return "/admin/Aukcje?faces-redirect=true";
        }
        else {
            return "/user/userPanel?faces-redirect=true";
        }
    }
    
    public String getNazwaKatPoId(Long id) {
        for(Kategoria kategoria : this.nazwyKategorii) {
            if(id.equals(kategoria.getId()))
                return kategoria.getNazwa();
        }
        return "";
    }
    
    public String getEmailPoId(Long id) {
        for(Uzytkownik uzytkownik : this.nazwyUzytkownikow) {
            if(uzytkownik.getId().equals(id))
                return uzytkownik.getEmail();
        }
        return "";
    }
    
    @ManagedProperty("#{Auth}")
    private Auth auth; // +setter (no getter!)
    
    @ManagedProperty("#{Wybory}")
    private Wybory wybory;
    
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
    
    public void setWybory(Wybory wybory) {
        this.wybory = wybory;
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
    
    public  void setKwota(int kwota) {
        int index = aukcjeDM.getRowIndex();
        this.aktualneAukcje = (List<Aukcja>)aukcjeDM.getWrappedData();
 
        this.aktualneAukcje.get(index).setCena(kwota);
    }
    
    public int getKwota() {
        int index = aukcjeDM.getRowIndex();
        if(this.aktualneAukcje.size() > index) {
            return this.aktualneAukcje.get(index).getCena();
        }
        else {
            return 0;
        }
    }
}
