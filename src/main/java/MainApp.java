public class MainApp {

    static volatile char c = 'A';
    static final Object monitor = new Object();

    static class SyncPrintLetter implements Runnable {
        private char currentLetter;
        private char nextLetter;

        public SyncPrintLetter(char currentLetter, char nextLetter) {
            this.currentLetter = currentLetter;
            this.nextLetter = nextLetter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                synchronized (monitor) {
                    try {
                        while (c != currentLetter)
                            monitor.wait();
                        System.out.print(currentLetter);
                        c = nextLetter;
                        monitor.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        new Thread(new SyncPrintLetter('A', 'B')).start();
        new Thread(new SyncPrintLetter('B', 'C')).start();
        new Thread(new SyncPrintLetter('C', 'A')).start();
    }
}
