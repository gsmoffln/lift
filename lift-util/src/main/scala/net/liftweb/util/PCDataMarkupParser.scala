package net.liftweb.util

/*
 * Copyright 2006-2008 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import _root_.scala.xml.parsing.{MarkupParser, MarkupHandler, FatalError, ConstructingHandler, ExternalSources}
import _root_.scala.xml.dtd._
import _root_.scala.xml.{Unparsed, NodeSeq, Atom, Elem, Comment, Group, NamespaceBinding, Node, SpecialNode, Text, TopScope, EntityRef}
import _root_.scala.io.{Source}
import _root_.java.io.{InputStream}

object HtmlEntities {
  val entList = List(("quot",34), ("amp",38), ("lt",60), ("gt",62), ("nbsp",160), ("iexcl",161), ("cent",162), ("pound",163), ("curren",164), ("yen",165),
                     ("euro",8364), ("brvbar",166), ("sect",167), ("uml",168), ("copy",169), ("ordf",170), ("laquo",171), ("shy",173), ("reg",174), ("trade",8482),
                     ("macr",175), ("deg",176), ("plusmn",177), ("sup2",178), ("sup3",179), ("acute",180), ("micro",181), ("para",182), ("middot",183), ("cedil",184),
                     ("sup1",185), ("ordm",186), ("raquo",187), ("frac14",188), ("frac12",189), ("frac34",190), ("iquest",191), ("times",215), ("divide",247),
                     ("Agrave",192), ("Aacute",193), ("Acirc",194), ("Atilde",195), ("Auml",196), ("Aring",197), ("AElig",198), ("Ccedil",199), ("Egrave",200),
                     ("Eacute",201), ("Ecirc",202), ("Euml",203), ("Igrave",204), ("Iacute",205), ("Icirc",206), ("Iuml",207), ("ETH",208), ("Ntilde",209),
                     ("Ograve",210), ("Oacute",211), ("Ocirc",212), ("Otilde",213), ("Ouml",214), ("Oslash",216), ("Ugrave",217), ("Uacute",218), ("Ucirc",219),
                     ("Uuml",220), ("Yacute",221), ("THORN",222), ("szlig",223), ("agrave",224), ("aacute",225), ("acirc",226), ("atilde",227), ("auml",228),
                     ("aring",229), ("aelig",230), ("ccedil",231), ("egrave",232), ("eacute",233), ("ecirc",234), ("euml",235), ("igrave",236), ("iacute",237),
                     ("icirc",238), ("iuml",239), ("eth",240), ("ntilde",241), ("ograve",242), ("oacute",243), ("ocirc",244), ("otilde",245), ("ouml",246),
                     ("oslash",248), ("ugrave",249), ("uacute",250), ("ucirc",251), ("uuml",252), ("yacute",253), ("thorn",254), ("yuml",255), ("OElig",338),
                     ("oelig",339), ("Scaron",352), ("scaron",353), ("Yuml",376), ("circ",710), ("ensp",8194), ("emsp",8195), ("zwnj",204), ("zwj",8205), ("lrm",8206),
                     ("rlm",8207), ("ndash",8211), ("mdash",8212), ("lsquo",8216), ("rsquo",8217), ("sbquo",8218), ("ldquo",8220), ("rdquo",8221), ("bdquo",8222),
                     ("dagger",8224), ("Dagger",8225), ("permil",8240), ("lsaquo",8249), ("rsaquo",8250), ("fnof",402), ("bull",8226), ("hellip",8230), ("prime",8242),
                     ("Prime",8243), ("oline",8254), ("frasl",8260), ("weierp",8472), ("image",8465), ("real",8476), ("alefsym",8501), ("larr",8592), ("uarr",8593),
                     ("rarr",8594), ("darr",8495), ("harr",8596), ("crarr",8629), ("lArr",8656), ("uArr",8657), ("rArr",8658), ("dArr",8659), ("hArr",8660),
                     ("forall",8704), ("part",8706), ("exist",8707), ("empty",8709), ("nabla",8711), ("isin",8712), ("notin",8713), ("ni",8715), ("prod",8719),
                     ("sum",8721), ("minus",8722), ("lowast",8727), ("radic",8730), ("prop",8733), ("infin",8734), ("ang",8736), ("and",8743), ("or",8744),
                     ("cap",8745), ("cup",8746), ("int",8747), ("there4",8756), ("sim",8764), ("cong",8773), ("asymp",8776), ("ne",8800), ("equiv",8801), ("le",8804),
                     ("ge",8805), ("sub",8834), ("sup",8835), ("nsub",8836), ("sube",8838), ("supe",8839), ("oplus",8853), ("otimes",8855), ("perp",8869), ("sdot",8901),
                     ("lceil",8968), ("rceil",8969), ("lfloor",8970), ("rfloor",8971), ("lang",9001), ("rang",9002), ("loz",9674), ("spades",9824), ("clubs",9827),
                     ("hearts",9829), ("diams",9830), ("Alpha",913), ("Beta",914), ("Gamma",915), ("Delta",916), ("Epsilon",917), ("Zeta",918), ("Eta",919),
                     ("Theta",920), ("Iota",921), ("Kappa",922), ("Lambda",923), ("Mu",924), ("Nu",925), ("Xi",926), ("Omicron",927), ("Pi",928), ("Rho",929),
                     ("Sigma",931), ("Tau",932), ("Upsilon",933), ("Phi",934), ("Chi",935), ("Psi",936), ("Omega",937), ("alpha",945), ("beta",946), ("gamma",947),
                     ("delta",948), ("epsilon",949), ("zeta",950), ("eta",951), ("theta",952), ("iota",953), ("kappa",954), ("lambda",955), ("mu",956), ("nu",957),
                     ("xi",958), ("omicron",959), ("pi",960), ("rho",961), ("sigmaf",962), ("sigma",963), ("tau",964), ("upsilon",965), ("phi",966), ("chi",967),
                     ("psi",968), ("omega",969), ("thetasym",977), ("upsih",978), ("piv",982))

  val entMap: Map[String, Char] = Map.empty ++ entList.map{ case (name, value) => (name, value.toChar)}

  val entities = entList.
  map{case (name, value) => (name, new ParsedEntityDecl(name, new IntDef(value.toChar.toString)))}
  def apply() = entities
}

/**
 * Extends the Markup Parser to do the right thing (tm) with PCData blocks
 */
