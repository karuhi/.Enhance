import java.util.Collections;
class Other {
  void DrawField() {
    for (int x = 0; x < BoxSize_W; x++) {
      for (int y = 0; y < BoxSize_H; y++) {
        stroke(0);
        strokeWeight(4);
        fill(46);
        rect(BoxLength*x, BoxLength*y, BoxLength, BoxLength);
      }
    }
  }
  void DrawClient() {
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
  void Respawn(int respX, int respY) {
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
  void artClient(int x, int y) {
    int nowX = x;
    int nowY = y;
    fill(180);
    rect(BoxLength*nowX, BoxLength*nowY, BoxLength, BoxLength);
  }
  ArrayList<int[]> InRain = new ArrayList<int[]>();
  boolean[][] InRainisFilled = new boolean[BoxSize_W][BoxSize_H+1];
  ArrayList RainRndList = new ArrayList();
  boolean RainRndInit = true;
  void rain() {
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
  void rain_init() {
    for (int i = 0; i < BoxSize_W; i++) {
      for (int j = 0; j < BoxSize_W/2; j++) {
        RainRndList.add(i);
      }
    }
    Collections.shuffle(RainRndList);
  }
  int[][] ExclusionList = {{4, 29}, {7, 21}, {7, 22}, {7, 23}, {7, 24}, {7, 25}, {7, 26}, {7, 27}, {7, 28}, {7, 29}, {8, 21}, {8, 25}, {8, 29}, {9, 21}, {9, 25}, {9, 29}, {10, 21}, {10, 25}, {10, 29}, {11, 21}, {11, 25}, {11, 29}, {13, 24}, {13, 25}, {13, 26}, {13, 27}, {13, 28}, {13, 29}, {14, 25}, {15, 25}, {16, 25}, {17, 25}, {17, 26}, {17, 27}, {17, 28}, {17, 29}, {19, 21}, {19, 22}, {19, 23}, {19, 24}, {19, 25}, {19, 26}, {19, 27}, {19, 28}, {19, 29}, {20, 25}, {21, 25}, {22, 25}, {23, 25}, {23, 26}, {23, 27}, {23, 28}, {23, 29}, {25, 25}, {25, 26}, {25, 27}, {25, 28}, {25, 29}, {26, 25}, {26, 29}, {27, 25}, {27, 29}, {28, 25}, {28, 29}, {29, 25}, {29, 26}, {29, 27}, {29, 28}, {29, 29}, {29, 30}, {31, 24}, {31, 25}, {31, 26}, {31, 27}, {31, 28}, {31, 29}, {32, 25}, {33, 25}, {34, 25}, {35, 25}, {35, 26}, {35, 27}, {35, 28}, {35, 29}, {37, 25}, {37, 26}, {37, 27}, {37, 28}, {37, 29}, {38, 25}, {38, 29}, {39, 25}, {39, 29}, {40, 25}, {40, 29}, {41, 25}, {41, 29}, {43, 25}, {43, 26}, {43, 27}, {43, 28}, {43, 29}, {44, 25}, {44, 27}, {44, 29}, {45, 25}, {45, 27}, {45, 29}, {46, 25}, {46, 27}, {46, 29}, {47, 25}, {47, 26}, {47, 27}, {47, 29}};
  void ExclusionDraw () {
    for (int i = 0; i < ExclusionList.length; i++) {
      int[] EXList = ExclusionList[i];
      int _x = EXList[0];
      int _y = EXList[1];
      float rnd = random(0.23, 0.25);
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
