<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" />


	<xsl:template match="/">
		<html>
			<title> Order List </title>
			<xsl:apply-templates select="//order" />
		</html>
	</xsl:template>


	<xsl:template match="//order">
		<h2>
			The customer with id number
			<xsl:value-of select="idCustomer/text()" />
			requests from the company with id number
			<xsl:value-of select="idCompany/text()" />
			the following order in
			<xsl:value-of select="currency/@type" />
			:
		</h2>
		<xsl:apply-templates select="//item" />

	</xsl:template>

	<xsl:template match="item">
		<p>
			<h3>
				- Item :
			</h3>
			Product Name:
			<xsl:value-of select="product/name/text()" />
			| Unit Price:
			'
			<xsl:value-of select="product/unitPrice/text()" />
			'
			| Quantity :
			<xsl:value-of select="quantity/text()" />
			<h4>
				from the company with a total cost of '
				<xsl:call-template name="sum">
					<xsl:with-param name="n1" select="product/unitPrice" />
					<xsl:with-param name="n2" select="quantity" />
				</xsl:call-template>
				'
			</h4>
		</p>
	</xsl:template>

	<xsl:template name="sum">
		<xsl:param name="n1" />
		<xsl:param name="n2" />
		<xsl:variable name="n" select="$n1*$n2" />
		<xsl:value-of select="$n" />
		<xsl:value-of select="sum(n)"/>
	</xsl:template>

</xsl:stylesheet>