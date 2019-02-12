import ddf.minim.*;
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
void setup() {
  size(1000, 1000);
  frameRate(60);
  minim = new Minim(this);
  Song = minim.loadFile("EDM_NEW_LIFE.mp3");
  SoundEffect = minim.loadFile("Respawn.mp3");
  ai = new AI();
  other = new Other();
  Song.play();
  Song.loop();
}

void draw() {
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
void mouseReleased (){
  isClick = true;
  print("a");
}
