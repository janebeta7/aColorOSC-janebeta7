
import oscP5.*;
import netP5.*;
color miColor;
OscP5 oscP5;
NetAddress myRemoteLocation;


float a = 0.0;
float s = 0.0;


void setup() {
  size(800,800);
  frameRate(25);
  oscP5 = new OscP5(this,5555); //receiver port
  oscP5.plug(this,"aColorCount","/acolor");
 background(0);
}

public void aColorCount(int theA) {
  miColor = theA; 
  
   
}

void draw(){
  a = a + 0.04;
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






