import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LogTailer implements Runnable{

	private File file;
	private long start;
	private long to;

	public LogTailer(File file, long start, long to) {
		this.file = file;
		this.start = start == 0 ? -1000 : start;
		this.to = to;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void run() {
		
		try {
			RandomAccessFile rFile = new RandomAccessFile(file, "r");
		
			start = rFile.length() + start;
			if (start < 0) {
				start = 0;
			}
			
			to = rFile.length() + to;
			if (to < 0) {
				to = rFile.length();
			}
			
			rFile.seek(start);
			while(true) {
				String line = rFile.readLine();
				System.out.println(line);
				
				if (to <= rFile.getFilePointer()) {
					break;
				}
			}

			rFile.seek(rFile.length());
			
			String line = null;
			while (true){
				
				line = rFile.readLine();
				if (line != null) {
					System.out.println(line);
					continue;
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
				
            }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void main(String[] args) {
		if (args.length == 0 || args.length > 3) {
			System.out.println("3 arguments needed. {filepath} {start} {to}");
			System.exit(0);
		}
		
		long start = 0;
		long end = 0;
		if (args.length > 0 && args.length == 3) {
			start = Long.valueOf(args[1]);
			end = Long.valueOf(args[2]);
		}
		
		File file = new File(args[0]);
		LogTailer logTailer = new LogTailer(file, start, end);
		new Thread(logTailer).start();
	}

	
}

