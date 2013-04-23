<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />

	<xsl:template match="/">
		<html>
			<title>My Check</title>
			<xsl:apply-templates select="//check" />
		</html>
	</xsl:template>

	<xsl:template match="//check">

		<h3>
			Customer of id number
			<xsl:value-of select="idCustomer/text()" />
			, client of bank with id number
			<xsl:value-of select="idBank/text()" />
			requests the following check
		</h3>

		<h3>Check number : </h3>

		<xsl:value-of select="idCheck/text()" />

		<h3>Check date: </h3>

		<xsl:value-of select="date/@day" />
		-
		<xsl:value-of select="date/@month" />
		-
		<xsl:value-of select="date/@year" />

		<h3>Check amount: </h3>
		'
		<xsl:value-of select="amount/text()" />
		'
		<xsl:value-of select="currency/@type" />


	</xsl:template>


</xsl:stylesheet>