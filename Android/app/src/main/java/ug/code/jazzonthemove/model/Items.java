package ug.code.jazzonthemove.model;

public class Items {
   String id, name, poster, banner, link, type, desc;

   public Items(String id, String name, String poster, String banner, String link, String type, String desc) {
      this.id = id;
      this.name = name;
      this.poster = poster;
      this.banner = banner;
      this.link = link;
      this.type = type;
      this.desc = desc;
   }


   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPoster() {
      return poster;
   }

   public void setPoster(String poster) {
      this.poster = poster;
   }

   public String getBanner() {
      return banner;
   }

   public void setBanner(String banner) {
      this.banner = banner;
   }

   public String getLink() {
      return link;
   }

   public void setLink(String link) {
      this.link = link;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getDesc() {
      return desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }
}
