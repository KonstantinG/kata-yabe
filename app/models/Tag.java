package models;

import play.db.ebean.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Tag extends Model implements Comparable<Tag> {

    @Id
    @Column(name = "NAME")
    public String name;

    @ManyToMany(mappedBy="tags", fetch= FetchType.EAGER)
    public Set<Post> posts;
    
    public static Finder<String,Tag> find = new Finder<String,Tag>(String.class,Tag.class);

    private Tag(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
    
    public int compareTo(Tag otherTag){
        return name.compareTo(otherTag.name);
    }
    
    public static Tag findOrCreateByName(String name){
        Tag tag = Tag.find.where().eq("name",name).findUnique();
        if(tag == null){
            tag = new Tag(name);
        }
        return tag;
    }
    
    public static Map getCloud(){
        Map result = new TreeMap();
        List<Tag> tags = Tag.find.setAutofetch(true).join("posts").findList();
        for (Tag tag : tags) {            
            result.put(tag.name,tag.posts.size());
        }
        return result;
    }
}
