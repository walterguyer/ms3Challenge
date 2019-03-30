package ms3Challenge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class ms3ChallengeApp {

	private final static String DATABASE_URL = "jdbc:sqlite:mem:ms3csv.db";
//	private final static String DATABASE_URL = "jdbc:sqlite:C://Users/Walter Guyer/Desktop/ms3csv.db";
	
	private Dao<goodData, Integer> goodDataDao;
	private Dao<badData, Integer> badDataDao;
	
	private String chosenPath;
	
	public static void main(String[] args) throws Exception {
		
		new ms3ChallengeApp().doMain(args);
		
	}

	private void doMain(String[] args) throws Exception {
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		ConnectionSource connectionSource = null;
		try {
			connectionSource = new JdbcConnectionSource(DATABASE_URL);
			setupDatabase(connectionSource);
			promptData();
			printStats();
			dropTables(connectionSource);
			
		}finally {
			if(connectionSource != null) {
				connectionSource.close();
			}
		}
		
	}
	

	private void setupDatabase(ConnectionSource connectionSource) throws Exception {
		goodDataDao = DaoManager.createDao(connectionSource, goodData.class);
		badDataDao = DaoManager.createDao(connectionSource, badData.class);
		
		TableUtils.createTableIfNotExists(connectionSource, goodData.class);
		TableUtils.createTableIfNotExists(connectionSource, badData.class);
		
	}

	private void dropTables(ConnectionSource connectionSource) throws SQLException {
		
		TableUtils.dropTable(connectionSource, goodData.class, true);
		TableUtils.dropTable(connectionSource, badData.class, true);
		
	}
	
	public void promptData() throws Exception{

		JOptionPane.showMessageDialog(null, "Please choose the .csv file to be filtered.");
		
		//select csv file
		JFileChooser selectFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		selectFile.setFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
		int returnValue = selectFile.showOpenDialog(null);
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = selectFile.getSelectedFile();
			
			try {
				CSVReader reader = new CSVReader(new FileReader(selectedFile));
				writeBadData(filterData(reader));
				reader.close();
			} catch (FileNotFoundException e) {
				System.out.printf("File Not Found exception {}", e);
			} catch (IOException e) {
				System.out.printf("IO Exception {}", e);
			}
			
		}	
	}
	
	private void writeBadData(List<String[]> badData) throws IOException {
		
		//select path for new csv file
		JOptionPane.showMessageDialog(null, "Please choose path for bad data csv.");
		JFileChooser selectPath = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		selectPath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if(selectPath.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			String filePath = "\\bad-data-" + new SimpleDateFormat("yyyMMddHHmmss").format(new Date()) + ".csv";
			File file = new File(selectPath.getSelectedFile().getAbsolutePath() + filePath);
			
			//save path to be used for logs
			chosenPath = selectPath.getSelectedFile().getAbsolutePath();
			
			FileWriter fileWriter = new FileWriter(file);
			CSVWriter writer = new CSVWriter(fileWriter);
			writer.writeAll(badData);
			writer.close();
		}
		
	}

	public List<String[]> filterData(CSVReader reader) throws IOException, Exception {
		String[] line;
		List<String[]> badDataList = new ArrayList<String[]>();
		
		//eats column definitions
		reader.readNext();
		
		while ((line = reader.readNext()) != null) {
			List<String> records = Arrays.asList(line);
			if(records.size() != 1) {
				if(records.contains(null) || records.contains("") || records.size() != 10) {
					badDataList.add(line);
					badData data = new badData(records);
					badDataDao.create(data);
				}else {
					goodData data = new goodData(records);
					goodDataDao.create(data);
				}
			}
		}
		return badDataList;
	}

	private void printStats() throws Exception {
		QueryBuilder<goodData, Integer> queryGood = goodDataDao.queryBuilder();
		QueryBuilder<badData, Integer> queryBad = badDataDao.queryBuilder();
		
		Long numGood = queryGood.countOf();
		Long numBad = queryBad.countOf();
		
		String message = "Data filtering is complete: \n"
				+ "Total Records = " + (numGood + numBad) + "\n"
				+ "Good Records = " + numGood + "\n"
				+ "Bad Records = " + numBad + "\n";
		
		JOptionPane.showMessageDialog(null, message + "**Logfile has been written to same directory as bad-data.csv");
		
		File statsLog = new File(chosenPath + "\\logfile.txt");
		FileWriter logWriter = new FileWriter(statsLog);
		logWriter.write(message);
		logWriter.close();
	}
}	

