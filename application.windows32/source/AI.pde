class AI {
  int[] RandRespawn() {
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
  void Add(int id) {
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
  void Think() {
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
