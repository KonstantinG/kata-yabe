package controllers;

import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class LoginController extends Controller {
    //Authentication

    public static class Login{

        public String email;
        public String password;

        public String validate(){
            if(User.authenticate(email, password) == null){
                return "Invalid user or password";
            }
            return null;
        }
    }

    public static Result login(){
        return ok(login.render(form(Login.class)));
    }

    public static Result authenticate(){
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()){
            return badRequest(login.render(loginForm));
        } else {
            session("email",loginForm.get().email);
            return redirect(routes.Application.index());
        }
    }

    public static Result logout(){
        session().clear();
        flash("success","You've been logged out");
        return redirect(routes.LoginController.login());
    }


}
