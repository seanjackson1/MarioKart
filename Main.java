public class Main {
    public static void main(String[] args) {
        // start server in new thread
        new Thread() {
            public void run() {
                System.out.println("creating server");
                new Server(4);
            }
        }.start();

        // wait 2 seconds
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }

        // start client in new thread
        new Thread() {
            public void run() {
                System.out.println("creating client 1");
                new Client("10.13.46.243");
            }
        }.start();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        new Thread() {
            public void run() {
                System.out.println("creating client 2");
                new Client("10.13.46.243");
            }
        }.start();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        /* */
        new Thread() {
            public void run() {
                System.out.println("creating client 3");
                new Client("10.13.46.243");
            }
        }.start();

        // try {
        // Thread.sleep(1000);
        // } catch (Exception e) {
        // }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        new Thread() {
            public void run() {
                System.out.println("creating client 4");
                new Client("10.13.46.243");
            }
        }.start();

    }
}
