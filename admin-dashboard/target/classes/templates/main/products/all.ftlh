<#import "../../common/home.ftlh" as h>
<#import "../../common/table.ftlh" as t>

<@h.home "Список продуктов">

  <center class="mb-4">
    <a class="btn btn-sm btn-info" href="products/add">Добавить продукт</a>
  </center>

  <@t.table "products" />
  <table id="products" class="table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
    <tr>
      <th>Id</th>
      <th>Название</th>
      <th>Категория</th>
      <th>Цена</th>
      <th>Действие</th>
    </tr>
    </thead>
    <tbody>
    <#list products as product>
      <tr>
        <td>${product.id?c}</td>
        <td>${product.name}</td>
        <td><#if product.category??>${product.category.name}<#else>—</#if></td>
        <td>${product.price} $</td>
        <td>
          <a class="btn btn-xs btn-info" href="products/edit/${product.id?c}">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </a>
        </td>
      </tr>
    </#list>
    </tbody>
  </table>

</@h.home>
