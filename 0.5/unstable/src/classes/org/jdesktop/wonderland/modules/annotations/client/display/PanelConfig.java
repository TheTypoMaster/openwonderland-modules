/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.annotations.client.display;

import java.awt.Color;
import java.awt.Font;

/**
 * Stores graphical config information for an AnnotationPane
 * @author mabonner
 */
public class PanelConfig {
  // Default Colors
  public static Color DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY;
  public static Color DEFAULT_FONT_COLOR = Color.WHITE;
  // Default alpha
  public static int DEFAULT_ALPHA = 200;
  
  private Color bgColor;
  private Color fontColor;
  private Font font;

  public PanelConfig(Color bgc, Color fc, Font f){
    bgColor = bgc;
    fontColor = fc;
    font = f;
  }

  public PanelConfig(Color bgc, Color fc){
    bgColor = bgc;
    fontColor = fc;
    font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
  }

  public PanelConfig(){
    bgColor = Color.WHITE;
    fontColor = Color.BLACK;
    font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
  }

  public void setBackgroundColor(Color c){
    bgColor = c;
  }

  public void setFontColor(Color c){
    fontColor = c;
  }

  public void setFont(Font f){
    font = f;
  }

  public Color getBackgroundColor(){
    return bgColor;
  }

  public Color getFontColor(){
    return fontColor;
  }

  public Font getFont(){
    return font;
  }
  
}
