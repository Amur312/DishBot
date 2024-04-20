<#import "../../common/home.ftlh" as h>

<@h.home "Редактирование клиента">

  <#if client??>
    <form method="post" action="clients/update">
      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Id:</label>
        <div class="col-sm-10">
          <input class="form-control" type="number" name="id" value="${client.id?c}" readonly>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Chat id:</label>
        <div class="col-sm-10">
          <input class="form-control" type="number" name="chatId" value="${client.chatId?c}" readonly>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Имя:</label>
        <div class="col-sm-10">
          <input class="form-control" name="firstName" value="${client.firstName!}">
          <#if firstNameError??><div class="text-warning">${firstNameError}</div></#if>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Фамилия:</label>
        <div class="col-sm-10">
          <input class="form-control" name="lastName" value="${client.lastName!}">
          <#if lastNameError??><div class="text-warning">${lastNameError}</div></#if>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Номер телефона:</label>
        <div class="col-sm-10">
          <input class="form-control" name="phoneNumber" value="${client.phoneNumber!}">
          <#if phoneNumberError??><div class="text-warning">${phoneNumberError}</div></#if>
        </div>
      </div>
      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Адрес:</label>
        <div class="col-sm-10">
          <input class="form-control" name="address" value="${client.address!}">
          <#if addressError??><div class="text-warning">${addressError}</div></#if>
        </div>
      </div>

      <input type="hidden" name="_csrf" value="${_csrf.token}">
      <button class="btn btn-success" type="submit">Обновить</button>
    </form>

    <form method="get" action="orders">
      <button class="btn btn-info" type="submit">Назад</button>
    </form>

  <#else>
    <div class="alert alert-danger" role="alert">
      <h4 class="alert-heading">Ошибка</h4>
      <hr>Такой клиент не существует!
    </div>
  </#if>

</@h.home>
