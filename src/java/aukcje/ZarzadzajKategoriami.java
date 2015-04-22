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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Tomasz
 */
@ManagedBean(name="ZarzadzajKategoriami")
@RequestScoped
public class ZarzadzajKategoriami
{
@PersistenceContext(name = "SerwisSzybkichAukcjiPU")
    EntityManager em;

    @Resource
    UserTransaction tx;

    private DataModel kategorieDM = new ListDataModel();

    public ZarzadzajKategoriami() {
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

    public int getSize()
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
        return "success";
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
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
            getKategoria().setNazwa(e.getMessage());
        }
        setKategoria(new Kategoria());
        return "success";
    }

    // Korzystamy z DI dla JSF - patrz plik faces-config.xml
    @ManagedProperty(value="#{Kategoria}")
    Kategoria kategoria;

    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    public Kategoria getKategoria() {
        return this.kategoria;
    }
}
