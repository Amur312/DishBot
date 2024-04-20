<#import "../../common/home.ftlh" as h>

<@h.home "Редактирование категории">

  <#if category??>
    <form method="post" action="categories/update">
      <div class="form-group row">
        <label class="col-sm-2 col-form-label">ID:</label>
        <div class="col-sm-10">
          <input class="form-control" type="number" name="id" value="${category.id?c}" readonly>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Название:</label>
        <div class="col-sm-10">
          <input class="form-control" name="name" value="${category.name!}">
          <#if nameError??><div class="alert alert-danger">${nameError}</div></#if>
        </div>
      </div>

      <input type="hidden" name="_csrf" value="${_csrf.token}">
      <div class="form-group row">
        <div class="col-sm-10">
          <button class="btn btn-success" type="submit">Сохранить</button>
        </div>
      </div>
    </form>

    <form method="get" action="categories">
      <button class="btn btn-secondary" type="submit">Назад</button>
    </form>

    <form method="post" action="categories/delete">
      <input type="hidden" name="id" value="${category.id?c}">
      <input type="hidden" name="_csrf" value="${_csrf.token}">
      <div class="form-group row">
        <div class="col-sm-10">
          <button class="btn btn-danger" type="submit">Удалить</button>
        </div>
      </div>
    </form>

  <#else>
    <div class="alert alert-danger" role="alert">
      <h4 class="alert-heading">Ошибка</h4>
      <p>Категория не существует!</p>
    </div>
  </#if>

</@h.home>
