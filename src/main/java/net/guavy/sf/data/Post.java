package net.guavy.sf.data;

public class Post {
    public String name;
    public String description;
    public String author;
    public String icon;
    public long dateCreated;

    public Post(String name, String description, String author, String icon, long dateCreated) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.icon = icon;
        this.dateCreated = dateCreated;
    }
}
