/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Tomasz
 */

@Entity
@Table(name = "PRACOWNICY")
@NamedQuery(name = "pobierzPracownikow", query = "SELECT p FROM Pracownik p ORDER BY p.nazwisko, p.imie")
@ManagedBean(name="Pracownik")
@RequestScoped
public class Pracownik implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imie;
    private String nazwisko;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getImie()
    {
        return imie;
    }

    public void setImie(String imie)
    {
        this.imie = imie;
    }
    
    public String getNazwisko()
    {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko)
    {
        this.nazwisko = nazwisko;
    }
    
    //Auto generowane i w sumie nie potrzebne ale dobre na wzor: 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pracownik)) {
            return false;
        }
        Pracownik other = (Pracownik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.Pracownik[ id=" + id + " ]";
    }
    
}