trait PCDataMarkupParser[PCM <: MarkupParser with MarkupHandler] extends MarkupParser { self: PCM =>
  /** '&lt;! CharData ::= [CDATA[ ( {char} - {char}"]]&gt;"{char} ) ']]&gt;'
   *
   * see [15]
   */
  override def xCharData: NodeSeq = {
    xToken("[CDATA[")
    val pos1 = pos
    val sb: StringBuilder = new StringBuilder()
    while (true) {
      if (ch==']'  &&
          { sb.append(ch); nextch; ch == ']' } &&
          { sb.append(ch); nextch; ch == '>' } ) {
        sb.setLength(sb.length - 2);
        nextch;
        return PCData(sb.toString)
      } else sb.append( ch );
      nextch;
    }
    throw FatalError("this cannot happen");
  }
}

class PCDataXmlParser(val input: Source) extends ConstructingHandler with PCDataMarkupParser[PCDataXmlParser] with ExternalSources  {
  val preserveWS = true
  ent ++= HtmlEntities()
  // val input = from
}

object PCDataXmlParser {
  def apply(in: InputStream): Box[NodeSeq] = {
    val source = Source.fromInputStream(in)
    val p = new PCDataXmlParser(source)
    while (p.ch != '<' && p.curInput.hasNext) p.nextch
    p.document match {
      case null => Empty
      case doc => Full(doc)
    }
  }

  def apply(in: String): Box[NodeSeq] = {
    val source = Source.fromString(in)
    val p = new PCDataXmlParser(source)
    while (p.ch != '<' && p.curInput.hasNext) p.nextch
    p.document match {
      case null => Empty
      case doc => Full(doc)
    }
  }
}

