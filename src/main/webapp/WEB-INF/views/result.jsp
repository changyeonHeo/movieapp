<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<%@ page import="org.json.JSONObject, org.json.JSONArray" %>
<%
    JSONObject result = (JSONObject) request.getAttribute("result");
    JSONArray movies = result != null && result.has("Search") ? result.getJSONArray("Search") : new JSONArray();
    request.setAttribute("movies", movies);
%>

<html>
<head>
    <title>검색 결과</title>
    <style>
        body {
            background-color: #121212;
            color: white;
            font-family: Arial, sans-serif;
            text-align: center;
        }
        .movie-grid {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 20px;
            margin-top: 30px;
        }
        .movie {
            background-color: #1f1f1f;
            padding: 10px;
            width: 200px;
            border-radius: 10px;
        }
        .movie img {
            width: 100%;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <h2>검색 결과</h2>
    <div class="movie-grid">
        <c:forEach var="i" begin="0" end="${movies.length() - 1}">
            <%
                JSONObject movie = movies.getJSONObject((Integer) pageContext.getAttribute("i"));
            %>
            <div class="movie">
                <img src="<%= movie.getString("Poster") %>" alt="포스터 없음">
                <h3><%= movie.getString("Title") %></h3>
                <p><%= movie.getString("Year") %></p>
            </div>
        </c:forEach>
    </div>
</body>
</html>
