<?xml version="1.0" encoding="UTF-8"?>
<tileset name="tilesheet" tilewidth="32" tileheight="32">
 <image source="tilesheet.png" width="256" height="320"/>
 <tile id="0">
  <properties>
   <property name="type" value="buildable"/>
  </properties>
 </tile>
 <tile id="1">
  <properties>
   <property name="type" value="path"/>
  </properties>
 </tile>
 <tile id="2">
  <properties>
   <property name="end" value="false"/>
   <property name="start" value="true"/>
   <property name="waypoint" value="true"/>
  </properties>
 </tile>
 <tile id="3">
  <properties>
   <property name="end" value="true"/>
   <property name="start" value="false"/>
   <property name="waypoint" value="true"/>
  </properties>
 </tile>
 <tile id="4">
  <properties>
   <property name="type" value="boundry"/>
  </properties>
 </tile>
</tileset>
