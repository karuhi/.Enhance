import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import java.util.Collections; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class dot_Enhance extends PApplet {


// -----Minim-----
Minim minim;
AudioPlayer Song, SoundEffect;
// -----全般-----
int BoxSize_W = 50;//10x10
int BoxSize_H = 50;//10x10
int BoxLength = 20;//長さ
int FrameCount = 0;
AI ai;
Other other;
// -----AI関連-----
int AICount = 1000;
boolean IfFirstBorn = false;
//単純に埋められてるかどうか
boolean[][] isFilled = new boolean[BoxSize_W+2][BoxSize_H+2];
//次の世代
boolean[][] nextGen = new boolean[BoxSize_W+2][BoxSize_H+2];
// -----Home関連-----
int isHome = 0;
boolean isFullHeight = false;
boolean isDraw = false;
// TODO：タイトルロゴ
public void setup() {
  
  frameRate(60);
  minim = new Minim(this);
  Song = minim.loadFile("EDM_NEW_LIFE.mp3");
  SoundEffect = minim.loadFile("Respawn.mp3");
  ai = new AI();
  other = new Other();
  Song.play();
  Song.loop();
}

public void draw() {
  //initialize
  other.DrawField();
  if (isHome == 0) { //もしホーム画面ならロゴとかローディングとか出す
    if (!isFullHeight) {
      other.rain(); 
    }
    other.ExclusionDraw();
    if (isFullHeight) {
     isHome = 1;
    }
  } else if (isHome == 1) { //そうでないならメインに行く

  other.DrawField();
    if (!IfFirstBorn) {
      for (int i = 0; i < AICount; ++i) {
        ai.Add(i);
      }
      IfFirstBorn = true;
    }
    FrameCount++;
    if ((FrameCount/5) == 1) {
      FrameCount = 0;
      ai.Think();
    }
    int BoxX = ((int)mouseX/BoxLength);
    int BoxY = ((int)mouseY/BoxLength);
     /*other.artClient(BoxX, BoxY);*/
    if (mousePressed) {
      isClick = false;
      other.Respawn(BoxX,BoxY);
    }

    other.DrawClient();
  }
}
boolean isClick = false;
public void mouseReleased (){
  isClick = true;
  print("a");
}
class AI {
  public int[] RandRespawn() {
    ArrayList<int[]> OpenSpace = new ArrayList<int[]>();
    for (int x = 1; x <= BoxSize_W; x++) {
      for (int y = 1; y <= BoxSize_H; y++) {
        if (!isFilled[x][y]) {
          int[] a = {
            x, y
          };
          OpenSpace.add(a);
        }
      }
    }
    int[] result = OpenSpace.get((int)random(0, OpenSpace.size()));
    return result;
  }
  public void Add(int id) {
    /**
     *  pos[] = {ID,x,y}
     */
    int[] a = RandRespawn();
    int x = a[0];
    int y = a[1];
    //int[] pos = {id,x,y};
    //AIs.add(pos);
    isFilled[x][y] = true;
  }
  public void Think() {
    for (int x = 1; x <= BoxSize_W; x++) {
      for (int y = 1; y <= BoxSize_H; y++) {
        // 8方向のうち、生きているセルをカウント
        int count = 0;
        // 左上、真上、右上
        count += isFilled[x - 1][y - 1] ? 1 : 0;
        count += isFilled[x][y - 1] ? 1 : 0;
        count += isFilled[x + 1][y - 1] ? 1 : 0;
        // 左、右
        count += isFilled[x - 1][y] ? 1 : 0;
        count += isFilled[x + 1][y] ? 1 : 0;
        // 左下、真下、右下
        count += isFilled[x - 1][y + 1] ? 1 : 0;
        count += isFilled[x][y + 1] ? 1 : 0;
        count += isFilled[x + 1][y + 1] ? 1 : 0;
        // 次世代の生死判定
        if (isFilled[x][y] && count <= 1) {
          nextGen[x][y] = false; // 過疎で死滅
        } else if (isFilled[x][y] && (count == 2 || count == 3)) {
          nextGen[x][y] = true; // 生存
        } else if (isFilled[x][y] && count >= 4) {
          nextGen[x][y] = false; // 過密で死滅
        } else if (!isFilled[x][y] && count == 3) {
          nextGen[x][y] = true; // 誕生
        } else {
          nextGen[x][y] = false;
        }
      }
    }
    // 次世代の状態を反映
    for (int a = 1; a <= BoxSize_W; a++)
      for (int b = 1; b <= BoxSize_H; b++)
        isFilled[a][b] = nextGen[a][b];
  }
}

class Other {
  public void DrawField() {
    for (int x = 0; x < BoxSize_W; x++) {
      for (int y = 0; y < BoxSize_H; y++) {
        stroke(0);
        strokeWeight(4);
        fill(46);
        rect(BoxLength*x, BoxLength*y, BoxLength, BoxLength);
      }
    }
  }
  public void DrawClient() {
    for (int x = 1; x <= BoxSize_W; x++) {
      for (int y = 1; y <= BoxSize_H; y++) {
        if (isFilled[x][y]) {
          stroke(0);
          strokeWeight(4);
          int min = (int)random(0, 100);
          int max = (int)random(155, 255);
          int r = (int)random(min, max);
          int g = (int)random(min, max);
          int b = (int)random(min, max);
          fill(r, g, b);
          rect(BoxLength*(x-1), BoxLength*(y-1), BoxLength, BoxLength);
        }
      }
    }
  }
  public void Respawn(int respX, int respY) {
    int[] Reverse = {-4, -3, -2, -1, 0, 1, 2, 3, 4};
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; ++y) {
        int[] FPosi = {respX + Reverse[x], respY + Reverse[y]};
        int a = (int)random(2);
        boolean b = true;
        if (a == 0) {
          b = false;
        }
        if (b) {
          isFilled[FPosi[0]][FPosi[1]] = true;
        } else {
          isFilled[FPosi[0]][FPosi[1]] = false;
        }
      }
    }
    if (!SoundEffect.isPlaying())
      SoundEffect.play(0);
  }
  public void artClient(int x, int y) {
    int nowX = x;
    int nowY = y;
    fill(180);
    rect(BoxLength*nowX, BoxLength*nowY, BoxLength, BoxLength);
  }
  ArrayList<int[]> InRain = new ArrayList<int[]>();
  boolean[][] InRainisFilled = new boolean[BoxSize_W][BoxSize_H+1];
  ArrayList RainRndList = new ArrayList();
  boolean RainRndInit = true;
  public void rain() {
    if (RainRndInit) {
      rain_init();
      RainRndInit = false;
    }
    for (int m = 0; m < BoxSize_W; m++) {
      InRainisFilled[m][BoxSize_W] = true;
    }
    if (RainRndList.size() == 1) {
      isFullHeight = true;
    }
    int x = (int)RainRndList.get(0);
    RainRndList.remove(0);
    int y = 0;
    for (int n = -1; n < 2; n++) {
      int[] num = {
        x, y+n
      };    
      InRain.add(num);
    }
    for (int i = 0; i < InRain.size (); i++) {
      int[] receive = InRain.get(i);
      int _x = receive[0];
      int _y = receive[1];
      if (InRainisFilled[_x][_y+1]) {
        InRainisFilled[_x][_y] = true;
      } else if (_y < BoxSize_H-1) {
        _y = _y + 1;
        InRain.get(i)[1]=_y;
      }
    }
    //println(InRain.size());
    //draw
    for (int j = 0; j < InRain.size (); j++) {
      int[] receive = InRain.get(j);
      int _x = receive[0];
      int _y = receive[1];
      stroke(0);
      strokeWeight(4);
      fill(240);
      rect(BoxLength*_x, BoxLength*(_y), BoxLength, BoxLength);
    }
  }
  public void rain_init() {
    for (int i = 0; i < BoxSize_W; i++) {
      for (int j = 0; j < BoxSize_W/2; j++) {
        RainRndList.add(i);
      }
    }
    Collections.shuffle(RainRndList);
  }
  int[][] ExclusionList = {{4, 29}, {7, 21}, {7, 22}, {7, 23}, {7, 24}, {7, 25}, {7, 26}, {7, 27}, {7, 28}, {7, 29}, {8, 21}, {8, 25}, {8, 29}, {9, 21}, {9, 25}, {9, 29}, {10, 21}, {10, 25}, {10, 29}, {11, 21}, {11, 25}, {11, 29}, {13, 24}, {13, 25}, {13, 26}, {13, 27}, {13, 28}, {13, 29}, {14, 25}, {15, 25}, {16, 25}, {17, 25}, {17, 26}, {17, 27}, {17, 28}, {17, 29}, {19, 21}, {19, 22}, {19, 23}, {19, 24}, {19, 25}, {19, 26}, {19, 27}, {19, 28}, {19, 29}, {20, 25}, {21, 25}, {22, 25}, {23, 25}, {23, 26}, {23, 27}, {23, 28}, {23, 29}, {25, 25}, {25, 26}, {25, 27}, {25, 28}, {25, 29}, {26, 25}, {26, 29}, {27, 25}, {27, 29}, {28, 25}, {28, 29}, {29, 25}, {29, 26}, {29, 27}, {29, 28}, {29, 29}, {29, 30}, {31, 24}, {31, 25}, {31, 26}, {31, 27}, {31, 28}, {31, 29}, {32, 25}, {33, 25}, {34, 25}, {35, 25}, {35, 26}, {35, 27}, {35, 28}, {35, 29}, {37, 25}, {37, 26}, {37, 27}, {37, 28}, {37, 29}, {38, 25}, {38, 29}, {39, 25}, {39, 29}, {40, 25}, {40, 29}, {41, 25}, {41, 29}, {43, 25}, {43, 26}, {43, 27}, {43, 28}, {43, 29}, {44, 25}, {44, 27}, {44, 29}, {45, 25}, {45, 27}, {45, 29}, {46, 25}, {46, 27}, {46, 29}, {47, 25}, {47, 26}, {47, 27}, {47, 29}};
  public void ExclusionDraw () {
    for (int i = 0; i < ExclusionList.length; i++) {
      int[] EXList = ExclusionList[i];
      int _x = EXList[0];
      int _y = EXList[1];
      float rnd = random(0.23f, 0.25f);
      int max = 255;
      int r = (int)(mouseX*rnd);
      if (r < 46) {
        r = 46;
        max = 46;
      }
      int g = (int)(mouseY*rnd);
      if (g < 46) {
        g = 46;
        max = 46;
      }
      int b = (int)random(46, max);
      stroke(0);
      strokeWeight(4);
      fill(r, g, b);
      rect(BoxLength*_x, BoxLength*_y, BoxLength, BoxLength);
    }
  }
}
  public void settings() {  size(1000, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "dot_Enhance" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
