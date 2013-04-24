<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" />


	<xsl:template match="/">
		<html>
			<title> Order List </title>
			<xsl:apply-templates select="//orderList" />
		</html>
	</xsl:template>


	<xsl:template match="//orderList">
		<h2>
			The company with id number 0 has
			<xsl:value-of select="count(order/company/id[contains(., '2')]) " /> <!-- HERE -->
			orders
		</h2>
		<xsl:apply-templates select="//order" />
	</xsl:template>

	<xsl:template match="order">
		<xsl:choose>
			<xsl:when test="company/id='2'"> <!-- HERE -->
				<p>
					<h3>
						- Order :
					</h3>
					<h4>
						Customer with id number -
						<xsl:value-of select="customer/idCustomer/text()" />
						- ordered a total of

						-
						<xsl:value-of
							select="count(customer/cart/product/name[contains(concat(' ',.,' 
				'), customer/cart/product/name)])" />
						- product(s):
					</h4>
					<xsl:choose>
						<xsl:when
							test="count(customer/cart/product/name[contains(concat(' ',.,' 
				'), customer/cart/product/name)]) > 2">
							<xsl:apply-templates select="//product" />
						</xsl:when>
						<xsl:otherwise>
							Product Name:
							<xsl:value-of select="customer/cart/product/name/text()" />
							| Unit Price:
							'
							<xsl:value-of select="customer/cart/product/unitPrice/text()" />
							'
							<xsl:value-of select="customer/cart/product/currency/@type" />
						</xsl:otherwise>
					</xsl:choose>
					<h4>
						from the company '
						<xsl:value-of select="company/name/text()" />
						'
						with a total cost of '
						<xsl:call-template name="sum">
							<xsl:with-param name="noeud"
								select="customer/cart/product/unitPrice" />
						</xsl:call-template>
						'
						<xsl:value-of select="customer/cart/product/currency/@type" />
					</h4>
				</p>
			</xsl:when>
		</xsl:choose>
	</xsl:template>


	<xsl:template match="product">
		<xsl:choose>
			<xsl:when test="id='2'">  <!-- HERE -->
				<p>
					Product Name:
					<xsl:value-of select="name/text()" />
					| Unit Price:
					'
					<xsl:value-of select="unitPrice/text()" />
					'
					<xsl:value-of select="currency/@type" />

				</p>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="sum">
		<xsl:param name="noeud" />
		<xsl:variable name="n" select="sum($noeud)" />
		<xsl:value-of select="$n" />
	</xsl:template>


</xsl:stylesheet>