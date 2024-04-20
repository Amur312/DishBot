<#import "../../common/home.ftlh" as h>

<@h.home "Добавление категории">

  <form method="post" action="categories/create">
    <div class="form-group row">
      <label class="col-sm-2 col-form-label">Название:</label>
      <div class="col-sm-10">
        <input type="text" class="form-control" name="name" placeholder="Введите название категории" value="<#if category??>${category.name!}</#if>">
        <#if nameError??><div class="alert alert-danger">${nameError}</div></#if>
      </div>
    </div>

    <div class="form-group row">
      <label for="parentCategory" class="col-sm-2 col-form-label">Родительская категория:</label>
      <div class="col-sm-10">
        <select class="form-control" name="parent_id" id="parentCategory">
          <option value="">-- Без родительской категории --</option>
          <#list parentCategories as parentCategory>
            <option value="${parentCategory.id}">${parentCategory.name}</option>
          </#list>
        </select>

        <#if parentCategoryError??><div class="alert alert-danger">${parentCategoryError}</div></#if>
      </div>
    </div>

    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <div class="form-group row">
      <div class="col-sm-10">
        <button class="btn btn-success" type="submit">Создать</button>
      </div>
    </div>
  </form>

  <form method="get" action="/categories">
    <button class="btn btn-secondary" type="submit">Назад</button>
  </form>

</@h.home>
