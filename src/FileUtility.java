import java.io.*;
import java.net.URI;
import java.net.URL;

/**
 * Created by Chris-laptop on 9/14/2015.
 */
public class FileUtility {
    public File GetFile(String resource) {
        File file = null;
        URL res = getClass().getResource(resource);
        if (res.toString().startsWith("jar:")) {
            try {
                System.out.println(resource.toString().substring(4));
                InputStream input = getClass().getResourceAsStream(resource);
                file = File.createTempFile("tempfile", ".tmp");
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                file.deleteOnExit();
            } catch (IOException ex) {
                //printStackTrace(ex);
            }
        } else {
            //this will probably work in your IDE, but not from a JAR
            file = new File(res.getPath());
        }

        if (file != null && !file.exists()) {
            throw new RuntimeException("Error: File " + file + " not found!");
        }

        return file;
    }
}
