<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/styles.css">
<link rel="stylesheet" type="text/css" href="css/presidentsStyles.css">
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<title>Presidential Gallery - ${trio.current.ordinal} -
	${trio.current.fullName}</title>
</head>
<body class="background">
		<h1>${trio.current.ordinal} -
			${trio.current.fullName}</h1>

		<div class='chooser'>
			<form method='GET' action='lookup.do'>
				<input type='hidden' name='view' value='pres'> <select class="dropdownList"
					id="presDropdownList" name='id' onchange="this.form.submit()">
					<c:forEach var='president' items='${presidents}'>
						<option value='${president.ordinal}'
							<c:if test='${president.ordinal == trio.current.ordinal}'>selected</c:if>>${president.ordinal}<p> - </p>
							${president.lastName}</option>
					</c:forEach>
				</select>
			</form>
		</div>

		<div class="mainContainer">
			<div id="prevPres">
				<a href='lookup.do?view=pres&id=${trio.previous.ordinal}'><img
					src="${trio.previous.thumbnailPath}"></a>
			</div>
			<div class='curr'>
				<div class='image'>
					<img src='${trio.current.imagePath}' alt='${trio.current.fullName}'>
				</div>

				<div class="presInfo">
					<ul>
						<li id="firstName">${trio.current.fullName}</li>
						<li id="party">${trio.current.party}</li>
						<li id="termRange">${trio.current.startTerm}-${trio.current.endTerm}</li>
						<li id="factoid">${trio.current.factoid}</li>
					</ul>
				</div>
			</div>
			<a id="nextPres" href='lookup.do?view=pres&id=${trio.next.ordinal}'><img
				src="${trio.next.thumbnailPath}"> </a>
		</div>


		<div class="navButtons">
			<a href='lookup.do?view=index'>Home</a>
		</div>
</body>
</html>
