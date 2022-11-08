package ug.code.jazzonthemove.model;

public class Slider {

    String id, image, url, name;

    public Slider() {
    }

    public Slider(String id, String image, String url, String name) {
        this.id = id;
        this.image = image;
        this.url = url;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
