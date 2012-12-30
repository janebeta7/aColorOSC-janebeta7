import processing.core.*; 

import oscP5.*; 
import netP5.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class aColorOSC_server extends PApplet {




int miColor;
OscP5 oscP5;
NetAddress myRemoteLocation;


float a = 0.0f;
float s = 0.0f;


public void setup() {
  size(800,800);
  frameRate(25);
  oscP5 = new OscP5(this,5555); //receiver port
  oscP5.plug(this,"aColorCount","/acolor");
 background(0);
}

public void aColorCount(int theA) {
  miColor = theA; 
  
   
}

public void draw(){
  a = a + 0.04f;
  s = cos(a)*2;
  //paletas.setPalette();
  fill(miColor);
  ellipse(random(width),random(height),10,10);
  pushMatrix();
  translate(width/2, height/2);
  scale(s); 

  fill(miColor);
  ellipse(0,0,100,100);
  popMatrix();
}






  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "aColorOSC_server" });
  }
}
