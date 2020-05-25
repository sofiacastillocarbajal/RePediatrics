package pe.work.karique.repediatrics.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.work.karique.repediatrics.models.User;

public class UserRepositories {
    private static UserRepositories userRepositories;
    private List<User> users;

    private UserRepositories() {
        users = new ArrayList<>();
        //users.add(new User("1", "Diana Perez", "Pediatria", 4.2f, "https://i.ibb.co/rwCXzBv/activo-3.png"));
        //users.add(new User("2", "Mario Vega", "Pediatria", 3.6f, "https://i.ibb.co/jrR7HV7/activo-1.png"));
        //users.add(new User("3", "Linda Chanta", "Pediatria", 3.0f, "https://i.ibb.co/KmQMZgG/activo-2.png"));
    }

    public static UserRepositories getInstance(){
        if (userRepositories == null){
            userRepositories = new UserRepositories();
        }
        return userRepositories;
    }

    public List<User> getUsers() {
        return users;
    }
}
