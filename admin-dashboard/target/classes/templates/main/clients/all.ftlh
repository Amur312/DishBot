<#import "../../common/home.ftlh" as h>
<#import "../../common/table.ftlh" as t>

<@h.home "Список клиентов">

  <@t.table "clients" />
  <table id="clients" class="table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
    <tr>
      <th>Id</th>
      <th>Имя</th>
      <th>Фамилия</th>
      <th>Номер телефона</th>
      <th>Адрес</th>
      <th>Действие</th>
    </tr>
    </thead>
    <tbody>
    <#list clients as client>
      <tr>
        <td>${client.id?c}</td>
        <td>${client.firstName!}</td>
        <td>${client.lastName!}</td>
        <td>${client.phoneNumber!}</td>
        <td>${client.address!}</td>
          <td>
            <a class="btn btn-xs btn-info" href="clients/edit/${client.id?c}">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </a>
          </td>
      </tr>
    </#list>
    </tbody>
  </table>

</@h.home>
