package club.dmyang.AdaterPattern;
public class Computer {
    /**
     * 背景：一般来讲，电脑的可移动设备厂家设计时就会满足行业的某一规范。（在本文中这个规范就是IMobileDevice接口）
     * 小李现在有一台数码相机和一个U盘，都是满足这个规范的，插入电脑后直接就可以操作。
     * 一天，他从自己家找到一个古董MP3，插上电脑后，发现无法识别，原因是接口不兼容（厂家没有满足ImobileDevice规范）。
     * 这时他想要读写这个MP3，就需要一个适配器：Mp3Adapter
     * @param args
     */
    public static void main(String[] args) {
        //读写U盘和数码相机
        IMobileDevice UDisk = new UDisk();
        IMobileDevice digitalCamera = new DigitalCamera();
        UDisk.read();
        UDisk.write();
        digitalCamera.read();
        digitalCamera.write();
        // Mp3不兼容，需要适配器
        IMobileDevice mp3 = new Mp3Adapter();
        mp3.read();
        mp3.write();
    }
}
