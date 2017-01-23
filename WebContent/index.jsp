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
					<option value='first-name'>First Name</option>
					<option value='last-name'>Last Name</option>
					<option value='fullName'>Full Name</option>
					<option value='factoid'>Factoid</option>
				</select> 
				<select name='operator' class="dropdownList">
					<option value=''>Contains</option>
					<option value='-'>Doesn't Contain</option>
					<option value='/'>Contains Any</option>
				</select> 
				<input type="text" name="operand">
				<input type="submit" name="submit" value="fiter"> 
				
			</form>
		</div>
		<div class="filters">
			<form method='GET' action='lookup.do' name='filter'>
	
				<select name='field' class='dropdownList'>
					<option value='termStart'>Term Start</option>
					<option value='termEnd'>Term End</option>
					<option value='termLength'>Term Length</option>
				</select> 
				<select name='operator' class="dropdownList">
					<option value='=='>Equals</option>
					<option value="<">Was Before</option>
					<option value=">">Was After</option>
				</select> 
				<input type="text" name="operand">
				<input type="submit" name="submit" value="fiter"> 
				
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
