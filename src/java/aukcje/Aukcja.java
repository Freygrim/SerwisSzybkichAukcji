/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aukcje;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Tomasz
 */
@Entity
@Table(name = "AUKCJE")
@NamedQueries({
@NamedQuery(name = "pobierzAukcje", query = "SELECT p FROM Aukcja p GROUP BY p.id"),
@NamedQuery(name = "pobierzAukcjeUzytkownika", query = "SELECT p FROM Aukcja p WHERE p.idWystawiajacego = :userId GROUP BY p.id"),
@NamedQuery(name = "pobierzAukcjeWKategorii", query = "SELECT p FROM Aukcja p WHERE p.idKategorii = :catId GROUP BY p.id"),
@NamedQuery(name = "pobierzAukcjePoId", query = "SELECT p FROM Aukcja p WHERE p.id = :aukcjaId")
})
@ManagedBean(name="Aukcja")
@RequestScoped
public class Aukcja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Long idKategorii;
    @NotNull
    private Long idWystawiajacego;
    @NotNull
    private String nazwa;
    @NotNull
    private String opis;
    private String zdjecieLink;
    @NotNull
    private int cena;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataZakonczenia;
    private Long idZwyciezcy;
    
    @NotNull
    private String status;

    public Aukcja() {
        this.dataZakonczenia = new Date();
        this.status = "Trwająca";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getIdKategorii() {
        return idKategorii;
    }
    
    public void setIdKategorii(Long idKategorii) {
        this.idKategorii = idKategorii;
    }
    
    public Long getIdWystawiajacego() {
        return idWystawiajacego;
    }

    public void setIdWystawiajacego(Long idWystawiajacego) {
        this.idWystawiajacego = idWystawiajacego;
    }
    
    public String getNazwa() {
        return nazwa;
    }
    
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
    
    public String getOpis() {
        return opis;
    }
    
    public void setOpis(String opis) {
        this.opis = opis;
    }
    
    public String getZdjecieLink() {
        return zdjecieLink;
    }
    
    public void setZdjecieLink(String zdjecieLink) {
        this.zdjecieLink = zdjecieLink;
    }
    
    public int getCena() {
        return this.cena;
    }
    
    public void setCena(int cena) {
        this.cena = cena;
    }
    
    public Date getDataZakonczenia() {
        return this.dataZakonczenia;
    }
    
    public void setDataZakonczenia(Date dataZakonczenia) {
        this.dataZakonczenia = dataZakonczenia;
    }
    
    public String getPrzekazanaData() {
        return dataZakonczenia.toString();
    }
    
    public Long getIdZwyciezcy() {
        return idZwyciezcy;
    }

    public void setIdZwyciezcy(Long idZwyciezcy) {
        this.idZwyciezcy = idZwyciezcy;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void zakoncz() {
        status = "Zakończona";
    }
    
    public void reset() {
        idKategorii = 0L;
        idWystawiajacego = 0L;
        nazwa = "";
        opis = "";
        zdjecieLink = "";
        cena = 0;
        dataZakonczenia = new Date();
        idZwyciezcy = 0L;
        status = "Trwająca";
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Uzytkownicy sa tacy sami jezeli ich emaile sie zgadzaja
        if (!(object instanceof Aukcja)) {
            return false;
        }
        Aukcja other = (Aukcja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aukcje.Aukcja[ id=" + id + " ]";
    }
    
}
