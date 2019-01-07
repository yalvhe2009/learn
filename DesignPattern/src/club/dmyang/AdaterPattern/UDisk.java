package club.dmyang.AdaterPattern;
// U盘
public class UDisk implements IMobileDevice{
    @Override
    public void read() {
        System.out.println("读U盘");
    }
    @Override
    public void write() {
        System.out.println("写U盘");
    }
}
