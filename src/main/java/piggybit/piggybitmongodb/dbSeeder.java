package piggybit.piggybitmongodb;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class dbSeeder implements CommandLineRunner {
    private UserRepository userRepository;

    public dbSeeder(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... strings) throws Exception{

        User user1 = new User("DanaSzapiro", "password", "Dana", "Szapiro", "danasz@bu.edu");
        User user2 = new User("IvanWong", "Password2", "Ivan", "Wong", "Ivan@bu.edu");

        List<User> users = Arrays.asList(user1, user2);
        userRepository.deleteAll();
        userRepository.save(users);

    }
}
