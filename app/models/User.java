package models;

import play.core.enhancers.PropertiesEnhancer;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.List;

@Entity
public class User extends Model{

    @Id
    public Long id;

    @Constraints.Required
    @Formats.NonEmpty
    public String email;
    public String fullname;
    public String password;
    public boolean isAdmin;


    public User(String email, String fullname, String password) {
        this.email = email;
        this.fullname = fullname;
        this.password = password;
    }

    public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class);

    public static List<User> all(){
        return find.all();
    }
    
    public static void create(User user){
        user.save();
    }
    
    public static void delete(Long id){
        find.ref(id).delete();
    }

    public static int count(){
        return find.findRowCount();
    }
    
    public static User authenticate(String email, String password){
        return find.where().eq("email",email).eq("password",password).findUnique();
    }

    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static boolean isAdmin(String email){
        return findByEmail(email).isAdmin;
    }
}
