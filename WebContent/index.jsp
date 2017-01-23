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
		<div class='chooser'>
			<form method='GET' action='lookup.do' >
				<input type='hidden' name='view' value='pres'> <select
					class="dropdownList" name='id' onchange="this.form.submit()">
					<c:forEach var='president' items='${presidents}'>
						<option value='${president.ordinal}'
							<c:if test='${president.ordinal == trio.current.ordinal}'>selected</c:if>>${president.ordinal}<p>
								-</p> ${president.lastName}
						</option>
					</c:forEach>
				</select>
			</form>
			</div>
	<div class="filters">
			<form method='GET' action='lookup.do' name='filter'>
	
				<select name='field' class='dropdownList'>
				<c:forEach var="field" items="${filterStringProps}">
					<option value='${field.value}'>${field.key}</option>
				</c:forEach>
				</select> 
				<select name='operator' class="dropdownList">
				<c:forEach var="op" items="${filterStringOps}">
					<option value='${op.value}'>${op.key}</option>
				</c:forEach>
				</select> 
				<input type="text" name="operand">
				<input type="submit" name="submit" value="filter"> 
				
			</form>
		</div>
		<div class="filters">
			<form method='GET' action='lookup.do'>
		</div>
		<div class="filters">
			<form method='GET' action='lookup.do' name='filter'>
	
				<select name='field' class='dropdownList'>
				<c:forEach var="field" items="${filterIntProps}">
					<option value='${field.value}'>${field.key}</option>
				</c:forEach>
				</select> 
				<select name='operator' class="dropdownList">
				<c:forEach var="field" items="${filterIntOps}">
					<option value='${field.value}'>${field.key}</option>
				</c:forEach>
				</select> 
				<input type="text" name="operand">
				<button>add filter</button>
				
			</form>
		</div>
		<div class="filters">
			<form method='GET' action='lookup.do'>
				<p id="partyLabel">Party: </p><select name='filter' class="dropdownList" onchange="this.form.submit()">
					<c:forEach var='party' items='${filterPreParties}'>
						<option value='${party.value}' <c:if test="${filters.contains(party.value) == true}">selected</c:if> >${party.key}</option>
					</c:forEach>
				</select> 
			</form>
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
