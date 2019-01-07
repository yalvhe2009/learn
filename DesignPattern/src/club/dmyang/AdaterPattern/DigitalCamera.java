package club.dmyang.AdaterPattern;
public class DigitalCamera implements IMobileDevice{
    @Override
    public void read() {
        System.out.println("读数码相机");
    }
    @Override
    public void write() {
        System.out.println("写数码相机");
    }
}
