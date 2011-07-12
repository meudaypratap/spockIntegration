<%@ page import="spockintegration.Transaction" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'transaction.label', default: 'Transaction')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]"/></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></span>
</div>

<div class="body">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
        <table>
            <tbody>

            <tr class="prop">
                <td valign="top" class="name"><g:message code="transaction.id.label" default="Id"/></td>

                <td valign="top" class="value">${fieldValue(bean: transactionInstance, field: "id")}</td>

            </tr>

            <tr class="prop">
                <td valign="top" class="name"><g:message code="transaction.amount.label" default="Amount"/></td>

                <td valign="top" class="value">${fieldValue(bean: transactionInstance, field: "amount")}</td>

            </tr>

            <tr class="prop">
                <td valign="top" class="name"><g:message code="transaction.type.label" default="Type"/></td>

                <td valign="top" class="value">${transactionInstance?.type?.encodeAsHTML()}</td>

            </tr>

            <tr class="prop">
                <td valign="top" class="name"><g:message code="transaction.account.label" default="Account"/></td>

                <td valign="top" class="value"><g:link controller="account" action="show" id="${transactionInstance?.account?.id}">${fieldValue(bean: transactionInstance, field: "account")}</g:link></td>

            </tr>

            </tbody>
        </table>
    </div>

    <div class="buttons">
        <g:form>
            <g:hiddenField name="id" value="${transactionInstance?.id}"/>
            <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>
            <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                                 onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
        </g:form>
    </div>
</div>
</body>
</html>
