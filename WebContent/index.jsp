<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/styles.css">
<link rel="stylesheet" type="text/css" href="css/indexStyles.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Presidential Gallery</title>
</head>
<body class="background">
	<h1>Presidential Gallery</h1>
	<div class="flexcontainer">
		<div class="filter">
			<select>
				<option name="filter" value=${value}>${key}</option>
			</select>
			<!-- TO DO: add filters. Page layout for the filter div is already stubbed out -->
		</div>
		<div class="imageContainer">
			<c:forEach var='pres' items='${presidents}'>
				<div class="flexitem">
					<a href="lookup.do?view=pres&id=${pres.ordinal}"><img
						src="${pres.thumbnailPath}"> </a>
					<p>${pres.ordinal}:
						${pres.fullName} <br> ${pres.startTerm}-${pres.endTerm}
					</p>
				</div>
			</c:forEach>
		</div>

	</div>
</body>
</html>
