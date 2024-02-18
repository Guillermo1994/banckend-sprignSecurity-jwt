package gramirez.demojwt.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            UserDTO userDTO = UserDTO.builder()
                    .id(user.id)
                    .username(user.username)
                    .firstname(user.firstname)
                    .lastname(user.lastname)
                    .country(user.country)
                    .build();
            return userDTO;
        }

        return null;
    }

    public UserResponse updateUser(UserRequest userRequest) {
        User user = User.builder()
                .id(userRequest.id)
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.lastname)
                .country(userRequest.getCountry())
                .role(Role.USER)
                .build();

        userRepository.updateUser(user.id, user.firstname, user.lastname, user.country);

        return new UserResponse("El usuario se Actualizo correctamente");

    }
}
