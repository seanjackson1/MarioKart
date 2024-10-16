import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.text.PlainDocument;

public class Client extends JComponent implements Runnable, KeyListener, ActionListener {

  public static final int BOUNDS_HEIGHT = 800;
  public static final int BOUNDS_WIDTH = 1201;
  public static final int CAR_WIDTH = 12;
  public static final int CAR_LENGTH = 24;
  public final double TERMINAL_VELOCITY = 3.5;

  public JFrame parent;
  private String[] colors = new String[] { "Red", "Blue", "Green", "Yellow" };

  public final Color trackColor1 = new Color(1, 0, 5);;
  public final Color trackColor2 = new Color(203, 206, 102);

  public final Color backgroundColor = new Color(66, 135, 245);

  private BufferedReader in;
  private PrintWriter out;
  private int x1;
  private int y1;
  private int x2;
  private int y2;

  private Color colorOn;

  private boolean up;
  private boolean down;
  private boolean right;
  private boolean left;

  private boolean onTrack;

  private String startmessage = "";
  private String playerMessage = "";

  private boolean started = false;

  private boolean ended = false;

  private int numPlayers;

  JFrame frame;

  private final int DELAY = 20;

  private int c = 0;

  // private long time = System.currentTimeMillis();

  private int playerNum;

  private Image image;

  private Image[] carImages = {
      new ImageIcon(getClass().getResource("images/red.png")).getImage().getScaledInstance(CAR_WIDTH, CAR_LENGTH,
          Image.SCALE_SMOOTH),
      new ImageIcon(getClass().getResource("images/blue.png")).getImage().getScaledInstance(CAR_WIDTH, CAR_LENGTH,
          Image.SCALE_SMOOTH),
      new ImageIcon(getClass().getResource("images/green.png")).getImage().getScaledInstance(CAR_WIDTH, CAR_LENGTH,
          Image.SCALE_SMOOTH),
      new ImageIcon(getClass().getResource("images/yellow.png")).getImage().getScaledInstance(CAR_WIDTH, CAR_LENGTH,
          Image.SCALE_SMOOTH) };

  private Image trackImage;

  private BufferedImage buffTrack;

  private ArrayList<Car> cars = new ArrayList<Car>();

  private InputStream inputStream;

  private String[] moveMessages = null;

