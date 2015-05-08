/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aukcje;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tomasz
 */
@ManagedBean(name = "Auth")
@SessionScoped
public class Auth {
    private Long id;
    private Long idWybranejKategorii;
    private Long idNadkategorii;
    private String login;
    private String haslo;
    private String imie;
    private String nazwisko;
    private Boolean isLogged;
    private Boolean mozeLicytowac;
    private Boolean admin;
    
    @PersistenceContext(name = "SerwisSzybkichAukcjiPU")
    EntityManager em;
    
    /**
     * Creates a new instance of Auth
     */
    public Auth() {
        login = "";
        haslo = "";
        isLogged = false;
        idWybranejKategorii = 351L;
        idNadkategorii = 0L;
        mozeLicytowac = false;
        admin = false;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getHaslo() {
        return this.haslo;
    }
    
    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }
    
    public void setImie(String imie) {
        this.imie = imie;
    }
    
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
    
    public String setIdWybranejKategroii(Long id) {
        this.idWybranejKategorii = id;
        this.idNadkategorii = pobierzKategoriePoId().getIdNadkategorii();
        return "AukcjeAll";
    }
    
    public String setIdWybranejKategroii(Long id, Long idN) {
        this.idWybranejKategorii = id;
        this.idNadkategorii = idN;
        return "AukcjeAll";
    }
    
    public Long getIdWybranejKategorii() {
        return this.idWybranejKategorii;
    }
    
    public void setIdNadkategorii(Long id) {
        this.idNadkategorii = id;
    }
    
    public Long getIdNadkategorii() {
        return this.idNadkategorii;
    }
    
    public Boolean getIsNotMainCat() {
        return this.idWybranejKategorii != 351L;
    }
    
    public Boolean getIsLogged() {
        return isLogged;
    }
    
    public Boolean getMozeLicytowac() {
        return this.mozeLicytowac;
    }
    
    public Boolean getIsAdmin() {
        return this.isLogged && this.admin;
    }
    
    public String getZalogowanyJako() {
        return "Zalogowany jako: " + imie + " " + nazwisko;
    }
    
    public String getNazwaKat() {
        return "Dostępne aukcje w kategorii " + pobierzKategoriePoId().getNazwa() + ":";
    }
    
    public List<Uzytkownik> pobierzUzytkownikow() {
        return em.createNamedQuery("pobierzUzytkownikow").getResultList();
    }
    
    public Kategoria pobierzKategoriePoId() {
        return (Kategoria)em.createNamedQuery("pobierzKategoriePoId").setParameter("catId", this.idWybranejKategorii).getSingleResult();
    }
    
    public List<Kategoria> getPobierzPodkategorie() {
        return em.createNamedQuery("pobierzKategoriePoIdNad").setParameter("catId", this.idWybranejKategorii).getResultList();
    }
    
    public String checkLogin() {
        Uzytkownik tmpUser = new Uzytkownik();
        List<Uzytkownik> wszyscyUzytkownicy = pobierzUzytkownikow();
        tmpUser.setEmail(login);
        tmpUser.setHaslo(haslo);
        int index = wszyscyUzytkownicy.indexOf(tmpUser);
        if(index > -1)
        {
            tmpUser = wszyscyUzytkownicy.get(index);
            imie = tmpUser.getImie();
            nazwisko = tmpUser.getNazwisko();
            id = tmpUser.getId();
            isLogged = true;
            mozeLicytowac = tmpUser.getMozeLicytowac();
            admin = tmpUser.getAdmin();
            if(admin) {
                return "admin/adminPanel?faces-redirect=true";
            }
            else {
                return "user/userPanel?faces-redirect=true";
            }
        }
        else {
            login = "";
            haslo = "";
            return "badLogin";
        }
    }
    
    public String logout() {
        isLogged = false;
        login = "";
        haslo = "";
        idWybranejKategorii = 351L;
        id = 0L;
        idNadkategorii = 0L;
        mozeLicytowac = false;
        admin = false;
        return "/faces/index?faces-redirect=true";
    }
}
