<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>index</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/register">
    姓名：<input name="name" type="text"/>
    年龄：<input name="age" type="text"/>
    <input type="submit" value="注册"/>
</form>
</body>
</html>