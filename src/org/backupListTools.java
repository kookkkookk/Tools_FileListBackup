package org;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class backupListTools {
	
	//attr OS 
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String OSis = "";
	
	//Window or Linux path type
	private static String pathSlashType = "\\";
	
	//Data
	static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
    static Date startDateTime = new Date();
    static String strDate = sdFormat.format(startDateTime);
    
    
    static int totalFile = 0;
    static int completeCopyFile = 0;
    static int errorCopyFile = 0;
    static ArrayList<String> errorCopyFileArr = new ArrayList<String>();
    //bat�i�ά۹���|�@�@//Eclipse �ݵ�����|  "C:\\filebackuptothis\\"
    //static String mainFolder = "C:\\filebackuptothis\\Migrate_source"+pathSlashType; 
    static String mainFolder = "";
    static String temp = "";
    
    public static void main(String[] args) throws IOException{
		//System.out.println(OS);
		//check OS
		if (isWindows()) {
			OSis = "win";
            pathSlashType = "\\";
            mainFolder = "Migrate_source"+pathSlashType;
            System.out.println("This is Windows");
        } else if (isMac()) {
        	OSis = "mac";
            System.out.println("This is Mac");
        } else if (isUnix()) {
        	OSis = "lux";
            pathSlashType = "/";
            mainFolder = "Migrate_source"+pathSlashType;
            System.out.println("This is Unix or Linux");
        } else if (isSolaris()) {
        	OSis = "sol";
            System.out.println("This is Solaris");
        } else {
        	OSis = "no";
            System.out.println("Your OS is not support!!");
        }
		//System.out.println(new File(".").getAbsoluteFile());//�ˬd�ثe���|
		//Eclipse �ݵ�����|
    	//FileReader fileTxt = new FileReader("C:"+pathSlashType+"Users"+pathSlashType+"ChinaDragon_Ben"+pathSlashType+"Desktop"+pathSlashType+"java"+pathSlashType+"fetnetFaqConvert"+pathSlashType+"src"+pathSlashType+"fetnet"+pathSlashType+"faq"+pathSlashType+"convert"+pathSlashType+"filepath.txt");
		//bat�i�ά۹���|
    	FileReader fileTxt = new FileReader("filelist.txt");
    	BufferedReader readFile = new BufferedReader(fileTxt);
    	String line;
    	//String line,tempstring;
    	//String[] tempArray = new String[3];
    	//ArrayList myList = new ArrayList();
    	ArrayList<String> myList = new ArrayList<String>();
    	int i=0;
    	while( (line = readFile.readLine() ) != null){
    		//tempstring = line;
            myList.add(line); 
    	}
    	readFile.close();
    	
    	//������´���
    	for (int j = 0; j < myList.size(); j++) {
            if (myList.get(j).equals("")) {
            	myList.remove(j);
                j--;
            }
        }
    	//�����D������|��
        if(OSis.equals("win")){
            for (int jj = 0; jj < myList.size(); jj++) {
                if (myList.get(jj).indexOf(":\\") == -1) {
                    myList.remove(jj);
                    jj--;
                }
            }
        }
        /*else if(OSis.equals("lux")){
        	for (int jj = 0; jj < myList.size(); jj++) {
                if (myList.get(jj).substring(0,1) != "/") {
                    myList.remove(jj);
                    jj--;
                }
            }
        }*/
    	
    	//System.out.println(myList);
    	//System.out.println("Path Total: " + myList.size());
    	
    	for (int k = 0; k < myList.size(); k++) {
    		
    		//System.out.println(myList.get(k));
    		//File folder = new File(myList.get(k));
    		//listFilesForFolder(folder);
    		
    		//System.out.println( myList.get(k).lastIndexOf("\\") );
    		//System.out.println( myList.get(k).substring(myList.get(k).lastIndexOf("\\")+1) );
    		if(myList.get(k).substring(myList.get(k).lastIndexOf(pathSlashType)+1).indexOf(".") == -1){
    			//System.out.println("���G�O��Ƨ�");
    			File folder = new File( myList.get(k)+pathSlashType );
    			listFilesForFolder( folder );
    		}else{
    			//System.out.println("���G�O�ɮ�");
    			totalFile++;
    			//System.out.println( myList.get(k).substring(0,myList.get(k).lastIndexOf("\\")+1) );
    			makeFolderFn( myList.get(k).substring(0,myList.get(k).lastIndexOf(pathSlashType)+1) );
    			startCopyWorking( myList.get(k).substring(0,myList.get(k).lastIndexOf(pathSlashType)+1), myList.get(k).substring(myList.get(k).lastIndexOf(pathSlashType)+1) );
    		}
    		
    	}
    	Date endDateTime = new Date();
        String endDate = sdFormat.format(endDateTime);
    	
    	System.out.println("Start Run at: " + strDate);
    	System.out.println("  End Run at: " + endDate);
    	
    	System.out.println("Path Total: " + myList.size() );
    	System.out.println( "Total File: "+totalFile+" ,Complete the number of copies: "+completeCopyFile );
    	if(errorCopyFile>0){
    		System.out.println( "Copy number of failures: "+errorCopyFile );
    		for(int err=0;err<errorCopyFile;err++){
    			System.out.println( "Copy failures: "+errorCopyFileArr.get(err) );
    		}
    	}
    	
    }
	
	//�s�@��Ƨ����|
	public static void makeFolderFn(String folderPath) {
		String OSfolderPath = "";
		if(OSis.equals("win")){
			OSfolderPath = folderPath.substring(3);
		}else if(OSis.equals("lux")){
			OSfolderPath = folderPath.substring(1);
		}
		File createNewFolder = new File(mainFolder+strDate+pathSlashType+OSfolderPath);
        createNewFolder.mkdirs();
	}
	
	//�N�ɮ׽ƻs���m
	public static void startCopyWorking(String folderPath, String filee) {
		String OSfolderPath = "";
		if(OSis.equals("win")){
			OSfolderPath = folderPath.substring(3);
		}else if(OSis.equals("lux")){
			OSfolderPath = folderPath.substring(1);
		}
		try {
			int bytesum = 0;
			int byteread = 0;
			//System.out.println("folderPath: "+folderPath);
			//System.out.println("filee:"+filee);
			File copyFile = new File(folderPath+filee);
			
			if (copyFile.exists()) {//�ɦs�b��
				InputStream inStream = new FileInputStream(folderPath+filee);//Ū�J����
				FileOutputStream fs = new FileOutputStream(mainFolder+strDate+pathSlashType+OSfolderPath+filee);
				byte[] buffer = new byte[1444];
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //�줸�ռ� �ɮפj�p
					fs.write(buffer, 0, byteread);//�s�J�ɮ�
				}
				System.out.println( "Complete the copied file: " + mainFolder+strDate+pathSlashType+OSfolderPath+filee );
				completeCopyFile++;
			}else{
				errorCopyFile++;
				errorCopyFileArr.add(folderPath+filee);
				return;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//�N��Ƨ�����`�h�j��
	public static void listFilesForFolder(final File folder) {
		
		for (final File fileEntry: folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	            //���G�O��Ƨ�
	        } else {
	            if (fileEntry.isFile()) {
	            	//���G�O�ɮ�
	            	totalFile++;
	                temp = fileEntry.getName();
	                //if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("html"))
	                    //System.out.println("������|�ɦW: " + folder.getAbsolutePath() + pathSlashType + fileEntry.getName());
	                	String folderPath = folder.getAbsolutePath();
	                	//String postionNoC = folder.getAbsolutePath().substring(3);
	                	String filee = fileEntry.getName();  
	                	makeFolderFn( folderPath );
	                	startCopyWorking( folderPath+pathSlashType, filee );
	            }
	        }
	    }
	}
	
	// OS check function
	public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }
    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }
    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }
    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
    public static String getOS(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }
    

}
