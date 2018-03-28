package game;

/**
 * Created by Alek on 3/28/2018.
 */
public abstract class ConditionalSleep extends Thread {

    private int timeout;

    public ConditionalSleep(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            while (!condition()) {
                sleep(250);

                if (System.currentTimeMillis() > startTime + timeout)
                    break;
            }
            call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract boolean condition();

    public abstract void call();

}
