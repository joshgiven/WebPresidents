<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/styles.css">
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<title>Presidential Gallery - ${trio.current.ordinal} -
	${trio.current.fullName}</title>
</head>
<body>
	<h1>Presidential Gallery - ${trio.current.ordinal} -
		${trio.current.fullName}</h1>

	<div class='chooser'>
		<form method='GET' action='lookup.do'>
			<input type='hidden' name='view' value='pres'> <select
				name='id'>
				<c:forEach var='president' items='${presidents}'>
					<option value='${president.ordinal}'
						<c:if test='${president.ordinal == trio.current.ordinal}'>selected</c:if>>${president.ordinal}
						${president.lastName}</option>
				</c:forEach>
			</select>
			<button>Go</button>
		</form>
	</div>


	<div class='curr'>
		<div class='image'>
			<img src='${trio.current.imagePath}' alt='${trio.current.fullName}'>
		</div>

		<img
			src='img/big/<fmt:formatNumber pattern="00" value="${trio.current.ordinal}" />.jpeg'
			alt='${trio.current.fullName}'>
		<ul>
			<li>${trio.current.fullName}</li>
			<li>${trio.current.startTerm}- ${trio.current.endTerm}</li>
			<li>${trio.current.party}</li>
			<li>${trio.current.factoid}</li>
		</ul>
	</div>

	<div id="navButtons">
		<div class='prev'>
			<button name="previous" value="previous"
				formaction="lookup.do?view=pres&id=${trio.previous.ordinal}">Previous</button>
			<%-- <a href='lookup.do?view=pres&id=${trio.previous.ordinal}'>previous</a> --%>
		</div>

		<div class='home'>
			<button name="home" value="home" formaction="lookup.do?view=index">Home</button>
			<%-- <a href='lookup.do?view=index'>home</a> --%>
		</div>

		<div class='next'>
			<button name="next" value="next"
				formaction="lookup.do?view=pres&id=${trio.next.ordinal}">Next</button>
			<%-- <a href='lookup.do?view=pres&id=${trio.next.ordinal}'>next</a> --%>
		</div>
	</div>
</body>
</html>
