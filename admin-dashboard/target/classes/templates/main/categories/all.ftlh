<#import "../../common/home.ftlh" as h>

<@h.home "Список категорий">

  <center class="mb-4">
    <a class="btn btn-sm btn-info" href="categories/add">Добавить категорию</a>
  </center>

  <#macro displayCategories categories>
    <#list categories as category>
      <tr>
        <td>${category.id?c}</td>
        <td><#if category.parentId??>(Подкатегория) </#if>${category.name!}</td>
        <td>
          <a class="btn btn-xs btn-info" href="categories/edit/${category.id?c}">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </a>
        </td>
      </tr>
      <#if category.subCategories?? && category.subCategories?size > 0>
        <tr>
          <td colspan="3">
            <table class="table">
              <@displayCategories category.subCategories/>
            </table>
          </td>
        </tr>
      </#if>
    </#list>
  </#macro>

  <table id="categories" class="table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
    <tr>
      <th>ID</th>
      <th>Название</th>
      <th>Действие</th>
    </tr>
    </thead>
    <tbody>
    <@displayCategories categories/>
    </tbody>
  </table>

</@h.home>
