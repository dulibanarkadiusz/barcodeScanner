package example.scanner;

/**
 * Created by Hanna on 10.01.2017.
 */

public class Computer {
    private int id = -1;
    private String barcode = "";
    private String ip = "";
    private String mac = "";
    private String name = "";
    private String location = "";
    private String os_name = "";
    private String key = "";
    private String software = "";

    public Computer(){}

    public Computer(int id, String barcode,String ip,String mac, String name,String location, String os_name, String key, String software){
        this.id=id;
        this.barcode = barcode;
        this.mac = mac;
        this.name = name;
        this.location = location;
        this.os_name = os_name;
        this.key = key;
        this.software = software;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOs_name() {
        return os_name;
    }

    public void setOs_name(String os_name) {
        this.os_name = os_name;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getSoftware() { return software; }

    public void setSoftware(String software) { this.software = software; }
}
