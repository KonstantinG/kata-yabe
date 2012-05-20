package models;

import play.db.ebean.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Post extends Model {

    @Id
    @Column(name = "POST_ID")
    public Long id;
    
    public String title;
    public Date postedAt;

    @Basic(fetch=FetchType.EAGER)
    @Lob
    public String content;
    
    @ManyToOne(fetch=FetchType.EAGER)
    public User author;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    public List<Comment> comments;


    //@JoinTable(name = "TAGGED_POSTS", joinColumns = { @JoinColumn(name = "POST_ID") }, inverseJoinColumns = { @JoinColumn(name = "NAME") })
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name="TAGGED_POSTS")
    public Set<Tag> tags;

    public static Finder<Long,Post> find = new Finder<Long,Post>(Long.class, Post.class);

    public static List<Post> findPosts(){
        List<Post> result = Post.find.join("author").orderBy("postedAt desc").findList();
        return result;
    }

    public Post(User author, String title, String content) {
        this.comments = new ArrayList<Comment>();
        this.tags = new TreeSet<Tag>();
        this.title = title;
        this.postedAt = new Date();
        this.content = content;
        this.author = author;
    }


    public Post addComment(String author, String content){
        Comment comment = new Comment(this,author,content);
        comment.save();
        this.save();
        return this;
    }

    public static int count(){
        return find.all().size();//TODO remove loading all list. should be loaded count only
    }

    public Post previous(){
        List<Post> posts = Post.find.where().lt("postedAt", postedAt).orderBy("postedAt desc").findList();
        return posts.size() > 0 ? posts.get(0) : null;
    }

    public Post next(){
        List<Post> posts = Post.find.where().gt("postedAt", postedAt).orderBy("postedAt asc").findList();
        return posts.size() > 0 ? posts.get(0) : null;
    }
    
    public Post tagItWith(String name){
        Tag tag = Tag.findOrCreateByName(name);
        tag.posts.add(this);
        tags.add(tag);
        return this;
    }
    
    public static List<Post> findTaggedWith(String tag){
//        raw("select distinct p from Post p join p.tags as t where t.name = ?", tag)
        List<Post> result = Post.find.setAutofetch(true).setDistinct(true).fetch("tags").where().eq("tags.name",tag).findList();
        return result;
    }

}
