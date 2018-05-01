package equality;

import java.io.File;
import java.util.ArrayList;

import model.FileRunnerModel;
import model.DirectoryModel;
import model.ResourceModel;
import enumerator.Patterns;
import enumerator.ReservedWords;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtils;
import utils.LineUtils;

public class Main {

    public static Integer MODIFIED_LINES_COUNTER = 0;

    public static void main(String[] args) {
        try {
            ArrayList<File> classes = getClasses();
            equalizeStrings(classes);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ArrayList<File> getClasses(){
        DirectoryModel directoryClass = new DirectoryModel();

        directoryClass.setPath("/Users/victor.rocha/Documents/");

        FileUtils classes = new FileUtils();

        ArrayList<File> project = new ArrayList<File>();
        project.addAll(classes.walkDirs(directoryClass.getPath()));
        
        System.out.println("Classes and Layouts: " + project.size() + "\n");
        
        return project;
    }

    private static void equalizeStrings(ArrayList<File> classes) throws FileNotFoundException, IOException {
        for(File clazz : classes) {
            if(fileNeedsModification(clazz)){
                StringBuilder fileBuilder = readFileModifications(clazz);
                writeFileModifications(clazz, fileBuilder);
            }
        }
        
        System.out.println("");
        System.out.println("Lines Modified: " + MODIFIED_LINES_COUNTER);
    }

    private static StringBuilder readFileModifications(File clazz) throws FileNotFoundException {
        FileReader oldClassFile = new FileReader(clazz);
        FileRunnerModel fileRunnerModel = new FileRunnerModel();
        fileRunnerModel.setReader(new BufferedReader(oldClassFile));
            
        StringBuilder fileBuilder = new StringBuilder();
        
        getNewFileClass(fileBuilder, fileRunnerModel);
               
        return fileBuilder;
    }
    
    private static void writeFileModifications(File clazz, StringBuilder fileBuilder) throws IOException {
        FileWriter fileWriter = new FileWriter(clazz);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileBuilder.toString());
        bufferedWriter.close();
    }
    
    private static void getNewFileClass(StringBuilder fileBuilder, FileRunnerModel fileRunnerModel) {
        while (hasLinesToReadInFile(fileRunnerModel)) {
            String currentLine = fileRunnerModel.getLine();
            boolean isModifyField = LineUtils.isToEqualize(currentLine);
            if(isModifyField){
                System.out.println("Old Line: " + currentLine);
                currentLine = LineUtils.swapEqualizer(currentLine);
                System.out.println("New Line: " + currentLine);
                System.out.println(" ");

                MODIFIED_LINES_COUNTER++;
            }

            fileBuilder.append(currentLine + "\n");
        }
    }
    
    private static boolean hasLinesToReadInFile(FileRunnerModel fileRunnerModel) {
        try {
            String text = fileRunnerModel.getReader().readLine();
            fileRunnerModel.setLine(text);
            
            return text != null;
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    private static boolean fileNeedsModification(File clazz) throws FileNotFoundException {
        FileReader fileReader = new FileReader(clazz);
        FileRunnerModel fileRunnerModel = new FileRunnerModel();

        fileRunnerModel.setReader(new BufferedReader(fileReader));

        while (hasLinesToReadInFile(fileRunnerModel)) {
            boolean isModifyField = LineUtils.isToEqualize(fileRunnerModel.getLine());
            if(isModifyField){    
                return true;
            }
        }

        return false;
    }

}