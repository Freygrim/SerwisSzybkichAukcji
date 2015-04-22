<%-- 
    Document   : Kadry
    Created on : 2015-04-16, 21:33:59
    Author     : Tomasz
--%>

<%@ page contentType="text/html; charset=ISO-8859-2"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
    <h:form>
        <h:dataTable var="pracownik" value="#{Kadry.pracownicy}" border="0">
            <f:facet name="header">
                <h:outputText value="Lista pracowników" />
            </f:facet>
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Nazwisko" />
                </f:facet>
                <h:outputText value="#{pracownik.nazwisko}" />
            </h:column>
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Imiê" />
                </f:facet>
                <h:outputText value="#{pracownik.imie}" />
            </h:column>
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Identyfikator" />
                </f:facet>
                <h:outputText value="#{pracownik.id}" style='align:center' />
            </h:column>
            <h:column>
                <h:commandButton value="Zwolnij" action="#{Kadry.zwolnijPracownika}" />
            </h:column>            
            <f:facet name="footer">
                <h:outputText value="Pozycji: #{Kadry.size}" />
            </f:facet>
        </h:dataTable>
        <hr>
        <table border="0">
            <tbody>
                <tr>
                    <td>Imiê</td>
                    <td><h:inputText value="#{Pracownik.imie}" /></td>
                </tr>
                <tr>
                    <td>Nazwisko</td>
                    <td><h:inputText value="#{Pracownik.nazwisko}" /></td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <th colspan="2" align="center"><h:commandButton value="Zatrudnij nowego pracownika" action="#{Kadry.zatrudnijPracownika}"/></th>
                </tr>                
            </tfoot>
        </table>
    </h:form>
</f:view>
