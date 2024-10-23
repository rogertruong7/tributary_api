package tributary.cli;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLoader {
    public static JSONObject getJSON(String fileName) {
        try {
            URL resourceUrl = FileLoader.class.getClassLoader().getResource("events/" + fileName + ".json");

            if (resourceUrl != null) {
                // Convert URL to File to Path
                File file = new File(resourceUrl.getFile());
                Path path = file.toPath();
                String content = new String(Files.readAllBytes(path));

                JSONObject jsonObject = new JSONObject(content);

                // System.out.println(jsonObject.toString(4));
                return jsonObject;
            } else {
                System.out.println("File not found in resources!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
