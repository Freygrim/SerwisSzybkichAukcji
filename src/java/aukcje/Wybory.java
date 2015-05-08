/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aukcje;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tomasz
 */
@ManagedBean(name = "Wybory")
@SessionScoped
public class Wybory {

    private Long idWybranejKategorii;
    private Long idNadkategorii;
    private Long idWybranegoUzytkownika;
    private Boolean modyfikacjaSiebie;
    
    @PersistenceContext(name = "SerwisSzybkichAukcjiPU")
    EntityManager em;
    
    /**
     * Creates a new instance of Auth
     */
    public Wybory() {
        idWybranejKategorii = 351L;
        idNadkategorii = 0L;
        idWybranegoUzytkownika = 0L;
        modyfikacjaSiebie = false;
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
    
    public String getNazwaKat() {
        return "Dostępne aukcje w kategorii " + pobierzKategoriePoId().getNazwa() + ":";
    }
    
    public Kategoria pobierzKategoriePoId() {
        return (Kategoria)em.createNamedQuery("pobierzKategoriePoId").setParameter("catId", this.idWybranejKategorii).getSingleResult();
    }
    
    public List<Kategoria> getPobierzPodkategorie() {
        return em.createNamedQuery("pobierzKategoriePoIdNad").setParameter("catId", this.idWybranejKategorii).getResultList();
    }
    
    public Long getIdWybranegoUzytkownika() {
        return this.idWybranegoUzytkownika;
    }
    
    public void setIdWybranegoUzytkownika(Long idWybranegoUzytkownika) {
        this.idWybranegoUzytkownika = idWybranegoUzytkownika;
    }
    
    public Boolean getModyfikacjaSiebie() {
        return this.modyfikacjaSiebie;
    }
    
    public void setModyfikacjaSiebie(Boolean mod) {
        this.modyfikacjaSiebie = mod;
    }
}
