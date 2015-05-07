/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aukcje;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

/**
 *
 * @author Tomasz
 */
@WebFilter("/faces/*")
public class FiltrPrzedawnionychAukcji implements Filter {

    @PersistenceContext(name = "SerwisSzybkichAukcjiPU")
    EntityManager em;

    @Resource
    UserTransaction tx;
    
    private List<Aukcja> pobierzAukcje() {
        return em.createNamedQuery("pobierzAukcje").getResultList();
    }
    
    private void SprawdzAukcje() {
        Date dzisiaj = new Date();
        List<Aukcja> listaAukcji = pobierzAukcje();

        for(Aukcja ak : listaAukcji) {
            try
            {
                tx.begin();
                // UWAGA: JPA wykonując merge zwróci trwałą encję i tylko ta będzie trwała, a nie przekazywany parametr
                Aukcja aukcja = em.merge(ak);

                if(aukcja.getDataZakonczenia().before(dzisiaj))
                {
                    aukcja.zakoncz();
                }
                tx.commit();
            }
            catch (Exception e)
            {
                // zignoruj tymczasowo jedynie dla uproszczenia przykładu
            }
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {    
        HttpServletRequest req = (HttpServletRequest) request;
        if(req.getRequestURI().endsWith("Aukcje.xhtml") || 
           req.getRequestURI().endsWith("AukcjeAll.xhtml") ||
           req.getRequestURI().endsWith("userPanel.xhtml") ||
           req.getRequestURI().endsWith("adminPanel.xhtml")
          ) {
            SprawdzAukcje();
        }
        chain.doFilter(request, response);
    }

    // You need to override init() and destroy() as well, but they can be kept empty.

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
