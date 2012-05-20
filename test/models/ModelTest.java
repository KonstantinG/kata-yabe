package models;

import models.User;

import org.junit.*;

import play.libs.Yaml;
import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import java.util.List;
import java.util.Map;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import javax.persistence.*;


public class ModelTest {

    @Test
    public void simpleCheck(){
        int sum = 1 + 1;
        assertThat(sum).isEqualTo(2);
    }

    @Test
    public void findById(){
        running(fakeApplication(inMemoryDatabase()), new Runnable(){
            public void run(){
                User ivan = User.find.byId(101L);
                assertThat(ivan.fullname).isEqualTo("Ivan Ivanov");
                User petr = User.find.byId(102L);
                assertThat(petr.fullname).isEqualTo("Petr Petrov");
            }
        });
    }

    @Test
    public void createAndRetrieveUser(){
        running(fakeApplication(inMemoryDatabase()), new Runnable(){
            public void run(){
                new User("bob@kmail.com","Bob Shaken","secret").save();
                User bob = User.find.where().eq("email", "bob@kmail.com").findList().get(0);
                assertThat(bob).isNotNull();
            }
        });
    }

    @Test
    public void createPost(){
        running(fakeApplication(inMemoryDatabase()), new Runnable(){
            public void run(){
                int initialPostCount = Post.find.all().size();
                User ray = new User("ray@kmail.com","Ray Kurzweil","secret");
                ray.save();
                Post newPost = new Post(ray,"Hello World!","This is my first post to this blog!");
                newPost.save();
                
                assertThat(Post.find.all().size() - initialPostCount).isEqualTo(1);

                List<Post> posts = Post.find.where().eq("author_id",ray.id).findList();
                assertThat(posts.size()).isEqualTo(1);
                Post firstPost = posts.get(0);

                assertThat(firstPost.author.id).isEqualTo(ray.id);
                assertThat(firstPost.title).isEqualTo("Hello World!");
                assertThat(firstPost.content).isEqualTo("This is my first post to this blog!");
                assertThat(firstPost.postedAt).isNotNull();
               
            }
        });
    }
    
    @Test
    public void postComments(){
        running(fakeApplication(inMemoryDatabase()), new Runnable(){
            public void run(){
                User john = new User("jhon@kmail.com","John Smart", "secret");
                john.save();
                Post johnPost = new Post(john,"This is second post!","Content of the second post");
                johnPost.save();
                new Comment(johnPost,"Trevor","Awesome man! Keep posting.").save();
                new Comment(johnPost,"Steve","Nice blog. Interesting ideas.").save();

                List<Comment> comments = Comment.find.where().eq("post_id",johnPost.id).findList();
                
                assertThat(comments.size()).isEqualTo(2);

                //TODO
            }
        });
    }

    @Test
    public void useTheCommentRelation(){
        running(fakeApplication(inMemoryDatabase()),new Runnable(){
            public void run(){
                User john = new User("jhon@kmail.com","John Smart", "secret");
                john.save();
                Post johnPost = new Post(john,"This is second post!","Content of the second post");
                johnPost.save();
                johnPost.addComment("Jeff", "My first comment");
                johnPost.addComment("Tom", "I knew that!");

                johnPost = Post.find.where().eq("author_id",john.id).findList().get(0);

                assertThat(johnPost.comments.size()).isEqualTo(2);

                assertThat(johnPost.comments.get(0).author).isEqualTo("Jeff");

                int initialCommentsCount = Comment.count();
                johnPost.delete();
                assertThat(initialCommentsCount - Comment.count()).isEqualTo(2);
            }
        });
    }

    @Test
    public void fullTest(){
        //TODO Map<String,List<Object>> all = (Map<String,List<Object>>) Yaml.load("initial-data.yml");
    }
    
    @Test
    public void testTags() {
        running(fakeApplication(inMemoryDatabase()), new Runnable(){
            public void run(){
                User john = new User("jhon3@kmail.com","John Smart3", "secret");
                john.save();
                Post newPost = new Post(john,"Hello World!","This is my first post to this blog!");
                newPost.save();
                Post secondPost = new Post(john,"This is second post!","Content of the second post");
                secondPost.save();

                newPost.tagItWith("Red").tagItWith("Blue").save();
                secondPost.tagItWith("Red").tagItWith("Green").save();

                assertThat(Post.findTaggedWith("Red").size()).isEqualTo(2);
                assertThat(Post.findTaggedWith("Blue").size()).isEqualTo(1);
                assertThat(Post.findTaggedWith("Green").size()).isEqualTo(1);
                assertThat(Post.findTaggedWith("Yellow").size()).isEqualTo(0);
                
                Map cloud = Tag.getCloud();
                assertThat(cloud.get("Red")).isEqualTo(2);
                assertThat(cloud.get("Green")).isEqualTo(1);
                assertThat(cloud.get("Blue")).isEqualTo(1);
            }
        });
    }
}
