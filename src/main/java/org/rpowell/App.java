package org.rpowell;

import org.rpowell.services.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private IS3Service s3Service;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args)  throws Exception {
        File file = new File("/home/rpowell/dev/aws/upload.txt");
        s3Service.uploadFile(file);
    }
}
