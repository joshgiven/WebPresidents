<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Presidential Gallery</title>
</head>
<body>
	<h1>Presidential Gallery</h1>
	<div class="flexcontainer">
	<c:forEach var='pres' items='${presidents}'>
		<div class="flexitem"> <a href="pres.do?view=pres&id=${pres.ordinal}"><img src="${pres.thumbnailPath}"> </a>
		<p>${pres.thumbnailPath}</p>
		<p>${pres.ordinal}: ${pres.fullName} ${pres.startTerm}-${pres.endTerm} </p></div>>
	</c:forEach>
	</div>>
</body>
</html>