package ru.org.sevn.mydata.views.files;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class FileOpener {
    private String commandName = "explorer"; // "xdg-open";
    {
        commandName = "krusader";
    }

    public String dir (String p) throws Exception {
        final String path = p;
        if (path != null) {
            final File dir = getDir (new File (path.trim ()));
            final JSONObject jo = new JSONObject ();
            jo.put ("exists", dir.exists ());
            jo.put ("isDirectory", dir.isDirectory ());
            jo.put ("canRead", dir.canRead ());
            if (dir.exists () && dir.canRead () && dir.isDirectory ()) {
                ProcessBuilder builder = new ProcessBuilder ();
                //builder.command("exo-open", "--launch", "FileManager");
                System.out.println (">>>>>>" + dir);
                builder.command (commandName, dir.getAbsolutePath ());
                builder.directory (dir);
                builder.start ();
                return null;
            }
            else {
                return "Can't open " + p + " [" + path + "]" + jo.toString (2);
            }
        }
        else {
            return "Can't open " + p + " [" + path + "]";
        }
    }

    private File getDir (final File dir) {
        if (dir.exists () && ! dir.isDirectory ()) {
            final File res = dir.getParentFile ();
            if (res != null) {
                return res;
            }
        }
        return dir;
    }

    private String getFilePath (final String url) {
        try {
            URI uri = new URI (url);
            return uri.getPath ();
        }
        catch (URISyntaxException ex) {
            Logger.getLogger (FileOpener.class.getName ()).log (Level.SEVERE, null, ex);
        }
        if (url != null) {
            if ('/' == File.separatorChar) {
                return url.replace ('\\', File.separatorChar);
            }
            else {
                return url.replace ('/', File.separatorChar);
            }
        }

        return url;
    }
}
