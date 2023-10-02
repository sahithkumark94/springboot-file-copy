package com.sahith.FileTransfer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.channels.FileChannel;

@SpringBootApplication
@RestController
public class FileApplication {

    @Value("${sourceFile}")
    private String sourcePath;

    @Value("${destinationPath}")
    private String destinationPath;
    @Value("${fileName}")
    private String fileName;

    @Value("${batchFilePath}")
    private String batchFilePath;


    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class,args);
        System.out.println("=============THE JAVA IS RUNNING===========");
    }
    @GetMapping("/copy")
    public ResponseEntity<?> copyFiles(@RequestParam String fileName){
        try{
            System.out.println("THE SOURCE FILE PATH IS :"+sourcePath);
            System.out.println("THE DESTINATION PATH IS :"+destinationPath);
            File sourceFile = new File(sourcePath);
            File destFile = new File(destinationPath);
            if (!sourceFile.exists()){
                System.out.println("THE SOURCE FILE PATH NOT EXIST");
            }
            if (!destFile.exists()){
                System.out.println("THE DESTINATION FILE PATH NOT EXIST");
            }
            String dest = destinationPath+fileName;
            String source = sourcePath+fileName;
            System.out.println("dest location path and file name"+dest+"\n"+"source :"+source);
            File writeLocationPath = new File(source);
            FileChannel sourceChannel = null;
            FileChannel destinationChannel = null;
            try {
                sourceChannel = new FileInputStream(source).getChannel();
                destinationChannel = new FileOutputStream(dest).getChannel();
                destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            } finally {
                sourceChannel.close();
                destinationChannel.close();
            }
            System.out.println("destiationPath :"+writeLocationPath.getAbsolutePath());
            if (writeLocationPath.delete()) {
                System.out.println("THE FILE IS DELETED FORM THE SOURCE LOCATION");
            } else {
                System.out.println("Failed to delete the file.");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Success");

        }catch (Exception e){
            System.out.println("ERROR :"+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping("/batch")
    public ResponseEntity<?> getPath(@RequestParam String fileName){
        System.out.println("file Name :"+fileName);
        try {
            String filePath = batchFilePath+" "+fileName;
            System.out.println("the batch file path :"+filePath);
            Process p = Runtime.getRuntime().exec(filePath);
            System.out.println("exit"+p);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR :"+e);
        }
    }
}