  public Client(String ipAddress) {
    try {
      Socket socket = new Socket(ipAddress, 9000);
      in = new BufferedReader(new InputStreamReader(inputStream = socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    new Thread(this).start();
    frame = new JFrame(colors[playerNum]);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(BOUNDS_WIDTH, BOUNDS_HEIGHT));
    setFocusable(true);
    addKeyListener(this);
    frame.getContentPane().add(this);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    String fileName = "images/blue.png";
    URL url = getClass().getResource(fileName);
    if (url == null)
      throw new RuntimeException("Unable to load:  " + fileName);

    image = new ImageIcon(url).getImage();
    image = image.getScaledInstance(CAR_WIDTH, CAR_LENGTH, Image.SCALE_SMOOTH);

    fileName = "images/trackBlue.png";
    url = getClass().getResource(fileName);
    if (url == null)
      throw new RuntimeException("Unable to load:  " + fileName);

    trackImage = new ImageIcon(url).getImage();

    trackImage = trackImage.getScaledInstance(BOUNDS_WIDTH, BOUNDS_HEIGHT, Image.SCALE_SMOOTH);

    try {
      buffTrack = ImageIO.read(new File("images/trackBlue.png"));
    } catch (IOException e) {
    }

    up = false;
    down = false;
    left = false;
    right = false;

    onTrack = true;

    colorOn = new Color(0, 0, 0);

    send("joined");
  }

  public void paintComponent(Graphics g) {

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, BOUNDS_WIDTH, BOUNDS_HEIGHT);

    g.drawImage(trackImage, 0, 0, null);

    if (cars != null && cars.size() > 0) {

      Font font = new Font("Serif", Font.BOLD, 30);
      g.setFont(font);
      g.drawString("Lap " + cars.get(playerNum).lap + " of 3", 970, 36);

      // Color cool = (new Color(buffTrack.getRGB(100, 100)));
      // System.out.println("Color: " + cool.getRed() + " " + cool.getGreen() + " " +
      // cool.getBlue());
      // long newTime = System.currentTimeMillis();
      // System.out.println(time);
      // System.out.println(newTime + "\n");
      // System.out.println("here");
      // if (Math.abs(time - newTime) > 100) {
      // time = newTime;

      for (int i = 0; i < cars.size(); i++) {

        // if (cars.get(i).angle > 0) {
        // Graphics2D g2 = (Graphics2D) g; // g is a Graphics object
        // AffineTransform old = g2.getTransform();
        // g2.rotate(cars.get(i).angle, cars.get(i).xFront - 20, cars.get(i).yFront +
        // 80);

        // g2.drawImage(image, round(cars.get(i).xFront - 20),
        // round(cars.get(i).yFront), null);

        // g2.rotate(-1 * cars.get(i).angle, cars.get(i).xFront - 20, cars.get(i).yFront
        // + 80);

        // g2.setTransform(old);
        // }
        // else if (cars.get(i).angle < 0) {
        // Graphics2D g2 = (Graphics2D) g; // g is a Graphics object
        // AffineTransform old = g2.getTransform();
        // g2.rotate(cars.get(i).angle, cars.get(i).xFront + 20, cars.get(i).yFront +
        // 80);

        // g2.drawImage(image, round(cars.get(i).xFront - 20),
        // round(cars.get(i).yFront), null);
        // g2.setTransform(old);

        // g2.rotate(-1 * cars.get(i).angle, cars.get(i).xFront + 20, cars.get(i).yFront
        // + 80);
        // g2.setTransform(old);
        // }
        // else {
        // g.drawImage(image, round(cars.get(i).xFront - 20), round(cars.get(i).yFront),
        // null);
        // }

        if (cars.get(i).angle != 0) {
          Graphics2D g2 = (Graphics2D) g; // g is a Graphics object
          AffineTransform old = g2.getTransform();
          g2.rotate(cars.get(i).angle, cars.get(i).xFront, cars.get(i).yFront + CAR_LENGTH);
          g2.drawImage(carImages[cars.get(i).playerNum], round(cars.get(i).xFront - CAR_WIDTH / 2),
              round(cars.get(i).yFront), null);

          // g2.rotate(-1 * cars.get(i).angle, cars.get(i).xFront, cars.get(i).yFront +
          // 80);

          g2.setTransform(old);
        } else {
          g.drawImage(carImages[cars.get(i).playerNum], round(cars.get(i).xFront - CAR_WIDTH / 2),
              round(cars.get(i).yFront), null);
        }

      }

      // g.setColor(Color.RED);
      // g.fillRect((int) cars.get(playerNum).x, (int) cars.get(playerNum).y, 10, 10);

      // g.setColor(Color.CYAN);
      // for (int i = 0; i < cars.size(); i++) {
      // if (i != playerNum)
      // g.fillRect((int) cars.get(i).x, (int) cars.get(i).y, 10, 10);
    }
  }

  public void run() {
    while (true) {
      try {
        listen();
      } catch (LineUnavailableException | UnsupportedAudioFileException e) {
        e.printStackTrace();
      }
    }
  }

  public void cycle() {
    if (cars != null && cars.size() > 1) {

      if (cars.get(playerNum).laps[2]) {
        out.println("finished");
      }

      // boolean onTrack = (((((Math.abs(colorOn.getRed() - trackColor1.getRed()))
      // + (Math.abs(colorOn.getGreen() - trackColor1.getGreen()))
      // + (Math
      // .abs(colorOn.getBlue() - trackColor1.getBlue()))) < 20
      // || ((Math.abs(colorOn.getRed() - trackColor2.getRed()))
      // + (Math.abs(colorOn.getGreen() - trackColor2.getGreen()))
      // + (Math
      // .abs(colorOn.getBlue() - trackColor2.getBlue()))) < 20)));

      if (Math.random() < 0.4)
        onTrack = (!(((Math.abs(colorOn.getRed() - backgroundColor.getRed()))
            + (Math.abs(colorOn.getGreen() - backgroundColor.getGreen()))
            + (Math.abs(colorOn.getBlue() - backgroundColor.getBlue()))) < 3));

      if (up) {
        if (onTrack)
          cars.get(playerNum).changeVelocity(0.05);
        else
          cars.get(playerNum).setVelocity(0.25);
      } else {
        if (!onTrack && cars.get(playerNum).v > 0)
          cars.get(playerNum).setVelocity(0.25);

      }
      if (down) {
        if (onTrack)
          cars.get(playerNum).changeVelocity(-0.05);
        else
          cars.get(playerNum).setVelocity(-0.25);
      } else {
        if (!onTrack && cars.get(playerNum).v < 0)
          cars.get(playerNum).setVelocity(-0.25);

      }
      if (right) {
        if (cars.get(playerNum).v > 0) {
          // if (onTrack)
          cars.get(playerNum).rotate(6 * Math.abs((cars.get(playerNum).v + 5) / (TERMINAL_VELOCITY
              + 5)) * Math.PI / 180);
          // else
          // cars.get(playerNum)
          // .rotate(3 * Math.abs((cars.get(playerNum).v + 5) / (TERMINAL_VELOCITY + 5)) *
          // Math.PI / 180);
          // cars.get(playerNum).changeVelocity(-0.01);
        } else if (cars.get(playerNum).v < 0) {
          // if (onTrack)
          cars.get(playerNum).rotate(4 * Math.abs((cars.get(playerNum).v + 5) / (TERMINAL_VELOCITY
              + 5)) * Math.PI / -180);
          // else
          // cars.get(playerNum).rotate(2 * Math.abs((cars.get(playerNum).v + 5) /
          // (TERMINAL_VELOCITY
          // + 5)) * Math.PI / -180);
          // cars.get(playerNum).changeVelocity(0.01);
        }
      }
      if (left) {
        if (cars.get(playerNum).v > 0) {
          // if (onTrack)
          cars.get(playerNum)
              .rotate(6 * Math.abs((cars.get(playerNum).v + 5) / (TERMINAL_VELOCITY + 5)) * Math.PI / -180);
          // else
          // cars.get(playerNum)
          // .rotate(3 * Math.abs((cars.get(playerNum).v + 5) / (TERMINAL_VELOCITY + 5)) *
          // Math.PI / -180);
          // cars.get(playerNum).changeVelocity(-0.01);
        } else if (cars.get(playerNum).v < 0) {
          // if (onTrack)
          cars.get(playerNum)
              .rotate(4 * Math.abs((cars.get(playerNum).v + 5) / (TERMINAL_VELOCITY + 5)) * Math.PI / 180);
          // else
          // cars.get(playerNum)
          // .rotate(2 * Math.abs((cars.get(playerNum).v + 5) / (TERMINAL_VELOCITY + 5)) *
          // Math.PI / 180);
          // cars.get(playerNum).changeVelocity(0.01);
        }
      }

      if (cars.get(playerNum).v > 0) {
        // if (!((((Math.abs(colorOn.getRed() - trackColor1.getRed()))
        // + (Math.abs(colorOn.getGreen() - trackColor1.getGreen()))
        // + (Math
        // .abs(colorOn.getBlue() - trackColor1.getBlue()))) < 20
        // || ((Math.abs(colorOn.getRed() - trackColor2.getRed()))
        // + (Math.abs(colorOn.getGreen() - trackColor2.getGreen()))
        // + (Math
        // .abs(colorOn.getBlue() - trackColor2.getBlue()))) < 20))) {
        // if (cars.get(playerNum).v > 0.25)
        // cars.get(playerNum).changeVelocity(-0.25);
        // // else
        // // cars.get(playerNum).changeVelocity(-1 * cars.get(playerNum).v);
        // }

        if (cars.get(playerNum).v > 0.025) {
          cars.get(playerNum).changeVelocity(-0.025);

          // if (cars.get(playerNum).v > 0.04 && !onTrack)
          // cars.get(playerNum).changeVelocity(-0.04);

          // }

          // else
          // cars.get(playerNum).changeVelocity(-1 * cars.get(playerNum).v);

          // else
          // cars.get(playerNum).changeVelocity(-1 * cars.get(playerNum).v);

        }
        if (cars.get(playerNum).v < 0) {
          // if (!((((Math.abs(colorOn.getRed() - trackColor1.getRed()))
          // + (Math.abs(colorOn.getGreen() - trackColor1.getGreen()))
          // + (Math
          // .abs(colorOn.getBlue() - trackColor1.getBlue()))) < 20
          // || ((Math.abs(colorOn.getRed() - trackColor2.getRed()))
          // + (Math.abs(colorOn.getGreen() - trackColor2.getGreen()))
          // + (Math
          // .abs(colorOn.getBlue() - trackColor2.getBlue()))) < 20))) {
          // if (cars.get(playerNum).v < -0.25)
          // cars.get(playerNum).changeVelocity(0.25);
          // // else
          // // cars.get(playerNum).changeVelocity(-1 * cars.get(playerNum).v);
          // }

          if (cars.get(playerNum).v < -0.025)
            cars.get(playerNum).changeVelocity(0.025);

          // if (cars.get(playerNum).v < -0.04 && !onTrack)
          // cars.get(playerNum).changeVelocity(0.04);

          // else
          // cars.get(playerNum).changeVelocity(-1 * cars.get(playerNum).v);
          // else
          // cars.get(playerNum).changeVelocity(-1 * cars.get(playerNum).v);
        }

        if (cars.get(playerNum).xFront < BOUNDS_WIDTH && cars.get(playerNum).xFront > 0
            && cars.get(playerNum).yFront + CAR_LENGTH < BOUNDS_HEIGHT && cars
                .get(playerNum).yFront + CAR_LENGTH > 0)
          colorOn = new Color(
              buffTrack.getRGB((int) (cars.get(playerNum).xFront), (int) (cars.get(playerNum).yFront + CAR_LENGTH)));

        cars.get(playerNum).updatePosition();
        send(
            "moved " + cars.get(playerNum).xFront + " " + cars.get(playerNum).yFront + " " + cars.get(playerNum).angle);

      }

      if (cars.size() > 1)

      {
        cars.get(playerNum).updatePosition();
        repaint();
      }
    }
  }

  public void listen() throws LineUnavailableException, UnsupportedAudioFileException {
    try {
      String line;
      String message = "";
      while (inputStream.available() > 0 && (line = in.readLine()) != null && line != "") {
        message = line;
        String[] tokens = message.split(" ");
        if (tokens[0].equals("moved") && started) {
          moveMessages[Integer.parseInt(tokens[4])] = message;
        } else if (tokens[0].equals("start")) {
          startmessage = message;
        } else if (tokens[0].equals("player")) {
          playerMessage = message;
        } else if (tokens[0].equals("won") && ended == false) {
          ended = true;
          winMessage(Integer.parseInt(tokens[1]));
        } else if (tokens[0].equals("restart")) {
          restart();
        }
      }

      if (moveMessages != null && started) {
        for (String moveMessage : moveMessages) {
          if (moveMessage != null) {
            String[] tokens = moveMessage.split(" ");
            cars.get(Integer.parseInt(tokens[4])).setPosition(Double.parseDouble(tokens[1]),
                Double.parseDouble(tokens[2]));
            cars.get(Integer.parseInt(tokens[4])).setAngle(Double.parseDouble(tokens[3]));
          }
        }
        moveMessages = new String[numPlayers];
      }

      String[] tokens;
      if (!playerMessage.equals("")) {
        tokens = playerMessage.split(" ");
        // Your player number
        playerNum = Integer.parseInt(tokens[1]);
        playerMessage = "";
        repaint();
      } else {
        if (!startmessage.equals("")) {

          tokens = startmessage.split(" ");
          started = true;
          // When it starts, set number of players
          System.out.println("client started");
          frame.setTitle(colors[playerNum]);

          String[] tracks = { "images/trackRed.png", "images/trackBlue.png", "images/trackGreen.png",
              "images/trackYellow.png" };

          String fileName = tracks[playerNum];
          URL url = getClass().getResource(fileName);
          if (url == null)
            throw new RuntimeException("Unable to load:  " + fileName);

          trackImage = new ImageIcon(url).getImage();
          trackImage = trackImage.getScaledInstance(BOUNDS_WIDTH, BOUNDS_HEIGHT, Image.SCALE_SMOOTH);

          int numberOfCars = 4;
          numPlayers = numberOfCars;
          moveMessages = new String[numPlayers];

          for (int i = 0; i < numberOfCars; i++) {
            cars.add(new Car(i));
            cars.get(i).setPosition((1090 + 25 * i), 430);
          }
          // cars.get(playerNum).setPosition(Double.parseDouble(tokens[2]),
          // Double.parseDouble(tokens[3]));
          startmessage = "";

          new Thread() {
            public void run() {

              long sleep;

              while (true) {
                if (ended) {
                  System.out.println("ended");
                  continue;
                }
                cycle();
                repaint();

                sleep = DELAY;

                try {
                  Thread.sleep(sleep);
                } catch (InterruptedException e) {
                  System.out.println("error");
                }
              }
            }
          }.start();
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void keyPressed(KeyEvent e) {
    if (cars != null && cars.size() > 1) {
      // cars.get(playerNum).move(1, 0);
      // send("moved " + (int) cars.get(playerNum).x + " " + (int)
      // cars.get(playerNum).y);

      // up key

      if (e.getKeyCode() == 38) {
        up = true;
      }

      // down key
      if (e.getKeyCode() == 40) {

        down = true;
      }

      // right key
      if (e.getKeyCode() == 39) {

        right = true;
      }

      // left key
      if (e.getKeyCode() == 37) {
        left = true;
      }

    }
  }

  public void keyReleased(KeyEvent e) {
    if (cars != null && cars.size() > 1) {
      // cars.get(playerNum).move(1, 0);
      // send("moved " + (int) cars.get(playerNum).x + " " + (int)
      // cars.get(playerNum).y);

      // up key
      if (e.getKeyCode() == 38) {
        up = false;
      }

      // down key
      if (e.getKeyCode() == 40) {

        down = false;
      }

      // right key
      if (e.getKeyCode() == 39) {

        right = false;
      }

      // left key
      if (e.getKeyCode() == 37) {
        left = false;
      }

    }
  }

  public void keyTyped(KeyEvent e) {
  }

  public int round(double x) {
    return (int) (x + 0.5);
  }

  public void send(String message) {
    if (playerNum == 1) {
      // System.out.println("Client " + playerNum + " sent " + message);
    }
    out.println(message);
    // System.out.println("Client sent: " + message);
  }

  public void winMessage(int player) {
    // display win screen, wait for button to restart game
    parent = new JFrame();
    parent.setPreferredSize(new Dimension(300, 300));

    GridLayout layout = new GridLayout(2, 0);
    parent.setLayout(layout);

    JLabel label = new JLabel(colors[player] + " Won!", SwingConstants.CENTER);

    parent.add(label);

    JButton button = new JButton();
    button.setPreferredSize(new Dimension(100, 100));
    button.addActionListener(this);
    button.setText("Restart");

    parent.add(button);
    parent.pack();
    parent.setLocationRelativeTo(null);
    parent.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    out.println("restart");
  }

  public void restart() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
    parent.dispose();
    cars.clear();
    for (int i = 0; i < 4; i++) {
      cars.add(new Car(i));
      cars.get(i).setPosition((1090 + 25 * i), 430);
    }
    up = false;
    down = false;
    right = false;
    left = false;
    ended = false;
  }
}
