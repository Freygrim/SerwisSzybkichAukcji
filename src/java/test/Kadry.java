/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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
@ManagedBean(name="Kadry")
@RequestScoped
public class Kadry
{
@PersistenceContext(name = "SerwisSzybkichAukcjiPU")
    EntityManager em;

    @Resource
    UserTransaction tx;

    private DataModel pracownicyDM = new ListDataModel();

    public Kadry() {
        // UWAGA: Kolejność wykonywania DI - serwer aplikacyjny i JSF
        // Korzystając z DI dla EM i korzystając z niego w konstruktorze nie można skorzystać z DI poprzez
        // faces-config.xml
    }

    public DataModel getPracownicy()
    {
        pracownicyDM.setWrappedData(pobierzPracownikow());
        return pracownicyDM;
    }

    public void setPracownicy(DataModel pracownicyDM)
    {
        this.pracownicyDM = pracownicyDM;
    }

    protected List<Pracownik> pobierzPracownikow()
    {
        return em.createNamedQuery("pobierzPracownikow").getResultList();
    }

    public int getSize()
    {
        return pracownicyDM.getRowCount();
    }

    public String zwolnijPracownika()
    {
        try
        {
            tx.begin();
            // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
            Pracownik pracownik = (Pracownik) em.merge(pracownicyDM.getRowData());
            em.remove(pracownik);
            tx.commit();
        }
        catch (Exception e)
        {
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
        }
        return "success";
    }

    public String zatrudnijPracownika()
    {
        // UWAGA:
        // Niepotrzebnie musimy opiekować się transakcją - najlepiej wynieść funkcjonalność do EJB z odpowiednią
        // deklaracją transakcji
        try
        {
            tx.begin();
            em.merge(getPracownik());
            tx.commit();
        }
        catch (Exception e)
        {
            // zignoruj tymczasowo jedynie dla uproszczenia przykładu
        }
        return "success";
    }

    // Korzystamy z DI dla JSF - patrz plik faces-config.xml
    @ManagedProperty(value="#{Pracownik}")
    Pracownik pracownik;

    public void setPracownik(Pracownik pracownik) {
        this.pracownik = pracownik;
    }

    public Pracownik getPracownik() {
        return this.pracownik;
    }
}
