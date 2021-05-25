import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class main {
    public static boolean withGUI;

    public static JTextArea USERS;

    public static JTextArea disk1;
    public static JTextArea disk2;

    public static JTextArea printer1;
    public static JTextArea printer2;
    public static JTextArea printer3;

    public static double speed = 1.0;
    public static void main(String[] args) {
        class GUIFrame extends JFrame {
            private static final int FRAME_WIDTH = 1000;
            private static final int FRAME_HEIGHT = 1000;
    
            private static final int AREA_ROWS = 10;
            private static final int AREA_COLUMNS = 20;
    
            private JRadioButton slowButton;
            private JRadioButton regButton;
            private JRadioButton fastButton; 

            private ActionListener listener; 
    
            public GUIFrame() {
                JPanel userPanel = new JPanel();
                userPanel.setLayout(new BorderLayout()); 

                JLabel userLabel = new JLabel("USER LIST:");

                main.USERS = new JTextArea(AREA_ROWS, AREA_COLUMNS);
                USERS.setEditable(false);
                userPanel.add(userLabel, BorderLayout.NORTH);
                userPanel.add(USERS, BorderLayout.WEST);

                main.disk1 = new JTextArea(AREA_ROWS, AREA_COLUMNS);
                disk1.setEditable(false);
                

                main.disk2 = new JTextArea(AREA_ROWS, AREA_COLUMNS);
                disk2.setEditable(false);

                main.printer1 = new JTextArea(AREA_ROWS, AREA_COLUMNS);
                printer1.setEditable(false);

                main.printer2 = new JTextArea(AREA_ROWS, AREA_COLUMNS);
                printer2.setEditable(false);

                main.printer3 = new JTextArea(AREA_ROWS, AREA_COLUMNS);
                printer3.setEditable(false);
    
                JPanel overallPanel = new JPanel();
                overallPanel.setLayout(new BorderLayout());


                JPanel showPanel = new JPanel();
                showPanel.setLayout(new GridLayout(1, 5)); 

                JPanel panel;
                JLabel label;
                JScrollPane scrollPane;

                scrollPane = new JScrollPane(disk1);
                label = new JLabel("DISK1");
                panel = new JPanel();
                panel.setLayout(new BorderLayout()); 
                panel.add(label, BorderLayout.NORTH);
                panel.add(scrollPane, BorderLayout.CENTER);
                showPanel.add(panel);

                scrollPane = new JScrollPane(disk2);
                label = new JLabel("DISK2");
                panel = new JPanel();
                panel.setLayout(new BorderLayout()); 
                panel.add(label, BorderLayout.NORTH);
                panel.add(scrollPane, BorderLayout.CENTER);
                showPanel.add(panel);

                scrollPane = new JScrollPane(printer1);
                label = new JLabel("PRINTER1");
                panel = new JPanel();
                panel.setLayout(new BorderLayout()); 
                panel.add(label, BorderLayout.NORTH);
                panel.add(scrollPane, BorderLayout.CENTER);
                showPanel.add(panel);
                scrollPane = new JScrollPane(printer2);
                label = new JLabel("PRINTER2");
                panel = new JPanel();
                panel.setLayout(new BorderLayout()); 
                panel.add(label, BorderLayout.NORTH);
                panel.add(scrollPane, BorderLayout.CENTER);
                showPanel.add(panel);
                scrollPane = new JScrollPane(printer3);
                label = new JLabel("PRINTER3");
                panel = new JPanel();
                panel.setLayout(new BorderLayout()); 
                panel.add(label, BorderLayout.NORTH);
                panel.add(scrollPane, BorderLayout.CENTER);
                showPanel.add(panel);
     
                overallPanel.add(showPanel, BorderLayout.CENTER);


                JPanel buttonPanel = new JPanel();
                listener = new ChoiceListener();

                slowButton = new JRadioButton("0.5x SPEED");
                slowButton.addActionListener(listener);
                
                regButton = new JRadioButton("REGULAR SPEED");
                regButton.addActionListener(listener); 
                regButton.setSelected(true); 

                fastButton = new JRadioButton("2x SPEED");
                fastButton.addActionListener(listener); 

                ButtonGroup group = new ButtonGroup(); 
                group.add(slowButton); 
                group.add(regButton); 
                group.add(fastButton); 
                buttonPanel.add(slowButton);
                buttonPanel.add(regButton);
                buttonPanel.add(fastButton);

                overallPanel.add(buttonPanel, BorderLayout.SOUTH);
                overallPanel.add(userPanel, BorderLayout.NORTH);

                add(overallPanel);
                setSize(FRAME_WIDTH, FRAME_HEIGHT);
            }

            class ChoiceListener implements ActionListener {
                public void actionPerformed(ActionEvent event) {
                    setSpeed();
                }
            }

            public void setSpeed() {
                if (slowButton.isSelected()) { 
                    speed = 2.0; 
                } else if(regButton.isSelected()) {
                    speed = 1.0;
                } else if(fastButton.isSelected()) {
                    speed = 0.5;
                }
            }
    
    
        }

        int userNum = Integer.parseInt(args[0]) * -1;
        String[] userList = new String[userNum];

        for(int i = 1; i < userNum + 1; i++) {
            userList[i - 1] = args[i];
        }

        int diskNum = Integer.parseInt(args[userNum + 1]) * -1;
        int printerNum = Integer.parseInt(args[userNum + 2]) * -1;

        main.withGUI = true;

        if(args[args.length - 1].equals("-ng")) {
            main.withGUI = false;
        }

        if(main.withGUI){
            JFrame frame = new GUIFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            frame.setTitle("141OS");
            frame.setVisible(true);
        } 
        DirectoryManager dirM = new DirectoryManager();
        DiskManager diskM = new DiskManager(diskNum);
        PrinterManager printM  = new PrinterManager(printerNum);

        UserThread [] userThreadList = new UserThread[userNum];
        for(int i = 0; i < userNum; i++) {
            if(main.withGUI)
                main.USERS.append("USER" + (i + 1) + '\n');
            userThreadList[i] = new UserThread(i + 1, diskM, dirM, printM);
            userThreadList[i].start();
            
        }

        // System.out.println("FISISHED RUNNING");
        
    }

    
}

