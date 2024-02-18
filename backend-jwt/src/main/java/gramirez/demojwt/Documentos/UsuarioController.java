package gramirez.demojwt.Documentos;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class UsuarioController {

    @PostMapping("factura")
    public String loggedin() {
        return "documentos come in exitoso";
    }
}
