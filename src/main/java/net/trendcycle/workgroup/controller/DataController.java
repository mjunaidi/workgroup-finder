package net.trendcycle.workgroup.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DataController {
    public final static String DATA_PATH = "/data";

    @Autowired
    ServletContext context;

    private File getDataDir() {
        String directoryPath = context.getRealPath(DATA_PATH);
        File dir = new File(directoryPath);
        return dir;
    }

    private List<File> getDataFiles() {
        File dir = getDataDir();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            List<File> list = new ArrayList<File>(files.length);
            for (File file : files) {
                list.add(file);
            }
            return list;
        }
        return null;
    }

    // TODO: download the file
    @RequestMapping(value = "/datasource/{fileName}.{ext}", method = RequestMethod.GET)
    public void getData(@PathVariable String fileName, @PathVariable String ext, HttpServletResponse response) {

        StringBuilder message = new StringBuilder(fileName);
        message = message.append('.').append(ext);

        List<File> files = getDataFiles();
        if (files != null && !files.isEmpty()) {
            for (File file : files) {
                if (file.getName().equals(message.toString())) {
                    try {
                        InputStream is = new FileInputStream(file);
                        IOUtils.copy(is, response.getOutputStream());
                        response.flushBuffer();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