class Printer
    // extends Thread
{
    int printerNum;

    Printer(int id)
    {
        this.printerNum = id;
    }
    void print(StringBuffer b, int printerNum)
    {
        BufferedWriter bw = null;
        try{

            Thread.sleep((long) (2750 * main.speed)); 
            File out = new File("PRINTER" + this.printerNum);

            if (!out.exists()) {
                out.createNewFile();
            }

            FileWriter fw = new FileWriter(out, true);
	        bw = new BufferedWriter(fw);
            bw.write(b.toString() + "\n");
            if(main.withGUI) {
                if(printerNum == 0) {
                    main.printer1.append(b.toString() + '\n');
                } else if(printerNum == 1) {
                    main.printer2.append(b.toString() + '\n');
                } else {
                    main.printer3.append(b.toString() + '\n');
                }
            }
        }
        catch(IOException | InterruptedException Except) {
            System.out.println("Caught Error " + Except + " in Printer");
        }
        finally {
            try{
                if(bw!=null)
                    bw.close();
            }
            catch(Exception ex){
                System.out.println("Error in closing the BufferedWriter "+ex+" in Printer");
            }
        }
    }
}



class Disk
    // extends Thread
{
    static final int NUM_SECTORS = 1024;
    StringBuffer sectors[] = new StringBuffer[NUM_SECTORS];

    Disk(int capacity) {
        for(int i = 0; i < sectors.length; i++) {
            sectors[i] = new StringBuffer(capacity);
        }
    }

    void read(int sector, StringBuffer data)
    {
        try{
            Thread.sleep((long) (200 * main.speed));
            data.setLength(0);
            data.append(sectors[sector]);
        }
        catch(InterruptedException IE){
            System.out.println("Caught InterruptedException Error in Disk");
        }
        
    }
    void write(int sector, StringBuffer data)
    {
        try{
            Thread.sleep((long) (200 * main.speed));
            sectors[sector] = data;
        }
        catch(InterruptedException IE){
            System.out.println("Caught InterruptedException Error in Disk");
        }
    }
}


class PrintJobThread
    extends Thread
{
    StringBuffer line = new StringBuffer(); // only allowed one line to reuse for read from disk and print to printer

    StringBuffer fileName;
    PrinterManager printM;
    DirectoryManager dirM;
    DiskManager diskM;
    PrintJobThread(StringBuffer fileName, PrinterManager printM, DirectoryManager dirM, DiskManager diskM)
    {
        this.fileName = fileName;
        this.printM = printM;
        this.dirM = dirM;
        this.diskM = diskM;

            
    }
    
    void print(int userID)
    {
        try {

            FileInfo f = dirM.lookup(fileName);
            int start = f.startingSector;
            int diskNum = f.diskNumber;

            int PrinterNum = printM.request();
            if(main.withGUI == true) {
                if(PrinterNum == 0) {
                    main.printer1.append("USER" + userID + "\'s job: PRINTER " + (PrinterNum + 1) + " printing " + fileName + '\n');
                } else if(PrinterNum == 1) {
                    main.printer2.append("USER" + userID + "\'s job: PRINTER " + (PrinterNum + 1) + " printing " + fileName + '\n');
                } else {
                    main.printer3.append("USER" + userID + "\'s job: PRINTER " + (PrinterNum + 1) + " printing " + fileName + '\n');
                }
            }

            for(int i = 0; i < f.fileLength; i++) {
                diskM.diskArray[diskNum].Disk.read(start + i, line);
                printM.printerArray[PrinterNum].print(line, PrinterNum);
            }

            printM.release(PrinterNum);

            
        }
        catch(InterruptedException IE){
            System.out.println("Caught InterruptedException Error in PrintJobThread");
        }

    }
}


