package springandreact.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ExampleController {
    @GetMapping("/api/example")
    public String message() {
        return "Hello, React! This message is from spring!";
    }
}
