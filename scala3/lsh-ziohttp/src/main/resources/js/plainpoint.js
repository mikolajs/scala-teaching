
class PlainPoint {
  constructor(X, Y){
    this.x = X;
    this.y = Y;
  }

  add(point){
    this.x += point.x;
    this.y += point.y;
  }
}
