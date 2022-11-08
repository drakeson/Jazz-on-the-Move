package ug.code.jazzonthemove.model;

public class Menu {

    String name, desc;
    int image;
    int id_;

    public Menu(String name, String desc, int image, int id_) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.id_ = id_;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }

    public String getDesc() { return desc; }
}
