package pw.stego.network.container.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Util class for work with HTML (basically, downloading by url)
 * Created by lina on 18.01.17.
 */
public class HTML {
    public static String getHTML(String urlString, String charset) throws MalformedURLException {
        URL url = new URL(urlString);
        StringBuilder stringBuffer = new StringBuilder();

        try (
                InputStream inputStream = url.openConnection().getInputStream();
                BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(inputStream, charset))
        ) {
            int numCharsRead;
            char[] charArray = new char[1024];

            while ((numCharsRead = inputStreamReader.read(charArray)) > 0)
                stringBuffer.append(charArray, 0, numCharsRead);
        } catch (IOException e) {
            return "";
        }

        return stringBuffer.toString();
    }
}
