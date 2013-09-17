package my.com.tm.workgroup.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import my.com.tm.workgroup.model.FileUpload;
import my.com.tm.workgroup.util.DataExtractorUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileUploadController {

    public final static String INPUT_PATH = "/WEB-INF/input";
    public final static String OUTPUT_PATH = "/data";

    @Autowired
    ServletContext context;

    private File getUploadDir() {
        String directoryPath = context.getRealPath(INPUT_PATH);
        File dir = new File(directoryPath);
        return dir;
    }

    private List<File> getUploadedFiles() {
        File dir = getUploadDir();
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
    
    // TODO: make file processing into one method
    private void processFiles() {
        
    }

    @RequestMapping(value = "/fileUpload", method = RequestMethod.GET)
    public ModelAndView uploadForm() {
        ModelAndView modelAndView = new ModelAndView("fileUpload");
        modelAndView.addObject("fileUpload", new FileUpload());
        modelAndView.addObject("uploadedFiles", getUploadedFiles());
        return modelAndView;
    }

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public ModelAndView uploadFile(@ModelAttribute FileUpload fileUpload) {
        ModelAndView modelAndView = new ModelAndView("fileUpload");

        MultipartFile multipartFile = fileUpload.getFile();

        String fileName = "";

        if (multipartFile != null) {
            fileName = multipartFile.getOriginalFilename();

            // TODO: process uploaded file properly
            InputStream is;
            try {
                String directoryPath = context.getRealPath(INPUT_PATH);
                File directory = new File(directoryPath);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                is = multipartFile.getInputStream();
                FileUtils.copyInputStreamToFile(is, new File(directory, fileName));

                String outputDirPath = context.getRealPath(OUTPUT_PATH);
                File outputDir = new File(outputDirPath);

                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                } else {
                    // TODO: empty output dir properly
                    boolean deleted = true;
                    for (File file : outputDir.listFiles()) {
                        if (!file.delete()) {
                            deleted = false;
                        }
                    }
                    if (!deleted) {
                        System.err.println("Not able to empty folder " + outputDir.getAbsolutePath());
                    }
                }

                DataExtractorUtil.extractFiles(directoryPath, outputDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        modelAndView.addObject("uploadedFiles", getUploadedFiles());

        return modelAndView;
    }

    @RequestMapping(value = "/fileUpload/delete/{fileName}.{ext}", method = RequestMethod.GET)
    public ModelAndView deleteUploadedFile(@PathVariable String fileName, @PathVariable String ext) {

        StringBuilder message = new StringBuilder(fileName);
        message = message.append('.').append(ext);
        
        ModelAndView modelAndView = new ModelAndView("fileUpload");

        boolean deleted = false;
        List<File> files = getUploadedFiles();
        for (File file : files) {
            if (file.getName().equals(message.toString())) {
                deleted = file.delete();
            }
        }
        
        if (deleted) {
            message = message.append(" is successfully deleted.");
        } else {
            message = message.append(" is not able to be deleted.");
        }
        modelAndView.addObject("message", message.toString());
        modelAndView.addObject("uploadedFiles", getUploadedFiles());

        return modelAndView;
    }

    // TODO: download the file
    @RequestMapping(value = "/fileUpload/download/{fileName}.{ext}", method = RequestMethod.GET)
    public ModelAndView deleteTeam(@PathVariable String fileName, @PathVariable String ext, HttpServletResponse response) {

        StringBuilder message = new StringBuilder(fileName);
        message = message.append('.').append(ext);
        
        ModelAndView modelAndView = new ModelAndView("fileUpload");

        List<File> files = getUploadedFiles();
        for (File file : files) {
            if (file.getName().equals(message.toString())) {
                
                // TODO: pushing the file
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

        message = message.append(" is ready to be downloaded.");
        modelAndView.addObject("message", message.toString());
        modelAndView.addObject("uploadedFiles", getUploadedFiles());

        return modelAndView;
    }
}
