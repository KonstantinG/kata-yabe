package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebeaninternal.server.reflect.EnhanceBeanReflect;
import models.Comment;
import models.Post;
import models.User;
import play.*;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.*;

import views.html.*;

import java.util.ArrayList;
import java.util.List;

@With(Application.ParametersAction.class)
@Security.Authenticated(Secured.class)
public class Application extends Controller {

    public static Form<Comment> commentForm = form(Comment.class);    

    //Blogging

    public static Result index() {

        List<Post> postList = Post.findPosts();

        return generateHomePageResult(postList);
    }

    private static Result generateHomePageResult(List<Post> postList) {
        Post frontPost = null;
        List<Post> posts = new ArrayList<Post>();
        if (postList.size() > 0) {
            frontPost = postList.get(0);

            int postCount = postList.size();
//            if(postCount > 10){ TODO WTF???
//                postCount = 10;
//            }
            if(postCount > 0){
                posts = postList.subList(1, postCount);
            }
        }
        return ok(views.html.index.render(frontPost, posts));
    }

    public static Result show(Long id){
        Post post = Post.find.join("author").join("comments").where().eq( "id", id ).findUnique();
        return ok( views.html.show.render (post , commentForm ) );
    }
    
    public static Result postComment(Long id){
        Form<Comment> filledCommentForm = commentForm.bindFromRequest( );
        Post post = Post.find.byId( id );
        if( filledCommentForm.hasErrors() ){
            return badRequest( views.html.show.render( post, filledCommentForm ) );
        } else {
            Comment comment = filledCommentForm.get();
            post.addComment(comment.author,comment.content);
            flash("success", "Thanks for posting " + comment.author + "!");
            return redirect(routes.Application.show( id ));
        }
    }
    
    public static Result listTagged(String tag){
        List<Post> posts = Post.findTaggedWith(tag);
        return ok(views.html.tagged.render(posts, tag));
    }
    
    public static class ParametersAction extends Action.Simple {
        public Result call(Http.Context ctx) throws Throwable {
            //TODO to remove -> ctx.args.put("blogTitle", Play.application().configuration().getString("blog.title"));
            ctx.session().put("blogTitle", Play.application().configuration().getString("blog.title"));
            ctx.session().put("blogBaseline", Play.application().configuration().getString("blog.baseline"));
            return delegate.call(ctx);
        }
    }
}