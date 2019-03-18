package net.algelier.servermanagement.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MiscUtils {

    public static File getJarFolder() {
        URL url;
        String extUrl;

        try {
            url = MiscUtils.class.getProtectionDomain().getCodeSource().getLocation();
        } catch (SecurityException e) {
            url = MiscUtils.class.getResource(MiscUtils.class.getSimpleName() + ".class");
        }

        extUrl = url.toExternalForm();

        if (extUrl.endsWith(".jar"))
            extUrl = extUrl.substring(0, extUrl.lastIndexOf("/"));

        else {
            String suffix = "/" + (MiscUtils.class.getName()).replace(".", "/") + ".class";
            extUrl = extUrl.replace(suffix, "");

            if (extUrl.startsWith("jar:") && extUrl.endsWith(".jar!"))
                extUrl = extUrl.substring(4, extUrl.lastIndexOf("/"));
        }

        try {
            url = new URL(extUrl);
        } catch (MalformedURLException ignored) {}

        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            return new File(url.getPath());
        }
    }

    public static String getApplicationDirectory() {
        String jarDir = null;

        try {
            CodeSource codeSource = MiscUtils.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(URLDecoder.decode(codeSource.getLocation().toURI().getPath(), StandardCharsets.UTF_8));
            jarDir = jarFile.getParentFile().getPath();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return jarDir + "/";
    }

    public static String getSHA1(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");

        try (FileInputStream fileInputStream = new FileInputStream(file)){
            byte[] data = new byte[1024];
            int read = 0;

            while ((read = fileInputStream.read(data)) != -1) {
                sha1.update(data, 0, read);
            }
        }

        byte[] hashBytes = sha1.digest();

        StringBuilder stringBuilder = new StringBuilder();
        for (byte hashByte : hashBytes) {
            stringBuilder.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }

    public static byte[] stringToByte(String ip) {
        String[] split = ip.split(".");
        byte[] result = new byte[4];
        int i = 0;

        for (String number : split) {
            result[i] = Byte.valueOf(number);
            i++;
        }

        return result;
    }

    public static int calculServerWeight(String game, int maxSlot,boolean isFriend) {
        game = game.toLowerCase();
        int weight = 0;

        switch (game) {
            //add any type of game you want with the max slots available
            // example :
            case "uhc":
                weight += 40;
                break;

                default:
                    break;
        }

        weight += maxSlot;

        if (isFriend) {
            weight += 50;
        }

        return weight;
    }
}
