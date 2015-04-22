<%-- 
    Document   : Kategorie
    Created on : 2015-04-16, 21:33:59
    Author     : Tomasz
--%>

<%@ page contentType="text/html; charset=ISO-8859-2"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
    <h:form>
        <h:dataTable var="kategoria" value="#{ZarzadzajKategoriami.kategorie}" border="0">
            <f:facet name="header">
                <h:outputText value="Dostêpne kategorie" />
            </f:facet>
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Id" />
                </f:facet>
                <h:outputText value="#{kategoria.id}" />
            </h:column>
            <h:column>
                <f:facet name="header">
                    <h:outputText value="IdNadkategorii" />
                </f:facet>
                <h:outputText value="#{kategoria.idNadkategorii}" />
            </h:column>
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Nazwa" />
                </f:facet>
                <h:outputText value="#{kategoria.nazwa}" style='align:center' />
            </h:column>
            <h:column>
                <h:commandButton value="Usuñ" action="#{ZarzadzajKategoriami.usunKategorie}" />
            </h:column>            
            <f:facet name="footer">
                <h:outputText value="Pozycji: #{ZarzadzajKategoriami.size}" />
            </f:facet>
        </h:dataTable>
        <hr>
        <table border="0">
            <tbody>
                <tr>
                    <td>Id nadkategorii:</td>
                    <td><h:selectOneListbox value="#{Kategoria.idNadkategorii}" size="1">
   			<f:selectItems value="#{ZarzadzajKategoriami.kategorie}" var="kat"
   			itemLabel="#{kat.nazwa}" itemValue="#{kat.id}" />
                        </h:selectOneListbox>
                    </td>
                </tr>
                <tr>
                    <td>Nazwa</td>
                    <td><h:inputText value="#{Kategoria.nazwa}" /></td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <th colspan="2" align="center"><h:commandButton value="Dodaj kategoriê" action="#{ZarzadzajKategoriami.dodajKategorie}"/></th>
                </tr>                
            </tfoot>
        </table>
    </h:form>
</f:view>
