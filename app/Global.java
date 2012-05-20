import models.Post;
import models.User;

import com.avaje.ebean.Ebean;

import play.*;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

public class Global extends GlobalSettings {
    public void onStart(Application app){
        InitialData.insert(app);
    }
    
    static class InitialData{
        public static void insert(Application app){
            if(Ebean.find(User.class).findRowCount() == 0){
                Map<String,List<Object>> all = (Map<String,List<Object>>) Yaml.load("initial-data.yml");

                List<Object> users = all.get("users");
                List<Object> posts = all.get("posts");
                Ebean.save(users);
                Ebean.save(posts);
            }
        }
    }
}