case class PCData(_data: String) extends Atom[String](_data) {
  /* The following code is a derivative work of scala.xml.Text */
  if (null == data)
  throw new _root_.java.lang.NullPointerException("tried to construct Text with null")

  final override def equals(x: Any) = x match {
    case s:String  => s.equals(data.toString())
    case s:Atom[_] => data == s.data
    case _ => false
  }

  /** Returns text, with some characters escaped according to the XML
   *  specification.
   *
   *  @param  sb ...
   *  @return ...
   */
  override def toString(sb: StringBuilder) = {
    sb.append("<![CDATA[")
    sb.append(data)
    sb.append("]]>")
  }
}

object AltXML {
  val ieBadTags: Set[String] = Set("br", "hr")

  def toXML(n: Node, stripComment: Boolean, convertAmp: Boolean,
            ieMode: Boolean): String = {
    val sb = new StringBuilder()
    toXML(n, TopScope, sb, stripComment, convertAmp, ieMode)
    sb.toString()
  }

  /**
   * Appends a tree to the given stringbuffer within given namespace scope.
   *
   * @param n            the node
   * @param pscope       the parent scope
   * @param sb           stringbuffer to append to
   * @param stripComment if true, strip comments
   */
  def toXML(x: Node, pscope: NamespaceBinding, sb: StringBuilder,
            stripComment: Boolean, convertAmp: Boolean,
            ieMode: Boolean): Unit =
  x match {
    case c: Comment if !stripComment =>
      c.toString(sb)

    case er: EntityRef if convertAmp =>
      HtmlEntities.entMap.get(er.entityName) match {
        case Some(chr) if chr.toInt >= 128 => sb.append(chr)
        case _ => er.toString(sb)
      }

    case x: SpecialNode =>
      x.toString(sb)

    case g: Group =>
      for (c <- g.nodes)
      toXML(c, x.scope, sb, stripComment, convertAmp, ieMode)

    case e: Elem if !ieMode && ((e.child eq null) || e.child.isEmpty) =>
      sb.append('<')
      e.nameToString(sb)
      if (e.attributes ne null) e.attributes.toString(sb)
      e.scope.toString(sb, pscope)
      sb.append("/>")

    case e: Elem if ieMode && ((e.child eq null) || e.child.isEmpty) &&
      ieBadTags.contains(e.label) =>
      sb.append('<')
      e.nameToString(sb)
      if (e.attributes ne null) e.attributes.toString(sb)
      e.scope.toString(sb, pscope)
      sb.append("/>")

    case e: Elem =>
      // print tag with namespace declarations
      sb.append('<')
      e.nameToString(sb)
      if (e.attributes ne null) e.attributes.toString(sb)
      e.scope.toString(sb, pscope)
      sb.append('>')
      sequenceToXML(e.child, e.scope, sb, stripComment, convertAmp, ieMode)
      sb.append("</")
      e.nameToString(sb)
      sb.append('>')

    case _ => // dunno what it is, but ignore it
  }


  /**
   * @param children     ...
   * @param pscope       ...
   * @param sb           ...
   * @param stripComment ...
   */
  def sequenceToXML(children: Seq[Node], pscope: NamespaceBinding,
                    sb: StringBuilder, stripComment: Boolean,
                    convertAmp: Boolean, ieMode: Boolean): Unit = {
    if (children.isEmpty)
    return
    else if (children forall { y => y.isInstanceOf[Atom[_]] && !y.isInstanceOf[Text] }) { // add space
      val it = children.elements
      val f = it.next
      toXML(f, pscope, sb, stripComment, convertAmp, ieMode)
      while (it.hasNext) {
        val x = it.next
        sb.append(' ')
        toXML(x, pscope, sb, stripComment, convertAmp, ieMode)
      }
    } else {
      for (c <- children) toXML(c, pscope, sb, stripComment, convertAmp, ieMode)
    }
  }

}
