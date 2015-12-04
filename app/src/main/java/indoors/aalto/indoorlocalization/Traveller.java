package indoors.aalto.indoorlocalization;


public class Traveller {

    private float velocity;
    private long lastTimeStamp;

    public Traveller() {
        velocity = 0.0f;
        lastTimeStamp = 0;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
