package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        try {
            ArrayList<String> list = new ArrayList<>();

            FileInputStream fis = new FileInputStream(new File("read.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String temp;
            while((temp = br.readLine()) != null) {
                list.add(temp);
            }
            br.close();
            fis.close();

            for (String str : list) {
                String fileName = "";
                ArrayList<String> toWrite = new ArrayList<>();

                URL url = new URL(str);
                InputStream is = url.openStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(is));

                temp = "";
                while ((temp = bReader.readLine()) != null) {
                    if(temp.contains("<title>")) { //Filename
                        fileName = temp.split("<title>")[1].split("</title>")[0];
                        System.out.println(fileName + ": isleniyor...");
                        //toWrite.add(fileName);
                    }
                    if(temp.contains("\"product_brands\":")) { //List of Firms
                        String[] commaList = temp.split("\"product_brands\":")[1].split("\\[")[1].split("\\]")[0].split(",");
                        for (String s : commaList) {
                            //s = strim(s,"\"");
                            if(!toWrite.contains(s + ","))
                                toWrite.add(s  + ",");
                        }
                    }
                }
                toWrite.set(toWrite.size()-1, strim(toWrite.get(toWrite.size()-1), ","));
                FileOutputStream fos = new FileOutputStream("data/elektronik/" + strim(str, "/") + ".json");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write("{\n\t\"" + fileName + "\": [\n");
                for(String w : toWrite)
                    bw.write("\t\t" + w + "\n");
                bw.write("\t]\n}");
                bw.flush();
                bw.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String strim(String str, String regex) {
        try {
            return str.split(regex)[1].split(regex)[0];
        } catch (IndexOutOfBoundsException ex) {
            return str.split(regex)[0].split(regex)[0];
        }
    }
}
