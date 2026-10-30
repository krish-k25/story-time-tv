package com.example.storytv;
final class WordLogic {
 static String clean(String s){return s.replaceAll("^[^\\p{L}\\p{M}\\p{N}]+|[^\\p{L}\\p{M}\\p{N}]+$","");}
 static String letters(String s){StringBuilder result=new StringBuilder(),cluster=new StringBuilder();for(int i=0;i<s.length();){int cp=s.codePointAt(i);i+=Character.charCount(cp);int type=Character.getType(cp);boolean mark=type==Character.NON_SPACING_MARK||type==Character.COMBINING_SPACING_MARK||type==Character.ENCLOSING_MARK;if(Character.isLetter(cp)){if(cluster.length()>0){if(result.length()>0)result.append(' ');result.append(cluster);cluster.setLength(0);}cluster.appendCodePoint(cp);}else if(mark&&cluster.length()>0)cluster.appendCodePoint(cp);}if(cluster.length()>0){if(result.length()>0)result.append(' ');result.append(cluster);}return result.toString();}
}
