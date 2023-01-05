import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MosaicPhoto extends PApplet {

//Created by Luis Gil
//www.legildesign.com

//This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
//To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/


//This is the only area you need to alter. All variable can be
//controlled from this block.

int imageCount = 560;//777;
//the number of images available for use in the mosaic.these images should be saved in the sketch folder and conform to 
//the naming protocol listed below. because these are the images that will make up the larger mosaic they should be saved
//in a small format so as not to take up too much memory. the ones i use are about 75px X 75px
//使う写真の数
//sketchフォルダに画像を保存している
//

PImage[] pics = new PImage[imageCount];
//array containing all of the images available for use in the mosaic.
//モザイクに使用する画像の全てを含む配列

int pixelSize = 10;
//the dimension (length and width) of one "pixel" of the mosaic.
//Pixelの縦横の大きさ

String mainImage = "R_size300.JPG";//"DSC00178.JPG";
//the name of the main image.
//元の画像

String imageName = "R2_2.JPG";
//the output of the mosaic.
//モザイク画

int tintValue = 60;
//will affect the transparency of a rectangle filled with the region's
//average color. this method is used because reducing the opacity in the 
//tint() funtion reduces the opacity of the entire image, not just the tint
//effect. this tries to mediate that. values are 0-255.

//tint()

//End of editable values. Everything below this line should be
//left alone.

PImage bgPic;
//the image to be used for the mosaic.
//モザイク画に使用される（並び替える）画像

//setup the general color ranges.
int[] greenDark = new int[0];
int[] greenMid = new int[0];
int[] greenBright = new int[0];
int[] redDark = new int[0];
int[] redMid = new int[0];
int[] redBright = new int[0];
int[] blueDark = new int[0];
int[] blueMid = new int[0];
int[] blueBright = new int[0];
int[] greenDark2 = new int[0];
int[] greenMid2 = new int[0];
int[] greenBright2 = new int[0];
int[] redDark2 = new int[0];
int[] redMid2 = new int[0];
int[] redBright2 = new int[0];
int[] blueDark2 = new int[0];
int[] blueMid2 = new int[0];
int[] blueBright2 = new int[0];


public void settings(){
    bgPic = loadImage(mainImage);  //元画像
    int picWidth = bgPic.width;
    int picHeight = bgPic.height;
  size(picWidth,picHeight);
}

public void setup(){

  colorMode(RGB, 255);

  background(bgPic);  //backgroundを元画像にする

  //画像の読み込み
  //this part loads all the images in sequence. the naming of the images can be easily done with
  //photoshop's batch tool. the code below is set for a batch of images named as "bg_" followed
  //by a four digit sequential serial number.
  /*
  for(int j = 0; j<imageCount; j++) {
    if (j < 10) {
      pics[j] = loadImage("bg_" + "000" + j + ".JPG");
    } 
    else if (j < 100) {
      pics[j] = loadImage("bg_" + "00" + j + ".JPG");
    } 
    else if (j < 1000) {
      pics[j] = loadImage("bg_" + "0" + j + ".JPG");
    } 
    else if (j < 10000) {
      pics[j] = loadImage("bg_" +  j + ".JPG");
    }
  }
  */
  for(int j = 0; j<imageCount; j++) {
    pics[j] = loadImage("Image_"+j+".JPG");
  }
  
  
  noLoop();
}

public void draw(){

  for (int i = 0; i < pics.length; i++){

    int avColor = averageColor(pics[i]);  //画像の平均の色
    float avBright = averageBrightness(pics[i]);  //画像の平均の明るさ
    int tag = sortImages(avColor, avBright);

    if (tag == 1) {
      redDark = append(redDark, i);
    } 
    else if (tag == 2) {
      redDark2 = append(redDark2, i);
    }
    else if (tag == 3) {
      redMid = append(redMid, i);
    }
    else if (tag == 4) {
      redMid2 = append(redMid2, i);
    }  
    else if (tag == 5) {
      redBright = append(redBright, i);
    } 
    else if (tag == 6) {
      redBright2 = append(redBright2, i);
    } 
    else if (tag == 7) {
      greenDark = append(greenDark, i);
    } 
    else if (tag == 8) {
      greenDark2 = append(greenDark2, i);
    } 
    else if (tag == 9) {
      greenMid = append(greenMid, i);
    } 
    else if (tag == 10) {
      greenMid2 = append(greenMid2, i);
    }
    else if (tag == 11) {
      greenBright = append(greenBright, i);
    } 
    else if (tag == 12) {
      greenBright2 = append(greenBright2, i);
    }
    else if (tag == 13) {
      blueDark = append(blueDark, i);
    } 
    else if (tag == 14) {
      blueDark2 = append(blueDark2, i);
    } 
    else if (tag == 15) {
      blueMid = append(blueMid, i);
    } 
    else if (tag == 16) {
      blueMid2 = append(blueMid2, i);
    } 
    else if (tag == 17) {
      blueBright = append(blueBright, i);
    } 
    else if (tag == 18) {
      blueBright2 = append(blueBright2, i);
    }

  }

  //below is where the magic happens...hopefully.
  for (int u = 0; u < width; u += pixelSize){
    for (int v = 0; v < height; v += pixelSize) {
      PImage mosaicPixel = get(u,v, pixelSize, pixelSize);  //元画像のモザイク一つあたりの領域画像
      int sampleColor = averageColor(mosaicPixel);  //元画像のモザイク一つの平均RGB
      float sampleBrightness = averageBrightness(mosaicPixel);  //元画像のモザイク一つの平均の明るさ
      int id = sortImages(sampleColor, sampleBrightness);  
      println(id);

      //id=1-18に該当する画像がなかった場合に、tempを使用する
      if (id == 0) {
        int tempIndex = randomArrayIndex(imageCount);
        tint(sampleColor);
        image(pics[tempIndex], u,v,pixelSize,pixelSize);
        noTint();
      } 
      else if (id == 1) {
        int aRange = redDark.length;
        println("redDark:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = redDark[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }
      } 
      else if (id == 2) {
        int aRange = redDark2.length;
        println("redDark2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = redDark2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }
      }
      else if (id == 3) {
        int aRange = redMid.length;
        println("redMid:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = redMid[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }
      } 
      else if (id == 4) {
        int aRange = redMid2.length;
        println("redMid2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = redMid2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }
      }
      else if (id == 5) {
        int aRange = redBright.length;
        println("redBright:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = redBright[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        }  
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }
      } 
      else if (id == 6) {
        int aRange = redBright2.length;
        println("redBright2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = redBright2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        }  
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }
      } 
      else if (id == 7) {
        int aRange = greenDark.length;
        println("greenDark:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = greenDark[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        }  
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }
      } 
      else if (id == 8) {
        int aRange = greenDark2.length;
        println("greenDark2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = greenDark2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        }  
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }
      } 
      else if (id == 9) {
        int aRange = greenMid.length;
        println("greenMid:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = greenMid[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        } 
      } 
      else if (id == 10) {
        int aRange = greenMid2.length;
        println("greenMid2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = greenMid2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        } 
      }
      else if (id == 11) {
        int aRange = greenBright.length;
        println("greenBright:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = greenBright[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }  
      } 
      else if (id == 12) {
        int aRange = greenBright2.length;
        println("greenBright2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = greenBright2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }  
      } 
      else if (id == 13) {
        int aRange = blueDark.length;
        println("blueDark:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = blueDark[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }  
      } 
      else if (id == 14) {
        int aRange = blueDark2.length;
        println("blueDark2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = blueDark2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          noTint();
        }  
      } 
      else if (id == 15) {
        int aRange = blueMid.length;
        println("blueMid:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = blueMid2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        }  
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }  
      } 
      else if (id == 16) {
        int aRange = blueMid2.length;
        println("blueMid2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = blueMid2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        }  
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }  
      }
      else if (id == 17) {
        int aRange = blueBright.length;
        println("blueBright:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = blueBright[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }  
      }
      
      else if (id == 18) {
        int aRange = blueBright2.length;
        println("blueBright2:"+aRange);
        if (aRange > 0) {
          int tempIndex = randomArrayIndex(aRange);
          int imageIndex = blueBright2[tempIndex];
          image(pics[imageIndex], u,v,pixelSize,pixelSize);
          noStroke();
          fill(sampleColor, tintValue);
          rect(u,v,pixelSize, pixelSize);
        } 
        else {
          int tempIndex = randomArrayIndex(imageCount);
          tint(sampleColor);
          image(pics[tempIndex], u,v,pixelSize,pixelSize);
          noTint();
        }  
      }
      
    }
  }
  save(imageName);
}

//画像のRGBの平均値を算出する
public int averageColor(PImage picture) {
  float r = 0;
  float g = 0;
  float b = 0;

  for(int i = 0; i < picture.pixels.length; i++){
    r += red(picture.pixels[i]);
    g += green(picture.pixels[i]);
    b += blue(picture.pixels[i]);
  }
  r = r / picture.pixels.length;
  g = g / picture.pixels.length;
  b = b / picture.pixels.length;

  int aColor = color(r,g,b);

  return aColor;
}

public float averageBrightness(PImage picture) {
  float b=0;

  for(int i = 0; i < picture.pixels.length; i++){
    b += brightness(picture.pixels[i]);
  }
  b = b/picture.pixels.length;

  return b;
}

/* i was having an issue with a seperate function that would organize the photos into their respective
 arrays, so this one will sort the values and then these can be used to drop the photos into the correct
 array. the order will be red, green, blue with each having an order from dark to light...for example,
 3 would be bright red, 7 would be dark blue etc. the sequence goes from 1 - 9
 場合分け
 1.Redの色が一番強い時、Blueの明るさの平均値で場合分け
 2.
*/
public int sortImages(int avC, float avB) {
  float r = red(avC);
  float g = green(avC);
  float b = blue(avC);
  int value = 0;

  if (r > g && r > b) {
    if (avB >=0 && avB <= 42) {
      value = 1;  //redDark
    } 
    else if (avB > 42 && avB<= 85) {
      value = 2;  //redDark2
    } 
    else if (avB > 85 && avB <= 127){
      value = 3;  //redMid
    }
    else if (avB > 127 && avB <= 170){
      value = 4;  //redMid2
    }
    else if (avB > 170 && avB <= 210){
      value = 5;  //redBright
    }
    else if (avB > 210){
      value = 6;  //redBright2
    }
  } 

  else if (g > r && g > b) {
    if (avB >=0 && avB <= 42) {
      value = 7;  //greenDark
    } 
    else if (avB > 42 && avB<= 85) {
      value = 8;  //greenDark2
    } 
    else if (avB > 85 && avB <= 127){
      value = 9;  //greenMid
    }
    else if (avB > 127 && avB <= 170){
      value = 10;  //greenMid2
    }
    else if (avB > 170 && avB <= 210){
      value = 11;  //greenBright
    }
    else if (avB > 210){
      value = 12;  //greenBright2
    }
  } 
  
  else if (b >r && b > g) {
    if (avB >=0 && avB <= 42) {
      value = 13;  //blueDark
    } 
    else if (avB > 42 && avB<= 85) {
      value = 14;  //blueDark2
    } 
    else if (avB > 85 && avB <= 127){
      value = 15;  //blueMid
    }
    else if (avB > 127 && avB <= 170){
      value = 16;  //blueMid2
    }
    else if (avB > 170 && avB <= 210){
      value = 17;  //blueBright
    }
    else if (avB > 210){
      value = 18;  //blueBright2
    }
  }
 
 
  return value;
}

//配列の値のランダム化
public int randomArrayIndex(int range){
  int index;
  index = PApplet.parseInt(random(range));

  return index;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MosaicPhoto" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
