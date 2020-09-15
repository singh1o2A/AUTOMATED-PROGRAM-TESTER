import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class server 
{
  public static int SOCKET = 55588;   
  public static void main (String [] args ) throws IOException 
  {
   String test = args[0]; 

    FileInputStream fis = null;
    BufferedInputStream bis = null;
    DataOutputStream os = null;
    ServerSocket servsock = null;
    Socket sock = null;
    FileWriter reportWriter = null;
    String fileName = null;
    File fileReport = null;
    try {
      servsock = new ServerSocket(SOCKET);
    while(true) 
     {	
      System.out.println("Waiting.......................");
        try 
        {
          sock = servsock.accept();
          System.out.println("Accepted : " + sock);
          
          //Sending page.class to client
          File myFile = new File ("Page.class");
          byte [] array  = new byte [(int)myFile.length()];
          bis = new BufferedInputStream(new FileInputStream(myFile));
          bis.read(array,0,array.length);
          os = new DataOutputStream(sock.getOutputStream());
          System.out.println("Sending " + "Page.class" );
          os.writeUTF("Page.class");
          os.writeInt(array.length);
          os.write(array,0,array.length);
          os.flush();
          System.out.println("Done.");
          
	  //Receiving C program from client
          DataInputStream is = new DataInputStream(sock.getInputStream());
          String studentName = is.readUTF();
    	  fileName = is.readUTF();
          int fileSize = is.readInt();
          Process x=Runtime.getRuntime().exec("mkdir "+studentName);
          x.waitFor();
          fileReport = new File(studentName,"report"); 
	  fileReport.createNewFile();
          System.out.print("Hi");
          File file = new File(studentName,fileName); 
          file.createNewFile();
    	  BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
          BufferedOutputStream bos_int = new BufferedOutputStream(new FileOutputStream(file));
    	  byte [] bytearray  = new byte [fileSize];
    	  is.read(bytearray,0,bytearray.length);
    	  bos_int.write(bytearray, 0 ,bytearray.length);
     	  bos.write(bytearray, 0 ,bytearray.length);
    	  bos_int.flush();
          bos.flush();
    	  bos.close();
         
	  //testing the program
          reportWriter = new FileWriter(fileReport);
          BufferedReader br= new BufferedReader(new FileReader(test));
          String str="";
          int count = 0; 
	  Process p,p1;
          p1 = Runtime.getRuntime().exec("gcc "+ fileName +" -o _a");
	  try{p1.waitFor();} catch(Exception e){}
          while((str=br.readLine())!=null)
          {
		try
		{
		       p = Runtime.getRuntime().exec("./_a");
		}catch( Exception e ){ reportWriter.write("Compile Unsuccessful"); break; }
		if(count==0)
			reportWriter.write("Compile successful\n");
                BufferedWriter stdOutput = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                while(!str.equals("*")&&!str.equals(null))
                {          
                   stdOutput.write(str+"\n");
                   str=br.readLine();
                }
                stdOutput.flush();

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));        
                str=stdInput.readLine();
                String strF;
                strF=br.readLine();
                count++;
                if(str.equals(strF))
                    reportWriter.write("Test Case "+count+ ": Passed\n");
                else
                    reportWriter.write("Test Case "+count+ ": Failed\n");
                br.readLine();
            }
        }
	catch(Exception e)
	{
  		System.out.println(e);
        }
        finally 
        {
	  reportWriter.close();
	  new File(fileName).delete();
	  new File("a").delete();
	  
	  //Sending Report to client
	  BufferedReader br = new BufferedReader(new FileReader(fileReport)); 
  	  String str= "";
	  String st; 
          while ((st = br.readLine()) != null)
	  { 
   		str=str+st+"\n";
 	  } 
	  os.writeUTF(str);
          bis.close();
          os.close();
          sock.close();
        }
      }
    }
    finally 
    {
      servsock.close();
    }
  }
}

