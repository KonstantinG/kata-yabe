package models;

import play.core.enhancers.PropertiesEnhancer;
import play.data.validation.Constraints;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public String author;
    public Date postedAt;

    @Constraints.Required
    @Lob
    @Basic(fetch=FetchType.EAGER)
    public String content;

    @ManyToOne
    @JoinColumn(name="POST_ID")
    public Post post;

    public Comment(Post post, String author, String content) {
        this.author = author;        
        this.content = content;
        this.post = post;
        this.postedAt = new Date();
    }
    
    public static Finder<Post,Comment> find = new Finder<Post, Comment>(Post.class, Comment.class);


    public static int count(){
        return find.all().size();//TODO remove loading all list. should be loaded count only
    }

}