class UserThread
    extends Thread
{
    int userID;
    DiskManager diskM;
    DirectoryManager dirM;
    PrinterManager printM;
    File file;
    UserThread(int id, DiskManager diskM, DirectoryManager dirM, PrinterManager printM)
    {
        this.userID = id;
        this.diskM = diskM;
        this.dirM = dirM;
        this.printM = printM;

        this.file = new File("../inputs/USER" + userID);

        
    }

    public void run() {
        processCommandsIn();
    }

    void processCommandsIn() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
               switch(commandOf(line)) {
                    case ".save":
                        saveFile(argumentOf(line), br);
                        break;
                    case ".print":
                        printFile(argumentOf(line));
                        break;

                    default:
                        System.out.println("Unknown Command: " + line + " in UserThread");
                        break;
               }
            }
        }
        catch(IOException IO) {
            System.out.println("Caught Error " + IO + " in UserThread");
        }

    }
    String commandOf(String line) {
        String arr[] = line.split(" ", 2);

        return arr[0];
    }

    String argumentOf(String line) {
        String arr[] = line.split(" ", 2);

        return arr[1];
    }

    void saveFile(String fileName, BufferedReader br) {
        try {
            int diskNum = diskM.request();
            if(main.withGUI == true) {
                if(diskNum == 0) {
                    main.disk1.append("USER" + userID + "\'s job: " +  "DISK " + (diskNum + 1) + " saving " + fileName + '\n');
                } else {
                    main.disk2.append("USER" + userID + "\'s job: " +  "DISK " + (diskNum + 1) + " saving " + fileName + '\n');
                }
            }
            int startingSector = diskM.getNextFreeSectorOnDisk(diskNum);
            int fileLines = 0;
            String line;
            while (!(line = br.readLine()).equals(".end")) {

                diskM.diskArray[diskNum].Disk.write(startingSector + fileLines, new StringBuffer(line));
                fileLines++;
            }
            dirM.enter(new StringBuffer(fileName), new FileInfo(diskNum, startingSector, fileLines));
            diskM.setNextFreeSectorOnDisk(diskNum, fileLines);
            diskM.release(diskNum);
        } 
        catch(IOException | InterruptedException Except) {
            System.out.println("Caught Error " + Except + " in UserThread");
        }
        
    }

    void printFile(String fileName) {
        StringBuffer file = new StringBuffer(fileName);
        PrintJobThread PJThread = new PrintJobThread(file, printM, dirM, diskM);
        PJThread.print(userID);
    }

}


class FileInfo {
    int diskNumber;
    int startingSector;
    int fileLength;

    FileInfo(int diskNumber, int startingSector, int fileLength) {
        this.diskNumber = diskNumber;
        this.startingSector = startingSector;
        this.fileLength = fileLength;
    }
}

class DirectoryManager {
    private Hashtable<String, FileInfo> T = new Hashtable<String, FileInfo>();

    void enter(StringBuffer fileName, FileInfo file) {
        T.put(fileName.toString(), file);
    }
    FileInfo lookup(StringBuffer fileName) {
        return T.get(fileName.toString());
    }
}

class diskInfo {
    int startingSector;
    Disk Disk;

    diskInfo(int startingSector) {
        this.startingSector = startingSector;
        this.Disk = new Disk(16);
    }
}
    
class DiskManager
    extends ResourceManager
{
    diskInfo diskArray[];
    DiskManager(int numberOfItems) {
        super(numberOfItems);
        diskArray = new diskInfo[numberOfItems];
		for (int i=0; i<diskArray.length; ++i)
			diskArray[i] = new diskInfo(0);
    }

    int getNextFreeSectorOnDisk(int diskNumber) {
        return diskArray[diskNumber].startingSector;
    }

    void setNextFreeSectorOnDisk(int diskNumber, int fileLines){
        diskArray[diskNumber].startingSector = diskArray[diskNumber].startingSector + fileLines;
    }
}

class PrinterManager
    extends ResourceManager
{
    Printer printerArray[];
    PrinterManager(int numberOfItems) {
        super(numberOfItems);
        printerArray = new Printer[numberOfItems];
        for(int i = 0; i < numberOfItems; i++) {
            printerArray[i] = new Printer(i + 1);
        }
    }
}



class ResourceManager {
	boolean isFree[];
	ResourceManager(int numberOfItems) {
		isFree = new boolean[numberOfItems];
		for (int i=0; i<isFree.length; ++i)
			isFree[i] = true;
	}
	synchronized int request() throws InterruptedException {
        while (true) {
            for (int i = 0; i < isFree.length; ++i)
                if ( isFree[i] ) {
                    isFree[i] = false;
                    return i;
                }
            this.wait(); // block until someone releases Resource
        }
		
    }

    synchronized void release( int index ) {
		isFree[index] = true;
		this.notify(); // let a blocked thread run
	}
}


