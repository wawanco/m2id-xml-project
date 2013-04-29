<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" />

	<xsl:template match="/">
		<html>
			<title> Customer Data Base </title>
			<xsl:apply-templates select="//customerList" />
		</html>
	</xsl:template>

	<xsl:template match="//customerList">
		<h3>
			The bank with id number 1 has
			<!-- <xsl:value-of select="count(customer/idBank[contains(concat(' ',.,' 
				'), '1')])"/> -->
			<xsl:value-of select="count(customer/checkList/check/idBank/text()[contains(., '1')]) " />
			clients
		</h3>
		<xsl:apply-templates select="//customer" />
	</xsl:template>

	<xsl:template name="nombre">
		<xsl:param name="noeud" />
		<xsl:variable name="n" select="count($noeud)" />
		<xsl:value-of select="$n" />
	</xsl:template>

	<xsl:template match="customer">
		<xsl:choose>
			<xsl:when test="checkList/check/idBank='1'">
				<p>
					<h4>
						- The client with name
					</h4>
					<xsl:value-of select="identity/@firstname" />
					<h4>
						and surname
					</h4>
					<xsl:value-of select="identity/@name" />
					<h4>
						with customer id
					</h4>
					<xsl:value-of select="idCustomer/text()" />
					<h4>
						who deposited
					</h4>
					<xsl:call-template name="nombre">
						<xsl:with-param name="noeud" select="checkList/check" />
					</xsl:call-template>
					<h4>
						check(s)
					</h4>
				</p>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>