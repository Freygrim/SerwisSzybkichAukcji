/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aukcje;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Tomasz
 */
@Entity
@Table(name = "UZYTKOWNICY",
       uniqueConstraints = @UniqueConstraint(columnNames={"email"}))
@NamedQuery(name = "pobierzUzytkownikow", query = "SELECT p FROM Uzytkownik p GROUP BY p.id")
@ManagedBean(name="Uzytkownik")
@RequestScoped
public class Uzytkownik implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imie;
    private String nazwisko;
    private String email;
    private String adres;
    private String haslo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getImie() {
        return imie;
    }
    
    public void setImie(String imie) {
        this.imie = imie;
    }
    
    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAdres() {
        return adres;
    }
    
    public void setAdres(String adres) {
        this.adres = adres;
    }
    
    public String getHaslo() {
        return haslo;
    }
    
    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }
    
    public void reset() {
        imie = "";
        nazwisko = "";
        email = "";
        adres = "";
        haslo = "";
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
        if (!(object instanceof Uzytkownik)) {
            return false;
        }
        Uzytkownik other = (Uzytkownik) object;
        if (this.email == null ||
            other.email == null ||
            (this.email != null && !this.email.equals(other.email)) ||
            this.haslo == null ||
            other.haslo == null ||
            (this.haslo != null && !this.haslo.equals(other.haslo))
            )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aukcje.Uzytkownik[ id=" + id + " ]";
    }
    
}
