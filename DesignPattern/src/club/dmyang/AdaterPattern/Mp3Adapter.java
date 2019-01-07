package club.dmyang.AdaterPattern;
public class Mp3Adapter implements IMobileDevice {
    Mp3 mp3 = new Mp3();
    @Override
    public void read() {
        mp3.pull();
    }
    @Override
    public void write() {
        mp3.push();
    }
}
