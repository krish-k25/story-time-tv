package com.example.storytv;
final class LanguageCatalog {static int count(int language){return language==0?56:25;}static int storyIndex(int language,int position){if(language==1)return 53+position;return position<53?position:position+25;}}
