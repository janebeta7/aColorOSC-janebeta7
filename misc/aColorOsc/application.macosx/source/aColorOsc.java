import processing.core.*; 

import janebeta7.*; 

import org.slf4j.helpers.*; 
import org.apache.mina.filter.keepalive.*; 
import org.apache.log4j.lf5.*; 
import org.apache.mina.filter.codec.*; 
import org.apache.mina.handler.stream.*; 
import org.apache.mina.transport.vmpipe.*; 
import org.apache.log4j.chainsaw.*; 
import org.apache.mina.filter.codec.textline.*; 
import org.apache.log4j.lf5.util.*; 
import org.apache.mina.filter.codec.prefixedstring.*; 
import org.apache.log4j.jmx.*; 
import org.apache.mina.filter.executor.*; 
import org.apache.log4j.or.sax.*; 
import org.apache.mina.filter.codec.demux.*; 
import org.apache.mina.filter.statistic.*; 
import org.apache.log4j.helpers.*; 
import org.apache.mina.core.service.*; 
import org.apache.mina.core.file.*; 
import org.apache.mina.filter.stream.*; 
import org.apache.log4j.or.*; 
import org.apache.log4j.spi.*; 
import org.apache.mina.filter.ssl.*; 
import org.apache.mina.core.polling.*; 
import org.apache.log4j.lf5.viewer.configure.*; 
import org.apache.mina.transport.socket.nio.*; 
import org.apache.mina.handler.chain.*; 
import org.apache.log4j.net.*; 
import org.apache.log4j.or.jms.*; 
import org.apache.mina.filter.logging.*; 
import org.apache.log4j.lf5.viewer.categoryexplorer.*; 
import org.slf4j.impl.*; 
import org.apache.mina.filter.util.*; 
import org.apache.log4j.jdbc.*; 
import org.apache.mina.handler.demux.*; 
import org.apache.mina.filter.firewall.*; 
import org.apache.log4j.*; 
import org.apache.mina.filter.errorgenerating.*; 
import org.apache.mina.filter.codec.serialization.*; 
import org.apache.mina.util.*; 
import org.slf4j.*; 
import org.apache.mina.filter.reqres.*; 
import org.apache.log4j.nt.*; 
import org.slf4j.spi.*; 
import org.apache.log4j.xml.*; 
import org.apache.mina.handler.multiton.*; 
import org.apache.log4j.varia.*; 
import org.apache.mina.core.session.*; 
import org.apache.log4j.lf5.viewer.*; 
import org.apache.log4j.config.*; 
import org.apache.mina.core.filterchain.*; 
import org.apache.mina.transport.socket.*; 
import org.apache.mina.filter.codec.statemachine.*; 
import org.apache.mina.filter.buffer.*; 
import org.apache.mina.core.write.*; 
import org.apache.mina.core.*; 
import org.apache.mina.core.future.*; 
import org.apache.mina.core.buffer.*; 

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

public class aColorOsc extends PApplet {



Colors paletas;

float a = 0.0f;
float s = 0.0f;
public void setup(){
  size (500,500);
  background(0);
  paletas = new Colors(this);
  smooth();
  paletas.osc(5555);

}

public void draw(){
  a = a + 0.04f;
  s = cos(a)*2;
  //paletas.setPalette();
  fill(paletas.getColor());
  ellipse(random(width),random(height),10,10);
  pushMatrix();
  translate(width/2, height/2);
  scale(s); 

  fill(paletas.getColor());
  ellipse(0,0,100,100);
  popMatrix();
}










  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "aColorOsc" });
  }
}
