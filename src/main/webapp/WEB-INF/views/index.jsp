<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<html>
<head>
    <title>Movie Search</title>
    <style>
        body {
            background-color: #121212;
            color: white;
            text-align: center;
            padding: 50px;
            font-family: Arial, sans-serif;
        }
        input, button {
            padding: 10px;
            font-size: 16px;
        }
    </style>
</head>
<body>
    <h1>🎬 MOVIE SEARCH</h1>
    <form action="${pageContext.request.contextPath}/search" method="get">
        <input type="text" name="query" placeholder="영화 제목 입력">
        <button type="submit">검색</button>
    </form>
</body>
</html>
