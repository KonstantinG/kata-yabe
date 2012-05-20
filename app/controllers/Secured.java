package controllers;

import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Http.Context ctx){
        return ctx.session().get("email");
    }
    
    @Override
    public Result onUnauthorized(Http.Context ctx){
        return redirect(routes.LoginController.login());
    }
    
    public static boolean isAdmin(){
        boolean result = User.isAdmin(Http.Context.current().session().get("email"));
        return result;
    }
    
}
