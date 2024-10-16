public class SimpleCar {

    public double x;
    public double y;

    public SimpleCar(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move(double deltaX, double deltaY) {
        x += deltaX;
        y += deltaY;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
