import java.io.*;
import javax.sound.sampled.*;

public class Car {

    public final double LENGTH = 24;
    public final double WIDTH = 12;

    public final double TERMINAL_VELOCITY = 3.5;

    public Clip clip;

    public Clip clipC;
    public Clip clipD;
    public Clip clipE;
    public Clip clipF;
    public Clip clipG;

    public double xFront;
    public double yFront;

    public double xFrontprev;
    public double yFrontprev;

    public double xBL;
    public double yBL;
    public double xBR;
    public double yBR;
    public double v;
    public double ax;
    public double ay;

    public Location FR;
    public Location FL;
    public Location BR;
    public Location BL;

    public int playerNum;

    public double angle;

    public boolean[] checkPoints = { false, false, false, false, false };
    public boolean[] laps = { false, false, false };
    public int lap = 1;

    public Car(double xFront, double yFront, /* double xBL, double yBL, double xBR, double yBR, */ double v,
            double ax, double ay, double angle)
            throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this.xFront = xFront;
        this.yFront = yFront;
        // this.xBL = xBL;
        // this.yBL = yBL;
        // this.xBR = xBR;
        // this.yBR = yBR;
        this.v = v;
        this.ax = ax;
        this.ay = ay;
        this.angle = angle;

        // FR = new Location(xFront - , ay)

        clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clip.open(AudioSystem.getAudioInputStream(new File("sounds/ding.wav")));

        clipC = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipC.open(AudioSystem.getAudioInputStream(new File("sounds/C.wav")));

        clipD = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipD.open(AudioSystem.getAudioInputStream(new File("sounds/D.wav")));

        clipE = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipE.open(AudioSystem.getAudioInputStream(new File("sounds/E.wav")));

        clipF = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipF.open(AudioSystem.getAudioInputStream(new File("sounds/F#.wav")));

        clipG = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipG.open(AudioSystem.getAudioInputStream(new File("sounds/G#.wav")));
    }

    public Car(int playerNum) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this.xFront = 400;
        this.yFront = 700;
        // this.xBL = 0;
        // this.yBL = 0;
        // this.xBR = 0;
        // this.yBR = 0;
        this.v = 0;
        this.ax = 0;
        this.ay = 0;
        this.angle = 0;

        this.playerNum = playerNum;

        clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clip.open(AudioSystem.getAudioInputStream(new File("sounds/ding.wav")));

        clipC = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipC.open(AudioSystem.getAudioInputStream(new File("sounds/C.wav")));

        clipD = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipD.open(AudioSystem.getAudioInputStream(new File("sounds/D.wav")));

        clipE = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipE.open(AudioSystem.getAudioInputStream(new File("sounds/E.wav")));

        clipF = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipF.open(AudioSystem.getAudioInputStream(new File("sounds/F#.wav")));

        clipG = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
        clipG.open(AudioSystem.getAudioInputStream(new File("sounds/G#.wav")));
    }

    // public Car() {
    // x = 0;
    // y = 0;
    // vx = 0;
    // vy = 0;
    // ax = 0;
    // ay = 0;

    // angle = 0;
    // }

    public void updatePosition() {
        xFrontprev = xFront;
        yFrontprev = yFront;
        xFront += v * Math.sin(angle - (angle % 0.1));
        yFront -= v * Math.cos(angle - (angle % 0.1));

        checkpoint();

        if (xFront > Client.BOUNDS_WIDTH)
            xFront = Client.BOUNDS_WIDTH;
        if (xFront < 0)
            xFront = 0;

        if (yFront > Client.BOUNDS_HEIGHT)
            yFront = Client.BOUNDS_HEIGHT;
        if (yFront < 0)
            yFront = 0;
        // xBL += v * Math.sin(angle);
        // yBL += v * Math.cos(angle);

        // xBR += vx;
        // yBR += vy;
    }

    public void rotate(double ang) {
        angle += ang;
    }

    public void setPosition(double xFront, double yFront) {

        xFrontprev = this.xFront;
        yFrontprev = this.yFront;
        this.xFront = xFront;
        this.yFront = yFront;

        if (xFront > Client.BOUNDS_WIDTH)
            xFront = Client.BOUNDS_WIDTH;
        else if (xFront < 0)
            xFront = 0;

        if (yFront > Client.BOUNDS_HEIGHT)
            yFront = Client.BOUNDS_HEIGHT;
        else if (yFront < 0)
            yFront = 0;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setVelocity(double v) {
        this.v = v;
    }

    public void changeVelocity(double deltaV) {
        this.v += deltaV;
        if (this.v > TERMINAL_VELOCITY)
            this.v = TERMINAL_VELOCITY;
        else if (this.v < -1 * TERMINAL_VELOCITY)
            this.v = -1 * TERMINAL_VELOCITY;
    }

    public void checkpoint() {
        if (xFrontprev > 803 && xFront < 803 && yFront + LENGTH > 120 && yFront + LENGTH < 220) {
            checkPoints[0] = true;
            System.out.println("cp 1");

            clipC.setFramePosition(0);
            clipC.start();
        }
        if (checkPoints[0] && xFrontprev > 159 && xFront < 159 && yFront + LENGTH > 120 && yFront + LENGTH < 220) {
            checkPoints[1] = true;
            System.out.println("cp 2");

            clipD.setFramePosition(0);
            clipD.start();
        }
        if (checkPoints[1] && xFrontprev < 594 && xFront > 594 && yFront + LENGTH > 289 && yFront + LENGTH < 388) {
            checkPoints[2] = true;
            System.out.println("cp 3");

            clipC.setFramePosition(0);
            clipC.start();
        }
        if (checkPoints[2] && xFrontprev > 235 && xFront < 235 && yFront + LENGTH > 459 && yFront + LENGTH < 557) {
            checkPoints[3] = true;
            System.out.println("cp 4");

            clipD.setFramePosition(0);
            clipD.start();
        }
        if (checkPoints[3] && xFrontprev < 809 && xFront > 809 && yFront + LENGTH > 629 && yFront + LENGTH < 727) {
            checkPoints[4] = true;
            System.out.println("cp 5");

            clipC.setFramePosition(0);
            clipC.start();
        }
        if (checkPoints[4] && yFrontprev + LENGTH > 420 && yFront + LENGTH < 420 && xFront > 1070 && xFront < 1170) {
            if (!laps[0]) {
                checkPoints = new boolean[] { false, false, false, false, false };
                laps[0] = true;
                lap = 2;
            } else if (!laps[1]) {
                checkPoints = new boolean[] { false, false, false, false, false };
                laps[1] = true;
                lap = 3;
            } else if (!laps[2]) {
                checkPoints = new boolean[] { false, false, false, false, false };
                laps[2] = true;
            }

            clip.setFramePosition(0);
            clip.start();
        }
    }
}
