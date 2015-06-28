<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

  <xsl:import href="http://docbook.sourceforge.net/release/xsl/current/xhtml/docbook.xsl" />
  <xsl:import href="yocto-project-qs-titlepage.xsl"/>

  <xsl:include href="../template/permalinks.xsl"/>
  <xsl:include href="../template/section.title.xsl"/>
  <xsl:include href="../template/component.title.xsl"/>
  <xsl:include href="../template/division.title.xsl"/>
  <xsl:include href="../template/formal.object.heading.xsl"/>

  <xsl:param name="generate.toc" select="'article nop'"></xsl:param>
  <xsl:param name="html.stylesheet" select="'qs-style.css'" />
</xsl:stylesheet>
