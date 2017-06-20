<%--
2. Спроектировать базу «Квартиры». Каждая запись
в базе содержит данные о квартире (район,
адрес, площадь, кол. комнат, цена). Сделать
возможность выборки квартир из списка по
параметрам.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Apartment</title>
    <style>
      td{
        width: 10em;
      }
    </style>
  </head>
  <body>
  <br/>
  <form action="/flat_search" method="POST">

  <table border="1">
    <tr>
      <td>District</td>
      <td>Address</td>
      <td>Area, m2</td>
      <td>Number of rooms</td>
      <td>Price, thous. of USD</td>
    </tr>
    <tr>
      <td>
        <select name="district">
          <option value=""></option>
        <c:forEach items="${districtList}" var="district">
          <option value="<c:out value="${district}" />"><c:out value="${district}"/></option>
        </c:forEach>
        </select>
      </td>
      <td>
        <select name="address">
          <option value=""></option>
          <c:forEach items="${addressList}" var="address">
            <option value="<c:out value="${address}" />"><c:out value="${address}" /></option>
          </c:forEach>
        </select>
      </td>
      <td>
        <select name="area">
          <option value=""></option>
          <c:forEach items="${areaList}" var="area">
            <option value="<c:out value="${area}" />"><c:out value="${area}" /></option>
          </c:forEach>
        </select>
      </td>
      <td>
        <select name="room_number">
          <option value=""></option>
          <c:forEach items="${roomNumberList}" var="roomNumber">
            <option value="<c:out value="${roomNumber}" />"><c:out value="${roomNumber}" /></option>
          </c:forEach>
        </select>
      </td>
      <td>
        <select name="price">
          <option value=""></option>
          <c:forEach items="${priceList}" var="price">
            <option value=" <c:out value="${price}" />"><c:out value="${price}" /></option>
          </c:forEach>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan="5">
        <input type="submit" value="Search"/>
      </td>
    </tr>
  </table>
  </form>
<br/>
  <h3>Results of search:</h3>
  <c:choose>
    <c:when test="${flatList ne null and not empty flatList}">

    <table border="1">
      <tr>
        <th>District</th>
        <th>Address</th>
        <th>Area, m2</th>
        <th>Number of rooms</th>
        <th>Price, thous. of USD</th>
      </tr>
      <c:forEach items="${flatList}" var="flat">
        <tr>
          <td><c:out value="${flat.district}"/></td>
          <td><c:out value="${flat.address}"/></td>
          <td><c:out value="${flat.area}"/></td>
          <td><c:out value="${flat.roomNumber}"/></td>
          <td><c:out value="${flat.price/100000}"/></td>
        </tr>
      </c:forEach>
    </table>

    </c:when>
    <c:otherwise>
      NONE
    </c:otherwise>
  </c:choose>

  </body>
</html>
